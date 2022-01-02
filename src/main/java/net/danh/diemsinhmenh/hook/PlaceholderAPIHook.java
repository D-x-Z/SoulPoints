package net.danh.diemsinhmenh.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.danh.diemsinhmenh.Main;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final Main main;

    public PlaceholderAPIHook(Main main) {
        this.main = main;
    }

    @Override
    public String getAuthor() {
        return main.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "sp";
    }

    @Override
    public String getVersion() {
        return main.getDescription().getVersion();
    }


    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (p == null)
            return "Player not online";

        switch (identifier) {
            case "live":
                return String.valueOf(main.getLives(p));
        }

        return null;
    }
}