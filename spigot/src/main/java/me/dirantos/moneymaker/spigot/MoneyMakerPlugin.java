package me.dirantos.moneymaker.spigot;

import me.dirantos.moneymaker.api.fetchers.AccountFetcher;
import me.dirantos.moneymaker.api.fetchers.BankFetcher;
import me.dirantos.moneymaker.api.fetchers.TransactionFetcher;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.models.Transaction;
import me.dirantos.moneymaker.api.models.Transfer;
import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.api.service.MoneyMakerAPIService;
import me.dirantos.moneymaker.api.transaction.TransactionManager;
import me.dirantos.moneymaker.spigot.chat.ChatMessanger;
import me.dirantos.moneymaker.spigot.command.CmdTest;
import me.dirantos.moneymaker.spigot.command.CommandManager;
import me.dirantos.moneymaker.spigot.fetchers.AccountFetcherImpl;
import me.dirantos.moneymaker.spigot.fetchers.BankFetcherImpl;
import me.dirantos.moneymaker.spigot.fetchers.TransactionFetcherImpl;
import me.dirantos.moneymaker.spigot.models.AccountImpl;
import me.dirantos.moneymaker.spigot.mysql.MySQLConnectionPool;
import me.dirantos.moneymaker.spigot.transaction.TransactionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public final class MoneyMakerPlugin extends JavaPlugin {

    private ChatMessanger chatMessanger;

    @Override
    public void onEnable() {

        MySQLConnectionPool mySQL = new MySQLConnectionPool("localhost", 3306, "rewi_moneymaker", "root", "", 10);
        BankFetcher bankFetcher = new BankFetcherImpl(mySQL, this);
        AccountFetcher accountFetcher = new AccountFetcherImpl(mySQL, this);
        TransactionFetcher transactionFetcher = new TransactionFetcherImpl(mySQL, this);

        TransactionManagerImpl transactionManager = new TransactionManagerImpl(transactionFetcher, accountFetcher);

        MoneyMakerService service = new MoneyMakerService(accountFetcher, bankFetcher, transactionFetcher, transactionManager);
        Bukkit.getServicesManager().register(MoneyMakerAPIService.class, service, this, ServicePriority.Normal);

        chatMessanger = new ChatMessanger("&3[&bMoneyMaker&3] ");

        CommandManager commandManager = new CommandManager(this);
        commandManager.registerCommand(new CmdTest());

        tests();

    }

    // just some tests
    public void tests() {
        AccountFetcher accountFetcher = MoneyMakerAPI.getService().getAccountFetcher();
        TransactionManager transactionManager = MoneyMakerAPI.getService().getTransactionManager();

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {

            Account account = accountFetcher.saveData(new AccountImpl(-1, UUID.randomUUID(), 100, new ArrayList<>()));
            Account second = accountFetcher.saveData(new AccountImpl(-1, UUID.randomUUID(), 500, new ArrayList<>()));
            log("created: " + account);
            log("created: " + second);


            Transaction withdrawal = transactionManager.makeWithdrawal(account, 30.52);
            log("made withdrawal");
            log("transaction: " + withdrawal);
            log("account: " + account);


            Transaction deposit = transactionManager.makeDeposit(account, 10);
            log("made deposit");
            log("transaction: " + deposit);
            log("account: " + account);

            Transaction withdrawal2 = transactionManager.makeWithdrawal(account, 300);
            log("made withdrawal");
            log("transaction: " + withdrawal2);
            log("account: " + account);

            Transfer transfer = transactionManager.makeTransfer(account, second, 50);
            log("made transfer");
            log("transaction: " + transfer);
            log("account: " + account);
            log("second: " + second);

            Account fetched = accountFetcher.fetchData(account.getAccountNumber());
            log("fetched account: " + fetched);
        });
    }

    public ChatMessanger getChatMessanger() {
        return chatMessanger;
    }

    public void log(String message) {
        getLogger().info(message);
    }


}
