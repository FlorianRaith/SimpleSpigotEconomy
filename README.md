# MoneyMaker

A testplugin for the rewinside-server by Dirantos for _Spigot 1.8_  
The plugin provides an API with which you can handle several operations like
creating accounts or making transactions

### Include the API

The best way to include the API is with maven.  
After you installed the repository on your local computer with `mvn install` you can add the following to your _pom.xml_:

    <dependency>
        <groupId>me.dirantos</groupId>
        <artifactId>MoneyMakerAPI</artifactId>
        <version>1.0-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>
    
Next you have to depend the Plugin in your plugin.yml:

    ...
    depend: [MoneyMaker]
    
### Using the API

To get an instance of the API you have to call `MoneyMakerAPI.getService()`

    public class MMTestPlugin extends JavaPlugin {
      
        @Override
        public void onEnable() {
            MoneyMakerAPIService apiService = MoneyMakerAPI.getService();
        }
        
    }
    
From the ApiService you can get several Managers with which you can handle different operations
        
    @Override
    public void onEnable() {
        
        MoneyMakerAPIService apiService = MoneyMakerAPI.getService();
        
        BankManager bankManager = apiService.getBankManager();
        TransactionManager transactionManager = apiService.getTransactionManager();
        AccountManager accountManager = apiService.getAccountManager();
        
    }
    
Every method in one of the managers-class should be called asynchronously.  
In the following example an account will be created and a deposit-transaction will be made to the account:

    AccountManager accountManager = MoneyMakerAPI.getService().getAccountManager();
    TransactionManager transactionManager = MoneyMakerAPI.getService().getTransactionManager();
      
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