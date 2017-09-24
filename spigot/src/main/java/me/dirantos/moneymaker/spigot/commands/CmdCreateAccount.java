package me.dirantos.moneymaker.spigot.commands;


import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.api.service.MoneyMakerAPIService;
import me.dirantos.moneymaker.spigot.chat.ChatLevel;
import me.dirantos.moneymaker.spigot.command.CommandInfo;
import me.dirantos.moneymaker.spigot.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "create", permission = "moneymaker.cmd.account.create", usage = "create", description = {"creates a new account", "__player only!__"})
public class CmdCreateAccount extends SubCommand {

    @Override
    protected void handle(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            getMessanger().send(sender, "This command is only for players!", ChatLevel.ERROR);
            return;
        }

        MoneyMakerAPIService service = MoneyMakerAPI.getService();
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Account account = service.getAccountManager().createNewAccount(((Player) sender).getUniqueId());
            getMessanger().send(sender, "Your account was successfully created.", ChatLevel.SUCCESS);
            getMessanger().send(sender, "The account-number is __" + account.getAccountNumber() + "__.", ChatLevel.SUCCESS);
        });

    }

}
