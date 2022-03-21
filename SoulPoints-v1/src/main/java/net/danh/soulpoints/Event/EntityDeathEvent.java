package net.danh.soulpoints.Event;

import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import net.danh.soulpoints.Manager.Data;
import net.danh.soulpoints.Manager.Files;
import org.bukkit.Bukkit;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;
import java.util.Random;

public class EntityDeathEvent implements Listener {

    @EventHandler
    public void onKill(org.bukkit.event.entity.EntityDeathEvent e) {
        if (Files.getConfig().getBoolean("Mobs.Enable")) {
            LivingEntity mob = e.getEntity();
            Player player = mob.getKiller();
            String vanillamobs = e.getEntityType().toString();
            if (player == null) {
                return;
            }
            if (Bukkit.getServer().getPluginManager().getPlugin("MythicMobs") != null) {
                BukkitAPIHelper mythicMobsAPI = new BukkitAPIHelper();
                if (mythicMobsAPI.isMythicMob(mob)) {
                    return;
                }
            }
            if (mob instanceof Animals) {
                if (!Files.getConfig().getBoolean("Mobs.Animals")) {
                    return;
                }
            }
            if (mob instanceof Monster) {
                if (!Files.getConfig().getBoolean("Mobs.Monster")) {
                    return;
                }
            }
            Random randomInt = new Random();
            int max = Files.getmob().getInt("Vanilla.Default.max");
            int min = Files.getmob().getInt("Vanilla.Default.min");
            int chance = Files.getmob().getInt("Vanilla.Default.chance");
            if (max == 0) {
                if (min == 0) {
                    if (chance == 0) {
                        return;
                    }
                }
            }
            for (String getEntityType : Objects.requireNonNull(Files.getmob().getConfigurationSection("Vanilla.")).getKeys(false)) {
                if (vanillamobs.equalsIgnoreCase(getEntityType)) {
                    max = Files.getmob().getInt("Vanilla." + vanillamobs + ".max");
                    min = Files.getmob().getInt("Vanilla." + vanillamobs + ".min");
                    chance = Files.getmob().getInt("Vanilla." + vanillamobs + ".chance");
                    break;
                }
            }
            max = max - min;
            int random = min + randomInt.nextInt(max);
            double chancee = Math.random() * 100.0D;
            if (chancee <= chance) {
                Data.addLives(player, random);
                player.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Kill-mobs-message")).replace("%souls%", Integer.toString(random)).replace("%mob%", mob.getName()));
            }
        }
    }
}
