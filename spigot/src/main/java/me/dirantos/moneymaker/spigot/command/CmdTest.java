package me.dirantos.moneymaker.spigot.command;

import org.bukkit.command.CommandSender;

public final class CmdTest extends AbstractCommand {

    public CmdTest() {
        super("test");
    }

    @Override
    protected void handle(CommandSender sender, String[] args) {
        getMessanger().send(sender, "Du hast erfolgreich den __/test__ Befehl ausgeführt!");
    }

}
