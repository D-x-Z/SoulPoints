package net.danh.soulpoints.Hook;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import net.danh.soulpoints.Manager.Data;
import net.danh.soulpoints.Manager.Files;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;
import java.util.Random;

public class MythicMobs implements Listener {


    @EventHandler
    public void onkillmm(MythicMobDeathEvent mme) {
        if (Files.getConfig().getBoolean("Mobs.Enable")) {
            Player p = (Player) mme.getKiller();
            String mobname = mme.getMobType().getInternalName();

            if (p == null) {
                return;
            }
            int max = Files.getmob().getInt("MythicMobs.Default.max");
            int min = Files.getmob().getInt("MythicMobs.Default.min");
            int chance = Files.getmob().getInt("MythicMobs.Default.chance");
            if (max == 0) {
                if (min == 0) {
                    if (chance == 0) {
                        return;
                    }
                }
            }
            Random randomInt = new Random();
            for (String getstring : Objects.requireNonNull(Files.getmob().getConfigurationSection("MythicMobs.")).getKeys(false)) {
                if (mobname.equalsIgnoreCase(getstring)) {
                    max = Files.getmob().getInt("MythicMobs." + mobname + ".max");
                    min = Files.getmob().getInt("MythicMobs." + mobname + ".min");
                    chance = Files.getmob().getInt("MythicMobs." + mobname + ".chance");
                    break;
                }
            }
            max = max - min;
            int random = min + randomInt.nextInt(max);
            double chancee = Math.random() * 100.0D;
            if (chancee <= chance) {
                Data.addLives(p, random);
                p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Objects.requireNonNull(Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Kill-mobs-message")).replace("%souls%", Integer.toString(random)).replace("%mob%", mme.getEntity().getName())));
            }
        }
    }
}
