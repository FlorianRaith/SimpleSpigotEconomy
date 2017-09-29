package me.dirantos.moneymaker.spigot.configs;

import me.dirantos.moneymaker.spigot.config.AbstractConfig;
import me.dirantos.moneymaker.spigot.config.ConfigFile;

public class RewardConfig extends AbstractConfig {

    private final double rewardFeedingAnimals;
    private final double rewardKillingMonsers;
    private final double rewardKillingAnimals;
    private final double rewardEatingMeat;

    public RewardConfig(ConfigFile config) {
        super(config);
        this.rewardFeedingAnimals = getDouble("reward.feedingAnimals", 10.50);
        this.rewardKillingMonsers = getDouble("reward.killingMonsters", 20.00);
        this.rewardKillingAnimals = getDouble("reward.killingAnimals", -30.00);
        this.rewardEatingMeat = getDouble("reward.eatingMeat", -10.50);
    }

    public double getRewardFeedingAnimals() {
        return rewardFeedingAnimals;
    }

    public double getRewardKillingMonsers() {
        return rewardKillingMonsers;
    }

    public double getRewardKillingAnimals() {
        return rewardKillingAnimals;
    }

    public double getRewardEatingMeat() {
        return rewardEatingMeat;
    }

}
