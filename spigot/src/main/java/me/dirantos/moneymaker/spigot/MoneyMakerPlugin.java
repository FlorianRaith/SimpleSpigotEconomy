package me.dirantos.moneymaker.spigot;

import me.dirantos.moneymaker.api.Bank;
import me.dirantos.moneymaker.api.service.MoneyMakerAPIService;
import me.dirantos.moneymaker.spigot.chat.ChatMessanger;
import me.dirantos.moneymaker.spigot.command.CmdTest;
import me.dirantos.moneymaker.spigot.command.CommandManager;
import me.dirantos.moneymaker.spigot.fetchers.AccountFetcher;
import me.dirantos.moneymaker.spigot.fetchers.BankFetcher;
import me.dirantos.moneymaker.spigot.fetchers.TransactionFetcher;
import me.dirantos.moneymaker.spigot.impl.BankImpl;
import me.dirantos.moneymaker.spigot.mysql.MySQLConnectionPool;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class MoneyMakerPlugin extends JavaPlugin {

    private ChatMessanger chatMessanger;
    private BankFetcher bankFetcher;
    private AccountFetcher accountFetcher;
    private TransactionFetcher transactionFetcher;

    @Override
    public void onEnable() {

        MoneyMakerService service = new MoneyMakerService(this);
        Bukkit.getServicesManager().register(MoneyMakerAPIService.class, service, this, ServicePriority.Normal);

        chatMessanger = new ChatMessanger("&3[&bMoneyMaker&3] ");

        CommandManager commandManager = new CommandManager(this);
        commandManager.registerCommand(new CmdTest());

        MySQLConnectionPool mySQL = new MySQLConnectionPool("localhost", 3306, "rewi_moneymaker", "root", "", 10);
        bankFetcher = new BankFetcher(mySQL, this);
        accountFetcher = new AccountFetcher(mySQL, this);
        transactionFetcher = new TransactionFetcher(mySQL, this);

        // create tables if they don't exsists
        bankFetcher.createTableIfNotExistsAsync();
        accountFetcher.createTableIfNotExistsAsync();
        transactionFetcher.createTableIfNotExistsAsync();

        // just some tests...
        Set<Bank> bankSet = new HashSet<>();
        bankSet.add(new BankImpl(UUID.randomUUID(), 103.95));
        bankSet.add(new BankImpl(UUID.randomUUID(), 70.50));
        bankSet.add(new BankImpl(UUID.randomUUID(), 43.37));
        bankSet.add(new BankImpl(UUID.randomUUID(), 1343.05));

        bankFetcher.saveMultipleDataAsync(bankSet);

        Set<UUID> ids = new HashSet<>();
        ids.add(UUID.fromString("5686f58e-83f0-4c38-96a9-4191438cf20c"));
        ids.add(UUID.fromString("004bbf56-019c-482f-affb-4aa19cdb3a73"));

        bankFetcher.fetchMultipleDataAsync(ids, dataSet -> {
            log("fetched banks: ");
            for (Bank bank : dataSet) {
                log(bank.toString());
            }
        });

        log("end...");

    }

    public ChatMessanger getChatMessanger() {
        return chatMessanger;
    }

    public void log(String message) {
        getLogger().info(message);
    }


}
