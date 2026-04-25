package dev.sillibeans.everythingpickaxe.config;

import java.nio.file.Paths;

public class Config {
    public int swingTime = 6;
    public Config()  {
        String currentDir = Paths.get(".").toAbsolutePath().normalize().toString();
        System.out.println("Working Directory = " + currentDir);
    }
}