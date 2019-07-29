package me.dirantos.economy.spigot;

import me.dirantos.economy.api.account.AccountFetcher;
import me.dirantos.economy.api.bank.BankFetcher;
import me.dirantos.economy.api.transaction.TransactionFetcher;
import me.dirantos.economy.api.account.AccountManager;
import me.dirantos.economy.api.bank.BankManager;
import me.dirantos.economy.api.transaction.TransactionManager;
import me.dirantos.economy.api.EconomyService;
import me.dirantos.economy.spigot.chat.ChatMessenger;
import me.dirantos.economy.spigot.command.*;
import me.dirantos.economy.spigot.bank.inventory.BankOpenListener;
import me.dirantos.economy.spigot.config.ConfigFile;
import me.dirantos.economy.spigot.config.InterestConfig;
import me.dirantos.economy.spigot.config.MessageConfig;
import me.dirantos.economy.spigot.config.MysqlConnectionConfig;
import me.dirantos.economy.spigot.config.RewardConfig;
import me.dirantos.economy.spigot.account.AccountFetcherImpl;
import me.dirantos.economy.spigot.bank.BankFetcherImpl;
import me.dirantos.economy.spigot.transaction.TransactionFetcherImpl;
import me.dirantos.economy.spigot.bank.BankUpdateListener;
import me.dirantos.economy.spigot.account.AccountManagerImpl;
import me.dirantos.economy.spigot.bank.BankManagerImpl;
import me.dirantos.economy.spigot.transaction.TransactionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class EconomyPlugin extends JavaPlugin {

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

        EconomyServiceImpl service = new EconomyServiceImpl(
                accountFetcher,
                bankFetcher,
                transactionFetcher,
                transactionManager,
                accountManager,
                bankManager
        );

        saveConfig();

        Bukkit.getServicesManager().register(EconomyService.class, service, this, ServicePriority.Normal);

        chatMessenger = new ChatMessenger(messageConfig.getPrefix());

        Command accountCommand = new Command("account", this);
        accountCommand.addSubCommand(new CreateAccountCommand(this, service));
        accountCommand.addSubCommand(new DeleteAccountCommand(this, service));
        accountCommand.addSubCommand(new ShowAccountBalanceCommand(this, service));
        accountCommand.addSubCommand(new TransferAccountCommand(this, service));
        accountCommand.addSubCommand(new DepositAccountCommand(this, service));
        accountCommand.addSubCommand(new WithdrawalAccountCommand(this, service));
        accountCommand.register();

        Command bankCommand = new Command("bank", this);
        bankCommand.addSubCommand(new BankSetBalanceCommand(this, service));
        bankCommand.addSubCommand(new BankShowBalanceCommand(this, service));
        bankCommand.addSubCommand(new BankOpenCommand(this, service));
        bankCommand.register();

        BankUpdateListener listener = new BankUpdateListener(this, bankManager);


        new RewardListener(this, rewardConfig, bankManager);
        new BlockClickListener(this, bankManager);
        new BankOpenListener(this, accountManager);

        InterestReceiver interestReceiver = new InterestReceiver(this, 20*60*5, interestConfig, bankManager, transactionManager);
        interestReceiver.start();

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            bankFetcher.createTableIfNotExists();
            accountFetcher.createTableIfNotExists();
            transactionFetcher.createTableIfNotExists();
            listener.loadBanks();
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
