package me.dirantos.economy.spigot.configs;

import me.dirantos.economy.components.config.AbstractConfig;
import me.dirantos.economy.components.config.ConfigFile;

public class InterestConfig extends AbstractConfig {

    private final double interestRate;

    public InterestConfig(ConfigFile config) {
        super(config);
        interestRate = getDouble("interest.interestRate", 0.05); // default 5%
    }

    public double getInterestRate() {
        return interestRate;
    }

}
