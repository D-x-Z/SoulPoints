package net.danh.soulpoints.Manager;

import net.danh.soulpoints.SoulPoints;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class Files {

    private static File configFile, langFile, dataFile, mobFile;
    private static FileConfiguration config, lang, data, mob;

    public static void createConfigs() {
        configFile = new File(SoulPoints.getInstance().getDataFolder(), "config.yml");
        langFile = new File(SoulPoints.getInstance().getDataFolder(), "message.yml");
        dataFile = new File(SoulPoints.getInstance().getDataFolder(), "data.yml");
        mobFile = new File(SoulPoints.getInstance().getDataFolder(), "mobs.yml");

        if (!configFile.exists()) SoulPoints.getInstance().saveResource("config.yml", false);
        if (!langFile.exists()) SoulPoints.getInstance().saveResource("message.yml", false);
        if (!mobFile.exists()) SoulPoints.getInstance().saveResource("mobs.yml", false);
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = new YamlConfiguration();
        lang = new YamlConfiguration();
        data = new YamlConfiguration();
        mob = new YamlConfiguration();

        try {
            config.load(configFile);
            lang.load(langFile);
            data.load(dataFile);
            mob.load(mobFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static FileConfiguration getlang() {
        return lang;
    }

    public static FileConfiguration getdata() {
        return data;
    }

    public static FileConfiguration getmob() {
        return mob;
    }

    public static void reloadfiles() {
        config = YamlConfiguration.loadConfiguration(configFile);
        lang = YamlConfiguration.loadConfiguration(langFile);
        mob = YamlConfiguration.loadConfiguration(mobFile);
    }

    public static void savefiles() {
        try {
            config.save(configFile);
            lang.save(langFile);
            mob.save(mobFile);
        } catch (IOException ignored) {
        }
    }

    public static void save() {
        try {
            data.save(dataFile);
        } catch (IOException ignored) {
        }
    }

    @Contract(pure = true)
    public static @NotNull String convert(@NotNull String s) {
        return s.replaceAll("&", "§");
    }

}
