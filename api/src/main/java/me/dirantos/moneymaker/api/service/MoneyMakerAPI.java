package me.dirantos.moneymaker.api.service;

import org.bukkit.Bukkit;

public final class MoneyMakerAPI {

    private static final MoneyMakerAPIService SERVICE;

    private MoneyMakerAPI() {}

    static {
        if(Bukkit.getServicesManager().isProvidedFor(MoneyMakerAPIService.class)) {
            SERVICE = Bukkit.getServicesManager().load(MoneyMakerAPIService.class);
        } else {
            SERVICE = null;
        }
    }

    public static MoneyMakerAPIService getService() {
        if(SERVICE == null) throw new IllegalAccessError("MoneyMakerAPI is not loaded!");
        return SERVICE;
    }

}
