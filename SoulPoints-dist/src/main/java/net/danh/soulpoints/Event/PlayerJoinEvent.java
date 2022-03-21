package net.danh.soulpoints.Event;

import net.danh.soulpoints.Manager.UpdateChecker;
import net.danh.soulpoints.SoulPoints;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinEvent implements Listener {

    @EventHandler
    public void onjoin(org.bukkit.event.player.@NotNull PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.isOp() || p.hasPermission("souls.admin")) {
            new UpdateChecker(SoulPoints.getInstance(), 96396).getVersion(version -> {
                if (!SoulPoints.getInstance().getDescription().getVersion().equals(version)) {
                    p.sendMessage(ChatColor.GREEN + "There's a new update! The new version is " + ChatColor.YELLOW + version + ChatColor.GREEN + ", you are using " + ChatColor.RED + SoulPoints.getInstance().getDescription().getVersion());
                    p.sendMessage(ChatColor.BLUE + "Download: " + ChatColor.WHITE + "https://www.spigotmc.org/resources/96396/");
                }

            });
        }
    }
}
