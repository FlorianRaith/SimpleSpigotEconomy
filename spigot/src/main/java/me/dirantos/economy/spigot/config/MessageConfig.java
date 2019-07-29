package me.dirantos.economy.spigot.config;

import me.dirantos.economy.spigot.config.AbstractConfig;
import me.dirantos.economy.spigot.config.ConfigFile;

public class MessageConfig extends AbstractConfig {

    private final String prefix;

    public MessageConfig(ConfigFile config) {
        super(config);
        prefix = getString("messages.prefix", "&3[&Economy&3] ");
    }

    public String getPrefix() {
        return prefix;
    }
}
