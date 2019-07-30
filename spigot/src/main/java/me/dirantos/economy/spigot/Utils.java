package me.dirantos.economy.spigot;

import java.util.Locale;

public final class Utils {

    private Utils() {}

    public static String formatMoney(double money) {
        return String.format(Locale.ENGLISH, "%.2f", money) + "$";
    }

}
