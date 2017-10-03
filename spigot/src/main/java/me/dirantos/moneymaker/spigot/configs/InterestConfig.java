package me.dirantos.moneymaker.spigot.configs;

import me.dirantos.moneymaker.components.config.AbstractConfig;
import me.dirantos.moneymaker.components.config.ConfigFile;

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
