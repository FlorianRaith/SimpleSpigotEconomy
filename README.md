# Economy

A simple economy plugin for _Spigot 1.8_.
The plugin provides an API with which you can handle several operations like creating accounts or making transactions

Every five minutes all players will get 5% interest to all their accounts.
Players who kill monsters and feed animals are being rewarded, however players who kill animals and eat meat will lose money from their wallet.
A config will be auto generated when the plugin starts and there isn't already a config

### Commands

* `/bank` - displays help page for all bank commands
* `/bank open` - opens the bank inventory
* `/bank balance` - displays the current balance
* `/bank setbalance [player] [amount]` - sets the wallet balance
* `/account` - displays help page for all account commands
* `/account balance` - displays the account's balance
* `/account withdrawal [accountID] [amount]` - withdrawal from account
* `/account deposit [accountID] [amount]` - deposit to account
* `/account create` - creates a new account
* `/account delete [accountID] [amount]` - deletes a new account
* `/account transfer [from-accountID] [to-accountID] [amount]` - transfers money from one to another account

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
    

Every method in the service class (except `getRepository()`) should be called asynchronously.
In the following example an account will be created and a deposit-transaction will be made to the account:

    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

        Bank bank = economyService.loadBank(player);
        economyService.addWalletBalance(bank, 50.0);

        Account account = economyService.createNewAccount(player);

        Transaction transaction = economyService.deposit(bank, account, 50.0);
      
    });
    
There are also some Events you can listen to.  
In the following example every transaction will get logged  

    @EventHandler
    public void onTransaction(AsyncTransactionEvent event) {
        
        Transaction transaction = event.getTransaction();
        getLogger().info("transaction: " + transaction.toString());
        
    }