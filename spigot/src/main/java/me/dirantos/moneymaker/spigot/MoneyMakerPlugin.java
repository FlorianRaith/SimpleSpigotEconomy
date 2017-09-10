package me.dirantos.moneymaker.spigot;

import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.api.service.MoneyMakerAPIService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class MoneyMakerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        MoneyMakerService service = new MoneyMakerService(this);
        Bukkit.getServicesManager().register(MoneyMakerAPIService.class, service, this, ServicePriority.Normal);

        MoneyMakerAPI.getService().test();

    }

    public void log(String message) {
        getLogger().info(message);
    }


}
