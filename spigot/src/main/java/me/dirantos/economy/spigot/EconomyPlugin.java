package me.dirantos.economy.spigot;

import me.dirantos.economy.api.EconomyRepository;
import me.dirantos.economy.api.EconomyService;
import me.dirantos.economy.api.bank.AsyncBankUpdateEvent;
import me.dirantos.economy.api.bank.Bank;
import me.dirantos.economy.spigot.bank.BankUpdateListener;
import me.dirantos.economy.spigot.bank.inventory.BankOpenListener;
import me.dirantos.economy.spigot.chat.ChatMessenger;
import me.dirantos.economy.spigot.command.*;
import me.dirantos.economy.spigot.config.*;
import me.dirantos.economy.spigot.repository.EconomyRepositoryImpl;
import me.dirantos.economy.spigot.repository.MySQLConnectionPool;
import me.dirantos.economy.spigot.repository.SQLQueries;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

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

        SQLQueries sqlQueries = new SQLQueries("banks", "accounts", "transactions");
        EconomyRepository repository = new EconomyRepositoryImpl(connectionPool, sqlQueries, getLogger());

        saveConfig();

        EconomyService service = new EconomyServiceImpl(repository);
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

        Bukkit.getPluginManager().registerEvents(new BankUpdateListener(this, service), this);
        Bukkit.getPluginManager().registerEvents(new RewardListener(this, rewardConfig, service), this);
        Bukkit.getPluginManager().registerEvents(new BlockClickListener(this, service), this);
        Bukkit.getPluginManager().registerEvents(new BankOpenListener(this, service), this);

        InterestReceiver interestReceiver = new InterestReceiver(this, 20*60*5, interestConfig, service);
        interestReceiver.start();


        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                repository.initialize();
            } catch (RuntimeException e) {
                getLogger().log(Level.SEVERE, "Exception during database initialization", e);
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            Bukkit.getOnlinePlayers().forEach(service::loadBank);
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

}
