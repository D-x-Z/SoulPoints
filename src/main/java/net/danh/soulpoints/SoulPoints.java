package net.danh.soulpoints;

import io.lumine.xikage.mythicmobs.MythicMobs;
import net.danh.soulpoints.Commands.Commands;
import net.danh.soulpoints.Commands.TabCommand;
import net.danh.soulpoints.Hook.MythicMobsHook;
import net.danh.soulpoints.Hook.PlaceholderAPIHook;
import net.danh.soulpoints.Manager.DeathEvent;
import net.danh.soulpoints.Manager.Files;
import net.danh.soulpoints.Manager.UpdateChecker;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.logging.Level;

public class SoulPoints extends JavaPlugin implements Listener {

    private static SoulPoints instance;

    public static SoulPoints getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 12918);
        PluginManager manager = getServer().getPluginManager();
        if (manager.isPluginEnabled("MythicMobs")) {
            if (MythicMobs.inst().getVersion().startsWith("4.1")
                    || MythicMobs.inst().getVersion().startsWith("5")) {
                manager.registerEvents(new MythicMobsHook(), this);
                getLogger().log(Level.INFO, "Hooked onto MythicMobs v" + MythicMobs.inst().getVersion());
            } else {
                getLogger().warning("Cannot hook to MythicMobs! You need update MythicMobs to 4.11+");
            }
            metrics.addCustomChart(new SimplePie("mythicmobs_version", () -> MythicMobs.inst().getVersion()));
        }

        if (manager.isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderAPIHook(this).register();
            getLogger().log(Level.INFO, "Hooked onto PlaceholderAPI");
        }
        getCommand("souls").setExecutor(new Commands());
        getCommand("souls").setTabCompleter(new TabCommand());
        manager.registerEvents(new DeathEvent(), this);
        Files.createConfigs();
        if (getConfig().getDouble("config-version") != 1.1) {
            getLogger().warning("Outdated config! Please backup & update config.yml file and restart server again!!");
        }
        if (Files.getlang().getDouble("lang-version") != 0.4) {
            getLogger().warning("Outdated lang! Please backup & update lang.yml file and restart server again!!");
        }
        new UpdateChecker(this, 96396).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("There is not a new update available.");
            } else {
                getLogger().warning("There's a new update! The new version is v" + version + ", you are using v" + getDescription().getVersion());
                getLogger().warning("Download: https://www.spigotmc.org/resources/96396/");
            }
        });
        (new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    List<String> w = getConfig().getStringList("available-worlds");
                    if (w.contains(p.getWorld().getName())) {
                        if (!Files.getdata().contains("Lives." + p.getUniqueId())) {
                            Files.addLives(p, getConfig().getInt("General.Normal"));

                        }

                        if (!getConfig().getBoolean("ActionBar.Enable")) {
                            return;
                        }
                        if (getConfig().getBoolean("ActionBar.Enable")) {
                            p.spigot().sendMessage(
                                    ChatMessageType.ACTION_BAR,
                                    new TextComponent(Files.convert(Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Soul-message")).replaceAll("%souls%", String.valueOf(Files.getLives(p)))));
                        }
                    }

                }
            }
        }).runTaskTimer(this, 20L, 20L);
        (new BukkitRunnable() {
            public void run() {

                for (Player p : Bukkit.getOnlinePlayers()) {
                    int sie = Files.getLives(p);


                    if (getConfig().getInt("General.Maximum") <= sie) {
                        return;
                    }

                    List<String> w = getConfig().getStringList("available-worlds");
                    if (w.contains(p.getWorld().getName())) {
                        Files.addLives(p, getConfig().getInt("General.Daily"));
                        p.sendMessage(Files.convert(getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Soul-earn-message").replaceAll("%souls%", Files.getConfig().getString("General.Daily"))));
                    }
                }
            }
        }).runTaskTimer(this, Files.getConfig().getInt("General.Regen-Time") * 20L, Files.getConfig().getInt("General.Regen-Time") * 20L);
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Saving data....");
        Files.save();
    }
}
