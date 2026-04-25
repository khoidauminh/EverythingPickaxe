package dev.sillibeans.everythingpickaxe.items;

import dev.sillibeans.everythingpickaxe.datacomponent.EverythingPickaxeDataComponents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

class EatEvent {
    public EatEvent() {}
    public void apply(Level level, Player player) {}
}

class EffectEvent extends EatEvent {
    final Holder<MobEffect> effect;
    final int duration;

    EffectEvent(Holder<MobEffect> effect, int duration) {
        this.effect = effect;
        this.duration = duration;
    }

    EffectEvent dur(int dur) {
        return new EffectEvent(this.effect, dur);
    }

    @Override
    public void apply(Level level, Player player) {
        player.addEffect(new MobEffectInstance(this.effect, this.duration, 0, false, false));
    }
}

class DamageEvent extends EatEvent {
    final float amount;
    final Source source;

    public enum Source {
        GENERIC,
        BURN,
    }

    DamageEvent(float a, Source source) {
        this.amount = a;
        this.source = source;
    }

    DamageEvent(float a) {
        this.amount = a;
        this.source = Source.GENERIC;
    }

    DamageEvent dmg(float amount) {
        return new DamageEvent(amount, this.source);
    }

    DamageEvent burn() {
        return new DamageEvent(this.amount, Source.BURN);
    }

    @Override
    public void apply(Level level, Player player) {
        player.hurt(player.damageSources().generic(), amount);
    }
}

class SpawnEvent<T extends Entity> extends EatEvent {
    final EntityType<T> type;
    final BlockPos pos;

    SpawnEvent(EntityType<T> type, BlockPos pos) {
        this.type = type;
        this.pos = pos;
    }

    @Override
    public void apply(Level level, Player player) {
        var entity = this.type.create(level);
        if (entity == null) {
            return;
        }
        entity.setPos(player.getX() + pos.getX(), player.getY() + pos.getY(), player.getZ() + pos.getZ());
        level.addFreshEntity(entity);
    }
}

class LootEvent extends EatEvent {
    final ItemStack stack;

    LootEvent(Item item, int count) {
        this.stack = new ItemStack(item, count);
    }

    @Override
    public void apply(Level level, Player player) {
        final ItemEntity entity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), this.stack);
        level.addFreshEntity(entity);
    }
}

record ChancedEvent<T>(T event, float chance) {
    ChancedEvent(T event) {
        this(event, 1f);
    }

    ChancedEvent<T> chance(float chance) {
        return new ChancedEvent<>(this.event, chance);
    }

    ChancedEvent<T> mod(Function<T, T> f) {
        return new ChancedEvent<>(f.apply(this.event), this.chance);
    }

    public void execute(Level level, Player player) {
        if (player.getRandom().nextFloat() <= chance && this.event instanceof EatEvent e) {
            e.apply(level, player);
        }
    }
}

public class SpoonItem extends Item {
    private final boolean isFilled;

    private static final ChancedEvent<DamageEvent> DAMAGE = new ChancedEvent<>(new DamageEvent(1));
    private static final ChancedEvent<DamageEvent> DAMAGE2 = DAMAGE.mod((e) -> e.dmg(2));
    private static final ChancedEvent<DamageEvent> DAMAGE3 = DAMAGE.mod((e) -> e.dmg(3));

    private static final ChancedEvent<EffectEvent> WITHER = new ChancedEvent<>(new EffectEvent(MobEffects.WITHER, 300));

    private static final ChancedEvent<DamageEvent> BURN = DAMAGE.mod((e) -> e.dmg(1).burn());

    private static final ChancedEvent<EffectEvent> HUNGER = new ChancedEvent<>(new EffectEvent(MobEffects.HUNGER, 300));

    private static final ChancedEvent<EffectEvent> GLOW = new ChancedEvent<>(new EffectEvent(MobEffects.GLOWING, 100));
    private static final ChancedEvent<EffectEvent> OOZING = new ChancedEvent<>(new EffectEvent(MobEffects.OOZING, 2400));

    private static final ChancedEvent<EffectEvent> ABSORBTION = new ChancedEvent<>(new EffectEvent(MobEffects.ABSORPTION, 2400));
    private static final ChancedEvent<EffectEvent> SATURATION = new ChancedEvent<>(new EffectEvent(MobEffects.SATURATION, 100));
    private static final ChancedEvent<EffectEvent> SLOWNESS = new ChancedEvent<>(new EffectEvent(MobEffects.MOVEMENT_SLOWDOWN, 900));
    private static final ChancedEvent<SpawnEvent<Phantom>> PHANTOM = new ChancedEvent<>(new SpawnEvent<>(EntityType.PHANTOM, new BlockPos(0, 2, 0)));
    private static final ChancedEvent<SpawnEvent<Allay>> ALLAY = new ChancedEvent<>(new SpawnEvent<>(EntityType.ALLAY, new BlockPos(0, 2, 0)));
    private static final ChancedEvent<SpawnEvent<ExperienceOrb>> EXPERIENCE = new ChancedEvent<>(new SpawnEvent<>(EntityType.EXPERIENCE_ORB, new BlockPos(0, 0, 0)));
    private static final ChancedEvent<LootEvent> DIAMOND = new ChancedEvent<>(new LootEvent(Items.DIAMOND, 1));

