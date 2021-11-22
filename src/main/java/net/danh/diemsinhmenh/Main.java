package net.danh.diemsinhmenh;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import net.danh.diemsinhmenh.commands.TabComplete;
import net.danh.diemsinhmenh.commands.commands;
import net.danh.diemsinhmenh.event.UpdateChecker;
import net.danh.diemsinhmenh.event.death;
import net.danh.diemsinhmenh.hook.MythicMobsHook;
import net.danh.diemsinhmenh.hook.placeholder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        PluginManager manager = getServer().getPluginManager();
        if (manager.isPluginEnabled("MythicMobs")) {
            getLogger().log(Level.INFO, "Hooked onto MythicMobs");
            manager.registerEvents(new MythicMobsHook(this), this);
        }

        if (manager.isPluginEnabled("PlaceholderAPI")) {
            new placeholder(this).register();
            getLogger().log(Level.INFO, "Hooked onto PlaceholderAPI");
        }
        Metrics metrics = new Metrics(this, 12918);
        getCommand("souls").setExecutor(new commands(this));
        getCommand("souls").setTabCompleter(new TabComplete());
        getServer().getPluginManager().registerEvents(new death(this), this);
        createConfigs();
        if (getConfig().getInt("config-version") != 9) {
            getLogger().severe("Outdated config! Please backup & update config.yml file and restart server again!!");
        }
        if (getlang().getInt("lang-version") != 3) {
            getLogger().severe("Outdated lang! Please backup & update lang.yml file and restart server again!!");
        }
        new UpdateChecker(this, 96396).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info(ChatColor.GREEN + "There is not a new update available.");
            } else {
                getLogger().info(ChatColor.RED + "There is a new update available.");
                getLogger().info(ChatColor.YELLOW + "Download: " + ChatColor.BLUE + "https://www.spigotmc.org/resources/96396/");
            }
        });
        (new BukkitRunnable() {
            public void run() {
                Iterator var2 = Bukkit.getOnlinePlayers().iterator();

                while (var2.hasNext()) {
                    Player p = (Player) var2.next();
                    List<String> w = getConfig().getStringList("available-worlds");
                    if (w.contains(p.getWorld().getName())) {
                        if (!getdata().contains("Lives." + p.getUniqueId())) {
                            addLives(p, getConfig().getInt("General.Normal"));

                        }

                        if (!getConfig().getBoolean("ActionBar.Enable")) {
                            return;
                        }
                        if (getConfig().getBoolean("ActionBar.Enable")) {
                            p.spigot().sendMessage(
                                    ChatMessageType.ACTION_BAR,
                                    new TextComponent(convert(getConfig().getString("prefix") + getlang().getString("lang." + getConfig().getString("language") + "." + "Soul-message")).replaceAll("%souls%", String.valueOf(getLives(p)))));
                        }
                    }

                }
            }
        }).runTaskTimer(this, 20L, 20L);
        (new BukkitRunnable() {
            public void run() {
                Iterator var2 = Bukkit.getOnlinePlayers().iterator();

                while (var2.hasNext()) {
                    Player p = (Player) var2.next();
                    int sie = getLives(p);


                    if (getConfig().getInt("General.Maximum") <= sie) {
                        return;
                    }

                    List<String> w = getConfig().getStringList("available-worlds");
                    if (w.contains(p.getWorld().getName())) {
                        addLives(p, getConfig().getInt("General.Daily"));
                        p.sendMessage(convert(getConfig().getString("prefix") + getlang().getString("lang." + getConfig().getString("language") + "." + "Soul-earn-message").replaceAll("%souls%", getConfig().getString("General.Daily"))));
                    }
                }
            }
        }).runTaskTimer(this, (long) (this.getConfig().getInt("General.Regen-Time") * 20), (long) (this.getConfig().getInt("General.Regen-Time") * 20));
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Saving data....");
        save();
    }

    private File configFile, langFile, dataFile, mobFile;
    private FileConfiguration config, lang, data, mob;


    public void createConfigs() {
        configFile = new File(getDataFolder(), "config.yml");
        langFile = new File(getDataFolder(), "lang.yml");
        dataFile = new File(getDataFolder(), "data.yml");
        mobFile = new File(getDataFolder(), "mobs.yml");

        if (!configFile.exists()) saveResource("config.yml", false);
        if (!langFile.exists()) saveResource("lang.yml", false);
        if (!dataFile.exists()) saveResource("data.yml", false);
        if (!mobFile.exists()) saveResource("mobs.yml", false);

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

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getlang() {
        return lang;
    }

    public FileConfiguration getdata() {
        return data;
    }

    public FileConfiguration getmob() {
        return mob;
    }

    public void reloadConfigs() {
        config = YamlConfiguration.loadConfiguration(configFile);
        lang = YamlConfiguration.loadConfiguration(langFile);
        data = YamlConfiguration.loadConfiguration(dataFile);
        mob = YamlConfiguration.loadConfiguration(mobFile);
    }

    public void save() {
        try {
            data.save(dataFile);
        } catch (IOException ignored) {
        }
    }

    public int getLives(Player p) {
        return getdata().getInt("Lives." + p.getUniqueId() + ".life");
    }

    public void setLives(Player p, int number) {
        getdata().set("Lives." + p.getUniqueId() + ".life", number);
        save();
    }

    public void addLives(Player p, int number) {
        getdata().set("Lives." + p.getUniqueId() + ".life", this.getLives(p) + number);
        save();
    }

    public void removeLives(Player p, int number) {
        getdata().set("Lives." + p.getUniqueId() + ".life", this.getLives(p) - number);
        save();
    }

    public void resetLives(Player p) {
        getdata().set("Lives." + p.getUniqueId() + ".life", this.getConfig().getInt("General.Normal"));
        save();
    }

    public String convert(String s) {
        return s.replaceAll("&", "§");
    }

}
