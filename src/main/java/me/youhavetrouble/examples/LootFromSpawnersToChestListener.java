package me.youhavetrouble.examples;

import com.destroystokyo.paper.event.entity.PreSpawnerSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

import java.util.*;

public class LootFromSpawnersToChestListener implements Listener {
    private final Random random = new Random();
    private final Entity dummyEntity;

    public LootFromSpawnersToChestListener() {
        // Create dummy entity to pass into LootContext
        World world = Bukkit.getWorlds().get(0);
        dummyEntity = Bukkit.getWorlds().get(0).spawnEntity(new Location(world, 0,5000,0), EntityType.ARMOR_STAND);
        // Remove the entity after saving reference to it
        dummyEntity.remove();
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onSpawnerSpawn(PreSpawnerSpawnEvent event) {
        Location spawnerLocation = event.getSpawnerLocation();
        Block chest = spawnerLocation.getBlock().getRelative(0, -1, 0);

        // If the block below the spawner doesn't have inventory, we don't care
        if (!(chest.getState() instanceof Container container)) return;

        EntityType entity = event.getType();

        // Get loot table key. This needs to be modified if you want any custom mobs
        NamespacedKey entityLootKey = new NamespacedKey(NamespacedKey.MINECRAFT, "entities/"+entity.getKey().getKey());
        LootTable lootTable = Bukkit.getServer().getLootTable(entityLootKey);

        // In case something weird happens, escape before it blows up
        if (lootTable == null) return;

        // Cancel the event, so no mobs spawn
        event.setCancelled(true);

        // Get player nearby. This needs to be modified if there's anything altering spawner activity ranges.
        Player player = (Player) spawnerLocation.getNearbyEntitiesByType(Player.class, 32).toArray()[0];

        LootContext lootContext = new LootContext.Builder(chest.getLocation())
                .killer(player)
                .lootedEntity(dummyEntity)
                .luck(0)
                .lootingModifier(3)
                .build();

        // Get loot from the loot table
        Collection<ItemStack> loot = lootTable.populateLoot(random, lootContext);

        // Shove the loot into the container
        loot.forEach(itemStack -> container.getInventory().addItem(itemStack));
    }

}
