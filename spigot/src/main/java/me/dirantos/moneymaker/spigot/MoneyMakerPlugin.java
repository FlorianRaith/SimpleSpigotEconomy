package me.dirantos.moneymaker.spigot;

import me.dirantos.moneymaker.api.fetchers.AccountFetcher;
import me.dirantos.moneymaker.api.fetchers.BankFetcher;
import me.dirantos.moneymaker.api.fetchers.TransactionFetcher;
import me.dirantos.moneymaker.api.service.MoneyMakerAPIService;
import me.dirantos.moneymaker.spigot.chat.ChatMessanger;
import me.dirantos.moneymaker.spigot.command.CmdTest;
import me.dirantos.moneymaker.spigot.command.CommandManager;
import me.dirantos.moneymaker.spigot.fetchers.AccountFetcherImpl;
import me.dirantos.moneymaker.spigot.fetchers.BankFetcherImpl;
import me.dirantos.moneymaker.spigot.fetchers.TransactionFetcherImpl;
import me.dirantos.moneymaker.spigot.mysql.MySQLConnectionPool;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class MoneyMakerPlugin extends JavaPlugin {

    private ChatMessanger chatMessanger;

    @Override
    public void onEnable() {

        MySQLConnectionPool mySQL = new MySQLConnectionPool("localhost", 3306, "rewi_moneymaker", "root", "", 10);
        BankFetcher bankFetcher = new BankFetcherImpl(mySQL, this);
        AccountFetcher accountFetcher = new AccountFetcherImpl(mySQL, this);
        TransactionFetcher transactionFetcher = new TransactionFetcherImpl(mySQL, this);

        MoneyMakerService service = new MoneyMakerService(accountFetcher, bankFetcher, transactionFetcher);
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
