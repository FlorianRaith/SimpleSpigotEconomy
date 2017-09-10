package me.dirantos.moneymaker.spigot;

import me.dirantos.moneymaker.api.service.MoneyMakerAPIService;

public final class MoneyMakerService implements MoneyMakerAPIService {

    private final MoneyMakerPlugin plugin;

    public MoneyMakerService(MoneyMakerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void test() {
        plugin.log("MoneyMakerServiceTest!");
    }

}
