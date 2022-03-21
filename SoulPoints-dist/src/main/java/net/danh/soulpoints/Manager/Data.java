package net.danh.soulpoints.Manager;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Data {

    public static int getLives(@NotNull Player p) {
        return Files.getdata().getInt("Lives." + p.getUniqueId() + ".life");
    }

    public static void setLives(@NotNull Player p, int number) {
        Files.getdata().set("Lives." + p.getUniqueId() + ".life", number);
        Files.save();
    }

    public static void addLives(@NotNull Player p, int number) {
        Files.getdata().set("Lives." + p.getUniqueId() + ".life", getLives(p) + number);
        Files.save();
    }

    public static void removeLives(@NotNull Player p, int number) {
        Files.getdata().set("Lives." + p.getUniqueId() + ".life", getLives(p) - number);
        Files.save();
    }

    public static void resetLives(@NotNull Player p) {
        Files.getdata().set("Lives." + p.getUniqueId() + ".life", Files.getConfig().getInt("General.Normal"));
        Files.save();
    }

    public static int getRandomNumber(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }
}