    private static final Map<Block, List<ChancedEvent<?>>> STATS = Map.ofEntries(
            Map.entry(Blocks.DIRT, List.of(DAMAGE.chance(0.8f))),
            Map.entry(Blocks.GRASS_BLOCK, List.of(DAMAGE.chance(0.6f))),
            Map.entry(Blocks.PODZOL, List.of(DAMAGE, HUNGER.chance(0.75f))),
            Map.entry(Blocks.COARSE_DIRT, List.of(DAMAGE2, DAMAGE.chance(0.5f))),
            Map.entry(Blocks.GRAVEL, List.of(DAMAGE3, ABSORBTION.chance(0.1f))),
            Map.entry(Blocks.MOSS_BLOCK, List.of(SATURATION.chance(0.2f))),
            Map.entry(Blocks.MUD, List.of(HUNGER.mod((e) -> e.dur(400)))),
            Map.entry(Blocks.DIRT_PATH, List.of(DAMAGE.chance(0.2f))),
            Map.entry(Blocks.FARMLAND, List.of(DAMAGE.chance(0.5f))),
            Map.entry(Blocks.SOUL_SAND, List.of(PHANTOM.chance(0.6f), ALLAY.chance(0.05f))),
            Map.entry(Blocks.SOUL_SOIL, List.of(SATURATION.chance(0.8f), PHANTOM.chance(0.6f), ALLAY.chance(0.05f))),

            Map.entry(Blocks.SNOW_BLOCK, List.of(SLOWNESS.chance(0.75f))),
            Map.entry(Blocks.SNOW, List.of(SLOWNESS.chance(0.75f))),
            Map.entry(Blocks.POWDER_SNOW_CAULDRON, List.of(SLOWNESS.chance(0.75f))),

            Map.entry(Blocks.POWDER_SNOW, List.of(SLOWNESS, DAMAGE3)),
            Map.entry(Blocks.SUSPICIOUS_GRAVEL, List.of(SLOWNESS, DAMAGE3, DIAMOND.chance(0.01f))),
            Map.entry(Blocks.SAND, List.of(SLOWNESS, DAMAGE3, DIAMOND.chance(0.01f))),

            Map.entry(Blocks.SCULK, List.of(EXPERIENCE)),
            Map.entry(Blocks.SCULK_CATALYST, List.of(EXPERIENCE)),

            Map.entry(Blocks.ROOTED_DIRT, List.of()),
            Map.entry(Blocks.CLAY, List.of(HUNGER)),
            Map.entry(Blocks.FLOWER_POT, List.of(DAMAGE.chance(0.8f))),
            Map.entry(Blocks.CAKE, List.of(SATURATION.chance(0.1f).mod(e -> e.dur(200)))),
            Map.entry(Blocks.COBWEB, List.of(SLOWNESS.chance(0.75f).mod(e -> e.dur(100)))),
            Map.entry(Blocks.GLOW_LICHEN, List.of(GLOW)),
            Map.entry(Blocks.HONEY_BLOCK, List.of(SATURATION)),
            Map.entry(Blocks.SLIME_BLOCK, List.of(HUNGER.mod(e -> e.dur(600)), OOZING)),
            Map.entry(Blocks.MUDDY_MANGROVE_ROOTS, List.of(HUNGER.mod(e -> e.dur(400)), SATURATION)),
            Map.entry(Blocks.PACKED_MUD, List.of()),

            Map.entry(Blocks.LAVA, List.of(DAMAGE.mod(e -> e.dmg(8)), BURN)),
            Map.entry(Blocks.LAVA_CAULDRON, List.of(DAMAGE.mod(e -> e.dmg(8)), BURN)),

            Map.entry(Blocks.WITHER_ROSE, List.of(WITHER))
    );

    public SpoonItem(boolean isFilled) {
        super(SpoonItem.props(isFilled));
        this.isFilled = isFilled;
    }

    public static Item.Properties props(boolean isFilled) {
        var prop = new Item.Properties();

        if (isFilled) {
            return prop
                .stacksTo(1)
                .food(
                    (new FoodProperties.Builder()).nutrition(1)
                        .saturationModifier(1)
                        .alwaysEdible()
                        .build()
                );
        }

        return prop;
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack itemstack, @NotNull BlockState blockstate) {
        return 0.0f;
    }

    @Override
    public boolean isCorrectToolForDrops(
            @NotNull ItemStack itemstack,
            @NotNull BlockState blockstate
    ) {
        return false;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(
            @NotNull ItemStack itemstack,
            @NotNull Level world,
            @NotNull LivingEntity entity
    ) {
        if (!isFilled) {
            return super.finishUsingItem(itemstack, world, entity);
        }

        if (!world.isClientSide() && entity instanceof Player player) {
            final var blockId = itemstack.get(EverythingPickaxeDataComponents.BLOCK_ID.get());
            final var effects = STATS.getOrDefault(BuiltInRegistries.BLOCK.get(blockId), List.of());

            for (var effect : effects) {
                effect.execute(world, player);
            }

            return new ItemStack(EverythingPickaxeItems.SPOON.get(), 1);
        }

        return super.finishUsingItem(itemstack, world, entity);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext ctx) {
        if (this.isFilled) {
            return InteractionResult.PASS;
        }

        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = ctx.getPlayer();

        if (STATS.containsKey(state.getBlock())) {
            level.playSound(
                player,
                pos,
                SoundEvents.GRASS_HIT,
                SoundSource.BLOCKS,
                1.01F,
                1.0F
            );

            var filled_spoon = new ItemStack(EverythingPickaxeItems.SPOON_FILLED.get(), 1);

            filled_spoon.set(EverythingPickaxeDataComponents.BLOCK_ID.get(), BuiltInRegistries.BLOCK.getKey(state.getBlock()));

            assert player != null;
            player.setItemInHand(ctx.getHand(), filled_spoon);

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }
}
