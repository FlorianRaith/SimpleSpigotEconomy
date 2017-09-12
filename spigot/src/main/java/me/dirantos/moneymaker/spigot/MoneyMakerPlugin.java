package me.dirantos.moneymaker.spigot;

import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.api.service.MoneyMakerAPIService;
import me.dirantos.moneymaker.spigot.chat.ChatMessanger;
import me.dirantos.moneymaker.spigot.command.CmdTest;
import me.dirantos.moneymaker.spigot.command.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class MoneyMakerPlugin extends JavaPlugin {

    private ChatMessanger chatMessanger;

    @Override
    public void onEnable() {

        MoneyMakerService service = new MoneyMakerService(this);
        Bukkit.getServicesManager().register(MoneyMakerAPIService.class, service, this, ServicePriority.Normal);

        chatMessanger = new ChatMessanger("&3[&bMoneyMaker&3] ");

        CommandManager commandManager = new CommandManager(this);
        commandManager.registerCommand(new CmdTest());

    }

    public ChatMessanger getChatMessanger() {
        return chatMessanger;
    }

    public void log(String message) {
        getLogger().info(message);
    }


}
