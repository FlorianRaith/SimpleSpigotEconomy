# Economy

A simple economy plugin for _Spigot 1.8_
The plugin provides an API with which you can handle several operations like
creating accounts or making transactions

### Include the API

The best way to include the API is with maven.  
After you installed the repository on your local computer with `mvn install` you can add the following to your _pom.xml_:

    <dependency>
        <groupId>me.dirantos</groupId>
        <artifactId>EconomyAPI</artifactId>
        <version>1.0-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>
    
Next you have to depend the Plugin in your plugin.yml:

    ...
    depend: [Economy]
    
### Using the API

To get an instance of the API you have to call `Bukkit.getServicesManager().load(EconomyService.class)`

    public class EconomyTestPlugin extends JavaPlugin {
      
        @Override
        public void onEnable() {
            EconomyService economyService = Bukkit.getServicesManager().load(EconomyService.class);
        }
        
    }
    
From the ApiService you can get several Managers with which you can handle different operations
        
    @Override
    public void onEnable() {
        
        EconomyService economyService = Bukkit.getServicesManager().load(EconomyService.class);
        
        BankManager bankManager = economyService.getBankManager();
        TransactionManager transactionManager = economyService.getTransactionManager();
        AccountManager accountManager = economyService.getAccountManager();
        
    }
    
Every method in one of the managers-class should be called asynchronously.  
In the following example an account will be created and a deposit-transaction will be made to the account:

    AccountManager accountManager = economyService.getAccountManager();
    TransactionManager transactionManager = economyService.getTransactionManager();
      
    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
        
        Account account = accountManager.createNewAccount(player.getUniqueId());
        Transaction transaction = transactionManager.makeDeposit(account, 50.00); 
      
    });
    
There are also some Events you can listen to.  
In the following example every transaction will get logged  

    @EventHandler
    public void onTransaction(AsyncTransactionEvent event) {
        
        Transaction transaction = event.getTransaction();
        getLogger().info("transaction: " + transaction.toString());
        
    }