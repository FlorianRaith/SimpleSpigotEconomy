package me.dirantos.moneymaker.spigot;

import me.dirantos.moneymaker.api.cache.ModelCache;
import me.dirantos.moneymaker.api.fetchers.AccountFetcher;
import me.dirantos.moneymaker.api.fetchers.BankFetcher;
import me.dirantos.moneymaker.api.fetchers.TransactionFetcher;
import me.dirantos.moneymaker.api.managers.AccountManager;
import me.dirantos.moneymaker.api.managers.BankManager;
import me.dirantos.moneymaker.api.managers.TransactionManager;
import me.dirantos.moneymaker.api.service.MoneyMakerAPIService;
import me.dirantos.moneymaker.spigot.chat.ChatMessanger;
import me.dirantos.moneymaker.spigot.command.Command;
import me.dirantos.moneymaker.spigot.commands.CmdCreateAccount;
import me.dirantos.moneymaker.spigot.commands.CmdDeleteAccount;
import me.dirantos.moneymaker.spigot.commands.CmdShowAccountBalance;
import me.dirantos.moneymaker.spigot.commands.CmdTransfer;
import me.dirantos.moneymaker.spigot.config.ConfigFile;
import me.dirantos.moneymaker.spigot.configs.MessageConfig;
import me.dirantos.moneymaker.spigot.configs.MysqlConnectionConfig;
import me.dirantos.moneymaker.spigot.configs.RewardConfig;
import me.dirantos.moneymaker.spigot.fetchers.AccountFetcherImpl;
import me.dirantos.moneymaker.spigot.fetchers.BankFetcherImpl;
import me.dirantos.moneymaker.spigot.fetchers.TransactionFetcherImpl;
import me.dirantos.moneymaker.spigot.managers.AccountManagerImpl;
import me.dirantos.moneymaker.spigot.managers.BankManagerImpl;
import me.dirantos.moneymaker.spigot.managers.TransactionManagerImpl;
import me.dirantos.moneymaker.spigot.mysql.MySQLConnectionPool;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class MoneyMakerPlugin extends JavaPlugin {

    private ChatMessanger chatMessanger;

    @Override
    public void onEnable() {

        ConfigFile config = new ConfigFile(this, "config.yml");

        MysqlConnectionConfig connectionConfig = new MysqlConnectionConfig(config);
        MessageConfig messageConfig = new MessageConfig(config);
        RewardConfig rewardConfig = new RewardConfig(config);

        MySQLConnectionPool mySQL = new MySQLConnectionPool(connectionConfig);

        ModelCache cache = new ModelCache();

        BankFetcher bankFetcher = new BankFetcherImpl(mySQL, this, cache);
        AccountFetcher accountFetcher = new AccountFetcherImpl(mySQL, this, cache);
        TransactionFetcher transactionFetcher = new TransactionFetcherImpl(mySQL, this, cache);


        TransactionManager transactionManager = new TransactionManagerImpl(transactionFetcher, accountFetcher, cache);
        AccountManager accountManager = new AccountManagerImpl(accountFetcher, transactionManager, cache);
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

        chatMessanger = new ChatMessanger(messageConfig.getPrefix());

        Command accountCommand = new Command("account", this);
        accountCommand.addSubCommand(new CmdCreateAccount());
        accountCommand.addSubCommand(new CmdDeleteAccount());
        accountCommand.addSubCommand(new CmdShowAccountBalance());
        accountCommand.addSubCommand(new CmdTransfer());
        accountCommand.register();

        Command bankCommand = new Command("bank", this);
        bankCommand.register();

        Set<UUID> uuids = new HashSet<>();
        Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).forEach(uuids::add);
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {

            transactionFetcher.createTableIfNotExists();
            accountFetcher.createTableIfNotExists();
            transactionFetcher.createTableIfNotExists();

            for (UUID uuid : uuids) {
                bankManager.loadBank(uuid);
            }
        });

    }

    public ChatMessanger getChatMessanger() {
        return chatMessanger;
    }

    public void log(String message) {
        getLogger().info(message);
    }


}
