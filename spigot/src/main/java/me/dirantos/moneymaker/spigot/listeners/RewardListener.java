package me.dirantos.moneymaker.spigot.listeners;

import me.dirantos.moneymaker.api.managers.BankManager;
import me.dirantos.moneymaker.api.models.Bank;
import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import me.dirantos.moneymaker.spigot.chat.ChatLevel;
import me.dirantos.moneymaker.spigot.chat.ChatMessanger;
import me.dirantos.moneymaker.spigot.configs.RewardConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.*;

public class RewardListener implements Listener {

    private static final Set<Material> meat = new HashSet<>(Arrays.asList(
            Material.PORK, Material.GRILLED_PORK, Material.COOKED_BEEF, Material.RAW_BEEF,
            Material.MUTTON, Material.COOKED_MUTTON, Material.RAW_CHICKEN, Material.COOKED_CHICKEN,
            Material.COOKED_RABBIT, Material.RABBIT
    ));

    private static final Map<EntityType, List<Material>> animalFood = new HashMap<>();

    // list from https://minecraft.gamepedia.com/Breeding
    static {
        animalFood.put(EntityType.HORSE, a(Material.APPLE, Material.WHEAT, Material.GOLDEN_APPLE, Material.GOLDEN_CARROT, Material.SUGAR, Material.HAY_BLOCK));
        animalFood.put(EntityType.SHEEP, a(Material.WHEAT));
        animalFood.put(EntityType.COW, a(Material.WHEAT));
        animalFood.put(EntityType.MUSHROOM_COW, a(Material.WHEAT));
        animalFood.put(EntityType.PIG, a(Material.CARROT_ITEM, Material.POTATO_ITEM));
        animalFood.put(EntityType.CHICKEN, a(Material.SEEDS, Material.PUMPKIN_SEEDS, Material.MELON_SEEDS));
        animalFood.put(EntityType.WOLF, a(Material.PORK, Material.GRILLED_PORK, Material.COOKED_BEEF, Material.RAW_BEEF, Material.MUTTON, Material.COOKED_MUTTON, Material.RAW_CHICKEN, Material.COOKED_CHICKEN, Material.COOKED_RABBIT, Material.RABBIT, Material.ROTTEN_FLESH, Material.COOKED_FISH, Material.RAW_FISH));
        animalFood.put(EntityType.OCELOT, a(Material.RAW_FISH));
        animalFood.put(EntityType.RABBIT, a(Material.YELLOW_FLOWER, Material.CARROT_ITEM, Material.GOLDEN_CARROT));
    }

    private final MoneyMakerPlugin plugin;
    private final RewardConfig rewardConfig;
    private final ChatMessanger messanger;

    public RewardListener(MoneyMakerPlugin plugin, RewardConfig rewardConfig) {
        this.plugin = plugin;
        this.rewardConfig = rewardConfig;
        this.messanger = plugin.getChatMessanger();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onAnimalFeed(PlayerInteractAtEntityEvent event) {

        if(!(animalFood.keySet().contains(event.getRightClicked().getType()))) return;
        if(event.getPlayer().getItemInHand() == null) return;
        if(!animalFood.get(event.getRightClicked().getType()).contains(event.getPlayer().getItemInHand().getType())) return;

        BankManager bankManager = MoneyMakerAPI.getService().getBankManager();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            Bank bank = bankManager.loadBank(event.getPlayer());
            double newMoney = bank.getMoney() + rewardConfig.getRewardFeedingAnimals();
            if(newMoney < 0) newMoney = 0;
            bankManager.setBalance(bank, newMoney);

            String s = rewardConfig.getRewardFeedingAnimals() < 0 ? "abgezogen" : "gutgeschrieben";
            ChatLevel level = rewardConfig.getRewardFeedingAnimals() < 0 ? ChatLevel.ERROR : ChatLevel.SUCCESS;
            messanger.send(event.getPlayer(), "Du hast [[" + Math.abs(rewardConfig.getRewardFeedingAnimals()) + "$]] " + s + " bekommen", level);
        });
    }

    @EventHandler
    public void onMonsterKill(EntityDeathEvent event) {

        if(event.getEntity().getKiller() == null) return;
        if(!(event.getEntity() instanceof Monster)) return;

        BankManager bankManager = MoneyMakerAPI.getService().getBankManager();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            Bank bank = bankManager.loadBank(event.getEntity().getKiller());
            double newMoney = bank.getMoney() + rewardConfig.getRewardKillingMonsers();
            if(newMoney < 0) newMoney = 0;
            bankManager.setBalance(bank, newMoney);

            String s = rewardConfig.getRewardKillingMonsers() < 0 ? "abgezogen" : "gutgeschrieben";
            ChatLevel level = rewardConfig.getRewardKillingMonsers() < 0 ? ChatLevel.ERROR : ChatLevel.SUCCESS;
            messanger.send(event.getEntity().getKiller(), "Du hast [[" + Math.abs(rewardConfig.getRewardKillingMonsers()) + "$]] " + s + " bekommen", level);
        });

    }

    @EventHandler
    public void onAnimalKill(EntityDeathEvent event) {

        if(event.getEntity().getKiller() == null) return;
        if(!(event.getEntity() instanceof Animals)) return;

        BankManager bankManager = MoneyMakerAPI.getService().getBankManager();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            Bank bank = bankManager.loadBank(event.getEntity().getKiller());
            double newMoney = bank.getMoney() + rewardConfig.getRewardKillingAnimals();
            if(newMoney < 0) newMoney = 0;
            bankManager.setBalance(bank, newMoney);

            String s = rewardConfig.getRewardKillingAnimals() < 0 ? "abgezogen" : "gutgeschrieben";
            ChatLevel level = rewardConfig.getRewardKillingAnimals() < 0 ? ChatLevel.ERROR : ChatLevel.SUCCESS;
            messanger.send(event.getEntity().getKiller(), "Du hast [[" + Math.abs(rewardConfig.getRewardKillingAnimals()) + "$]] " + s + " bekommen", level);
        });

    }

    @EventHandler
    public void onConsumeMeat(PlayerItemConsumeEvent event) {

        if(!event.getItem().getType().isEdible()) return;
        if(!meat.contains(event.getItem().getType())) return;

        BankManager bankManager = MoneyMakerAPI.getService().getBankManager();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            Bank bank = bankManager.loadBank(event.getPlayer());
            double newMoney = bank.getMoney() + rewardConfig.getRewardEatingMeat();
            if(newMoney < 0) newMoney = 0;
            bankManager.setBalance(bank, newMoney);

            String s = rewardConfig.getRewardEatingMeat() < 0 ? "abgezogen" : "gutgeschrieben";
            ChatLevel level = rewardConfig.getRewardEatingMeat() < 0 ? ChatLevel.ERROR : ChatLevel.SUCCESS;
            messanger.send(event.getPlayer(), "Du hast [[" + Math.abs(rewardConfig.getRewardEatingMeat()) + "$]] " + s + " bekommen", level);
        });

    }

    private static <T> List<T> a(T... a) {
        return Arrays.asList(a);
    }

}
