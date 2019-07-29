package me.dirantos.economy.spigot;

import me.dirantos.economy.api.bank.BankManager;
import me.dirantos.economy.api.bank.Bank;
import me.dirantos.economy.spigot.EconomyPlugin;
import me.dirantos.economy.spigot.chat.ChatLevel;
import me.dirantos.economy.spigot.chat.ChatMessenger;
import me.dirantos.economy.spigot.config.RewardConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.*;

public class RewardListener implements Listener {

    private static final Set<Material> MEAT = new HashSet<>(Arrays.asList(
            Material.PORK, Material.GRILLED_PORK, Material.COOKED_BEEF, Material.RAW_BEEF,
            Material.MUTTON, Material.COOKED_MUTTON, Material.RAW_CHICKEN, Material.COOKED_CHICKEN,
            Material.COOKED_RABBIT, Material.RABBIT
    ));

    private static final Map<EntityType, List<Material>> ANIMAL_FOOD = new HashMap<>();

    // list from https://minecraft.gamepedia.com/Breeding
    static {
        ANIMAL_FOOD.put(EntityType.HORSE, a(Material.APPLE, Material.WHEAT, Material.GOLDEN_APPLE, Material.GOLDEN_CARROT, Material.SUGAR, Material.HAY_BLOCK));
        ANIMAL_FOOD.put(EntityType.SHEEP, a(Material.WHEAT));
        ANIMAL_FOOD.put(EntityType.COW, a(Material.WHEAT));
        ANIMAL_FOOD.put(EntityType.MUSHROOM_COW, a(Material.WHEAT));
        ANIMAL_FOOD.put(EntityType.PIG, a(Material.CARROT_ITEM, Material.POTATO_ITEM));
        ANIMAL_FOOD.put(EntityType.CHICKEN, a(Material.SEEDS, Material.PUMPKIN_SEEDS, Material.MELON_SEEDS));
        ANIMAL_FOOD.put(EntityType.WOLF, a(Material.PORK, Material.GRILLED_PORK, Material.COOKED_BEEF, Material.RAW_BEEF, Material.MUTTON, Material.COOKED_MUTTON, Material.RAW_CHICKEN, Material.COOKED_CHICKEN, Material.COOKED_RABBIT, Material.RABBIT, Material.ROTTEN_FLESH, Material.COOKED_FISH, Material.RAW_FISH));
        ANIMAL_FOOD.put(EntityType.OCELOT, a(Material.RAW_FISH));
        ANIMAL_FOOD.put(EntityType.RABBIT, a(Material.YELLOW_FLOWER, Material.CARROT_ITEM, Material.GOLDEN_CARROT));
    }

    private final EconomyPlugin plugin;
    private final RewardConfig rewardConfig;
    private final ChatMessenger messenger;
    private final BankManager bankManager;

    public RewardListener(EconomyPlugin plugin, RewardConfig rewardConfig, BankManager bankManager) {
        this.plugin = plugin;
        this.rewardConfig = rewardConfig;
        this.messenger = plugin.getChatMessenger();
        this.bankManager = bankManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onAnimalFeed(PlayerInteractAtEntityEvent event) {

        if(!(ANIMAL_FOOD.keySet().contains(event.getRightClicked().getType()))) return;
        if(event.getPlayer().getItemInHand() == null) return;
        if(!ANIMAL_FOOD.get(event.getRightClicked().getType()).contains(event.getPlayer().getItemInHand().getType())) return;

        reward(event.getPlayer(), rewardConfig.getRewardFeedingAnimals());
    }

    @EventHandler
    public void onMonsterKill(EntityDeathEvent event) {

        if(event.getEntity().getKiller() == null) return;
        if(!(event.getEntity() instanceof Monster)) return;

        reward(event.getEntity().getKiller(), rewardConfig.getRewardKillingMonsers());
    }

    @EventHandler
    public void onAnimalKill(EntityDeathEvent event) {

        if(event.getEntity().getKiller() == null) return;
        if(!(event.getEntity() instanceof Animals)) return;

        reward(event.getEntity().getKiller(), rewardConfig.getRewardKillingAnimals());
    }

    @EventHandler
    public void onConsumeMeat(PlayerItemConsumeEvent event) {

        if(!event.getItem().getType().isEdible()) return;
        if(!MEAT.contains(event.getItem().getType())) return;

        reward(event.getPlayer(), rewardConfig.getRewardEatingMeat());
    }

    private void reward(Player player, double reward) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Bank bank = bankManager.loadBank(player);
            double newMoney = bank.getMoney() + reward;
            if(newMoney < 0) newMoney = 0;
            bankManager.setBalance(bank, newMoney);

            String s = reward < 0 ? "abgezogen" : "gutgeschrieben";
            ChatLevel level = reward < 0 ? ChatLevel.ERROR : ChatLevel.SUCCESS;
            messenger.send(player, "Du hast [[" + Math.abs(reward) + "$]] " + s + " bekommen", level);
        });
    }

    private static <T> List<T> a(T... a) {
        return Arrays.asList(a);
    }

}
