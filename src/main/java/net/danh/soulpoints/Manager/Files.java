package net.danh.soulpoints.Manager;

import net.danh.soulpoints.SoulPoints;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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
        if (!dataFile.exists()) SoulPoints.getInstance().saveResource("data.yml", false);
        if (!mobFile.exists()) SoulPoints.getInstance().saveResource("mobs.yml", false);

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

    public static void reloadConfigs() {
        config = YamlConfiguration.loadConfiguration(configFile);
        lang = YamlConfiguration.loadConfiguration(langFile);
        data = YamlConfiguration.loadConfiguration(dataFile);
        mob = YamlConfiguration.loadConfiguration(mobFile);
    }

    public static void saveconfigs() {
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

    public static int getLives(Player p) {
        return getdata().getInt("Lives." + p.getUniqueId() + ".life");
    }

    public static void setLives(Player p, int number) {
        getdata().set("Lives." + p.getUniqueId() + ".life", number);
        save();
    }

    public static void addLives(Player p, int number) {
        getdata().set("Lives." + p.getUniqueId() + ".life", getLives(p) + number);
        save();
    }

    public static void removeLives(Player p, int number) {
        getdata().set("Lives." + p.getUniqueId() + ".life", getLives(p) - number);
        save();
    }

    public static void resetLives(Player p) {
        getdata().set("Lives." + p.getUniqueId() + ".life", getConfig().getInt("General.Normal"));
        save();
    }

    public static String convert(String s) {
        return s.replaceAll("&", "§");
    }

}
