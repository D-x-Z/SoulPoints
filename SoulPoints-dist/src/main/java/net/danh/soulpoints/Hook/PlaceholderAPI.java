package net.danh.soulpoints.Hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.danh.soulpoints.Manager.Data;
import net.danh.soulpoints.SoulPoints;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPI extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() {
        return SoulPoints.getInstance().getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "sp";
    }

    @Override
    public @NotNull String getVersion() {
        return SoulPoints.getInstance().getDescription().getVersion();
    }


    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onPlaceholderRequest(Player p, @NotNull String identifier) {
        if (p == null)
            return "Player not online";

        if (identifier.equalsIgnoreCase("live")) {
            return String.valueOf(Data.getLives(p));
        }

        return null;
    }
}