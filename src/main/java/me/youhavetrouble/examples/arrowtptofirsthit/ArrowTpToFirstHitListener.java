package me.youhavetrouble.examples.arrowtptofirsthit;

import me.youhavetrouble.examples.Examples;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.persistence.PersistentDataType;

/**
 * This listener has a goal of teleporting the player to the entity hit with an arrow, but only once ever.
 */
public class ArrowTpToFirstHitListener implements Listener {

    /**
     * Method #1 - advancement
     */
    NamespacedKey takeAimNamespacedKey = NamespacedKey.minecraft("adventure/shoot_arrow");

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onFirstTimeHitEntityWithArrow(ProjectileHitEvent event) {

        // Do nothing if projectile hit a block
        if (event.getHitEntity() == null) return;

        // Check if the projectile is arrow, do nothing if not
        if (!(event.getEntity() instanceof Arrow arrow)) return;

        // Check if projectile shooter is a player, do nothing if not
        if (!(arrow.getShooter() instanceof Player player)) return;

        Advancement takeAimAdvancement = Bukkit.getAdvancement(takeAimNamespacedKey);

        // Check if advancement exists (it might not if it's disabled) and do nothing if not
        if (takeAimAdvancement == null) return;

        // Check if advancement is already done and do nothing if so
        if (player.getAdvancementProgress(takeAimAdvancement).isDone()) return;

        // Teleport the player
        player.teleport(event.getEntity().getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }


    /**
     * RECOMMENDED
     * Method #2 - PersistentDataContainer
     */
    NamespacedKey pdcKey = new NamespacedKey(Examples.getPluginInstance(), "hitsomethingwitharrow");

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onFirstTimeHitEntityWithArrowTwo(ProjectileHitEvent event) {

        // Do nothing if projectile hit a block
        if (event.getHitEntity() == null) return;

        // Check if the projectile is arrow, do nothing if not
        if (!(event.getEntity() instanceof Arrow arrow)) return;

        // Check if projectile shooter is a player, do nothing if not
        if (!(arrow.getShooter() instanceof Player player)) return;

        // If player already has pdc entry, do nothing
        if (player.getPersistentDataContainer().has(pdcKey)) return;

        // Set pdc entry on the player
        player.getPersistentDataContainer().set(pdcKey, PersistentDataType.STRING, "something, anything");

        // Teleport the player
        player.teleport(event.getEntity().getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

}
