package net.danh.soulpoints;

import net.danh.soulpoints.Commands.Commands;
import net.danh.soulpoints.Commands.TabCommand;
import net.danh.soulpoints.Event.EntityDeathEvent;
import net.danh.soulpoints.Event.PlayerDeathEvent;
import net.danh.soulpoints.Event.PlayerJoinEvent;
import net.danh.soulpoints.Hook.MythicMobs;
import net.danh.soulpoints.Hook.PlaceholderAPI;
import net.danh.soulpoints.Manager.Data;
import net.danh.soulpoints.Manager.Files;
import net.danh.soulpoints.Manager.UpdateChecker;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import preponderous.ponder.minecraft.bukkit.abs.PonderBukkitPlugin;
import preponderous.ponder.minecraft.bukkit.tools.EventHandlerRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class SoulPoints extends PonderBukkitPlugin implements Listener {

    private static SoulPoints instance;

    public static SoulPoints getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Metrics metrics = new Metrics(this, 12918);
        if (getServer().getPluginManager().getPlugin("MythicMobs") != null) {
            registerMythicEventHandlers();
            getLogger().log(Level.INFO, "Hooked onto MythicMobs v" + Objects.requireNonNull(getServer().getPluginManager().getPlugin("MythicMobs")).getDescription().getVersion());
            metrics.addCustomChart(new SimplePie("mythicmobs_version", () -> Objects.requireNonNull(getServer().getPluginManager().getPlugin("MythicMobs")).getDescription().getVersion()));
        }
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI().register();
            getLogger().log(Level.INFO, "Hooked onto PlaceholderAPI");
        }
        Objects.requireNonNull(getCommand("souls")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("souls")).setTabCompleter(new TabCommand());
        registerEventHandlers();
        Files.createConfigs();
        if (getConfig().getDouble("config-version") != 1.2) {
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
                    List<String> w = getConfig().getStringList("disable-worlds");
                    if (!w.contains(p.getWorld().getName())) {
                        if (!Files.getdata().contains("Lives." + p.getUniqueId())) {
                            Data.addLives(p, getConfig().getInt("General.Normal"));

                        }

                        if (!getConfig().getBoolean("ActionBar.Enable")) {
                            return;
                        }
                        if (getConfig().getBoolean("ActionBar.Enable")) {
                            p.spigot().sendMessage(
                                    ChatMessageType.ACTION_BAR,
                                    new TextComponent(Files.convert(Objects.requireNonNull(Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Soul-message"))).replaceAll("%souls%", String.valueOf(Data.getLives(p)))));
                        }
                    }

                }
            }
        }).runTaskTimer(this, 20L, 20L);
        (new BukkitRunnable() {
            public void run() {

                for (Player p : Bukkit.getOnlinePlayers()) {
                    int sie = Data.getLives(p);


                    if (getConfig().getInt("General.Maximum") <= sie) {
                        return;
                    }

                    List<String> w = getConfig().getStringList("disable-worlds");
                    if (!w.contains(p.getWorld().getName())) {
                        Data.addLives(p, getConfig().getInt("General.Daily"));
                        p.sendMessage(Files.convert(getConfig().getString("prefix") + Objects.requireNonNull(Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Soul-earn-message")).replaceAll("%souls%", Objects.requireNonNull(Files.getConfig().getString("General.Daily")))));
                    }
                }
            }
        }).runTaskTimer(this, Files.getConfig().getInt("General.Regen-Time") * 20L, Files.getConfig().getInt("General.Regen-Time") * 20L);
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Saving data....");
        Files.save();
        Files.savefiles();
    }


    @Contract(" -> new")
    private @NotNull ArrayList<Listener> initializeListeners() {
        return new ArrayList<Listener>(Arrays.asList(
                new EntityDeathEvent(),
                new PlayerDeathEvent(),
                new PlayerJoinEvent()
        ));
    }

    /**
     * Registers the event handlers of the plugin using Ponder.
     */
    private void registerEventHandlers() {
        ArrayList<Listener> listeners = initializeListeners();
        EventHandlerRegistry eventHandlerRegistry = new EventHandlerRegistry();
        eventHandlerRegistry.registerEventHandlers(listeners, this);
    }

    @Contract(" -> new")
    private @NotNull ArrayList<Listener> initializeMythicListeners() {
        return new ArrayList<Listener>(Arrays.asList(
                new MythicMobs()
        ));
    }

    /**
     * Registers the event handlers of the plugin using Ponder.
     */
    private void registerMythicEventHandlers() {
        ArrayList<Listener> listeners = initializeMythicListeners();
        EventHandlerRegistry eventHandlerRegistry = new EventHandlerRegistry();
        eventHandlerRegistry.registerEventHandlers(listeners, this);
    }
}
