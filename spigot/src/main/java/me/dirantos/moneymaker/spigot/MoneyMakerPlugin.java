package me.dirantos.moneymaker.spigot;

import me.dirantos.moneymaker.api.cache.ModelCache;
import me.dirantos.moneymaker.api.fetchers.AccountFetcher;
import me.dirantos.moneymaker.api.fetchers.BankFetcher;
import me.dirantos.moneymaker.api.fetchers.TransactionFetcher;
import me.dirantos.moneymaker.api.managers.AccountManager;
import me.dirantos.moneymaker.api.managers.BankManager;
import me.dirantos.moneymaker.api.managers.TransactionManager;
import me.dirantos.moneymaker.api.service.MoneyMakerAPIService;
import me.dirantos.moneymaker.components.chat.ChatMessenger;
import me.dirantos.moneymaker.components.command.Command;
import me.dirantos.moneymaker.spigot.bank.BankOpenListener;
import me.dirantos.moneymaker.spigot.commands.*;
import me.dirantos.moneymaker.components.config.ConfigFile;
import me.dirantos.moneymaker.spigot.configs.InterestConfig;
import me.dirantos.moneymaker.spigot.configs.MessageConfig;
import me.dirantos.moneymaker.spigot.configs.MysqlConnectionConfig;
import me.dirantos.moneymaker.spigot.configs.RewardConfig;
import me.dirantos.moneymaker.spigot.fetchers.AccountFetcherImpl;
import me.dirantos.moneymaker.spigot.fetchers.BankFetcherImpl;
import me.dirantos.moneymaker.spigot.fetchers.TransactionFetcherImpl;
import me.dirantos.moneymaker.spigot.listeners.BlockClickListener;
import me.dirantos.moneymaker.spigot.listeners.BankUpdateListener;
import me.dirantos.moneymaker.spigot.listeners.RewardListener;
import me.dirantos.moneymaker.spigot.managers.AccountManagerImpl;
import me.dirantos.moneymaker.spigot.managers.BankManagerImpl;
import me.dirantos.moneymaker.spigot.managers.TransactionManagerImpl;
import me.dirantos.moneymaker.spigot.mysql.MySQLConnectionPool;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class MoneyMakerPlugin extends JavaPlugin {

    private ChatMessenger chatMessenger;
    private MySQLConnectionPool connectionPool;

    @Override
    public void onEnable() {

        ConfigFile config = new ConfigFile(this, "config.yml");

        MysqlConnectionConfig connectionConfig = new MysqlConnectionConfig(config);
        MessageConfig messageConfig = new MessageConfig(config);
        RewardConfig rewardConfig = new RewardConfig(config);
        InterestConfig interestConfig = new InterestConfig(config);

        connectionPool = new MySQLConnectionPool(connectionConfig);

        ModelCache cache = new ModelCache();

        BankFetcher bankFetcher = new BankFetcherImpl(connectionPool, this, cache);
        AccountFetcher accountFetcher = new AccountFetcherImpl(connectionPool, this, cache);
        TransactionFetcher transactionFetcher = new TransactionFetcherImpl(connectionPool, this, cache);


        TransactionManager transactionManager = new TransactionManagerImpl(transactionFetcher, accountFetcher, bankFetcher, cache);
        AccountManager accountManager = new AccountManagerImpl(this, accountFetcher, transactionManager, bankFetcher, cache);
        BankManager bankManager = new BankManagerImpl(bankFetcher, accountManager, cache);

        MoneyMakerService service = new MoneyMakerService(
                accountFetcher,
                bankFetcher,
                transactionFetcher,
                transactionManager,
                accountManager,
                bankManager,
                cache
        );

        saveConfig();

        Bukkit.getServicesManager().register(MoneyMakerAPIService.class, service, this, ServicePriority.Normal);

        chatMessenger = new ChatMessenger(messageConfig.getPrefix());

        Command accountCommand = new Command("account", this);
        accountCommand.addSubCommand(new CmdCreateAccount());
        accountCommand.addSubCommand(new CmdDeleteAccount());
        accountCommand.addSubCommand(new CmdShowAccountBalance());
        accountCommand.addSubCommand(new CmdTransferAccount());
        accountCommand.addSubCommand(new CmdDepositAccount());
        accountCommand.addSubCommand(new CmdWithdrawalAccount());
        accountCommand.register();

        Command bankCommand = new Command("bank", this);
        bankCommand.addSubCommand(new CmdBankSetBalance());
        bankCommand.addSubCommand(new CmdBankShowBalance());
        bankCommand.addSubCommand(new CmdBankOpen());
        bankCommand.register();

        BankUpdateListener listener = new BankUpdateListener(this);
        listener.init();

        new RewardListener(this, rewardConfig);
        new BlockClickListener(this);
        new BankOpenListener(this);

        InterestReceiver interestReceiver = new InterestReceiver(this, 20*60*5, interestConfig);
        interestReceiver.start();

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            bankFetcher.createTableIfNotExists();
            accountFetcher.createTableIfNotExists();
            transactionFetcher.createTableIfNotExists();
        });

    }

    @Override
    public void onDisable() {
        try {
            connectionPool.close();
        } catch(NoClassDefFoundError e) {}
    }

    public ChatMessenger getChatMessenger() {
        return chatMessenger;
    }

    public void log(String message) {
        getLogger().info(message);
    }


}
