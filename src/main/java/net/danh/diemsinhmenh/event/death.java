package net.danh.diemsinhmenh.event;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.mobs.entities.MythicEntity;
import net.danh.diemsinhmenh.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;

public class death implements Listener {

    private Main main;


    public death(Main main) {
        this.main = main;
    }


    public int getRandomNumber(int min, int max) {
        Random r = new Random();
        int randomNumber = r.nextInt(max - min) + min;
        return randomNumber;
    }
    PluginManager manager = Bukkit.getServer().getPluginManager();

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (main.getConfig().getBoolean("Mobs.Enable")) {
            LivingEntity mob = e.getEntity();
            Player player = mob.getKiller();
            String vanillamobs = e.getEntityType().toString();
            if (player == null) {
                return;
            }
            if (manager.isPluginEnabled("MythicMobsHook")) {
                BukkitAPIHelper mythicMobsAPI = MythicMobs.inst().getAPIHelper();
                if (mythicMobsAPI.isMythicMob(mob)) {
                    return;
                }
            }
            Random randomInt = new Random();
            int max = main.getmob().getInt("Vanilla.Default.max");
            int min = main.getmob().getInt("Vanilla.Default.min");
            int chance = main.getmob().getInt("Vanilla.Default.chance");
            for (String getEntityType : main.getmob().getConfigurationSection("Vanilla.").getKeys(false)) {
                if (vanillamobs.equalsIgnoreCase(getEntityType)) {
                    max = main.getmob().getInt("Vanilla." + vanillamobs + ".max");
                    min = main.getmob().getInt("Vanilla." + vanillamobs + ".min");
                    chance = main.getmob().getInt("Vanilla." + vanillamobs + ".chance");
                    break;
                }
            }
            max = max - min;
            int random = min + randomInt.nextInt(max);
            double chancee = Math.random() * 100.0D;
            if (chancee <= chance) {
                main.addLives(player, random);
                player.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Kill-mobs-message")).replace("%souls%", Integer.toString(random)).replace("%mob%", mob.getName()));
            }
        }
    }


    @EventHandler
    public void death(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Player k = p.getKiller();
        List<String> w = main.getConfig().getStringList("available-worlds");
        if (w.contains(p.getWorld().getName())) {
            if (p.hasPermission("souls.use")) {
                main.removeLives(p, main.getConfig().getInt("General.Death-souls"));
                if (!main.getConfig().getBoolean("PVP.Enable")) {
                    return;
                }
                if (main.getConfig().getBoolean("PVP.Enable")) {
                    if (k instanceof Player) {
                        main.addLives(k, main.getConfig().getInt("PVP.Kill-souls"));
                        k.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Kill-message")).replace("%souls%", main.getConfig().getString("PVP.Kill-souls")).replace("%player%", p.getDisplayName()));
                    }
                }
                if (main.getLives(p) >= 1) {
                    (new BukkitRunnable() {
                        public void run() {
                            if (p.isOnline() && p != null) {
                                p.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Death-message")).replace("%souls%", String.valueOf(main.getLives(p))).replace("%lost%", main.getConfig().getString("General.Death-souls")));
                            }

                        }
                    }).runTaskLater(main, (long) (20 * main.getConfig().getInt("General.Lose-soul-message-after")));
                }

                if (main.getLives(p) > main.getConfig().getInt("General.Minimum_souls")) {
                    e.setKeepInventory(true);
                }

                if (main.getLives(p) < main.getConfig().getInt("General.Minimum_souls")) {
                    main.addLives(p, main.getConfig().getInt("General.Respawn_souls"));
                    if (main.getConfig().getBoolean("DropRandomItems")) {
                        List<Integer> fullSlots = new ArrayList<Integer>();
                        PlayerInventory playerInventory = p.getInventory();
                        for (int i = 0; i <= playerInventory.getSize(); i++) {
                            if (playerInventory.getItem(i) != null)
                                fullSlots.add(Integer.valueOf(i));
                        }
                        if (fullSlots.size() == 0)
                            return;
                        int theSlot = getRandomNumber(0, fullSlots.size());
                        ItemStack itemStack = new ItemStack(playerInventory.getItem(((Integer) fullSlots.get(theSlot)).intValue()));
                        playerInventory.setItem(((Integer) fullSlots.get(theSlot)).intValue(), null);
                    }
                    if (!main.getConfig().getBoolean("DropRandomItems")) {
                        p.getInventory().clear();
                    }
                    (new BukkitRunnable() {
                        public void run() {
                            if (p.isOnline() && p != null) {
                                p.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Death-message-inventory")).replaceAll("%souls%", String.valueOf(main.getLives(p))));
                            }

                        }
                    }).runTaskLater(main, (long) (20 * main.getConfig().getInt("General.Lose-soul-message-after")));
                }

            }
        }
    }


}
