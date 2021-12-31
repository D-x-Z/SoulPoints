package net.danh.diemsinhmenh.hook;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.mobs.entities.MythicEntity;
import net.danh.diemsinhmenh.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Random;

public class MythicMobsHook implements Listener {



    private Main main;


    public MythicMobsHook(Main main) {
        this.main = main;
    }


    @EventHandler
    public void onkillmm(MythicMobDeathEvent mme) {
        if (main.getConfig().getBoolean("Mobs.Enable")) {
            Player p = (Player) mme.getKiller();
            String mobname = mme.getMobType().getInternalName();

            if (p == null) {
                return;
            }
            int max = main.getmob().getInt("MythicMobs.Default.max");
            int min = main.getmob().getInt("MythicMobs.Default.min");
            int chance = main.getmob().getInt("MythicMobs.Default.chance");
            if (max == 0){
                if (min == 0){
                    if (chance == 0){
                        return;
                    }
                }
            }
            Random randomInt = new Random();
            for (String getstring : main.getmob().getConfigurationSection("MythicMobs.").getKeys(false)) {
                if (mobname.equalsIgnoreCase(getstring)) {
                    max = main.getmob().getInt("MythicMobs." + mobname + ".max");
                    min = main.getmob().getInt("MythicMobs." + mobname + ".min");
                    chance = main.getmob().getInt("MythicMobs." + mobname + ".chance");
                    break;
                }
            }
            max = max - min;
            int random = min + randomInt.nextInt(max);
            chance = chance;
            double chancee = Math.random() * 100.0D;
            if (chancee <= chance) {
                main.addLives(p, random);
                p.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Kill-mobs-message").replace("%souls%", Integer.toString(random)).replace("%mob%", mme.getEntity().getName())));
            }
        }
    }
}
