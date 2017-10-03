package me.dirantos.moneymaker.spigot.configs;

import me.dirantos.moneymaker.components.config.AbstractConfig;
import me.dirantos.moneymaker.components.config.ConfigFile;

public class MessageConfig extends AbstractConfig {

    private final String prefix;

    public MessageConfig(ConfigFile config) {
        super(config);
        prefix = getString("messages.prefix", "&3[&bMoneyMaker&3] ");
    }

    public String getPrefix() {
        return prefix;
    }
}
