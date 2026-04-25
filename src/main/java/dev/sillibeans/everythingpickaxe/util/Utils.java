package dev.sillibeans.everythingpickaxe.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class Utils {
    public static byte getMapScale(ItemStack stack, Level level) {
        MapId mapId = stack.get(DataComponents.MAP_ID);
        if (mapId != null) {
            MapItemSavedData mapState = MapItem.getSavedData(mapId, level);
            if (mapState != null) {
                return mapState.scale;
            }
        }
        return 0;
    }
}
