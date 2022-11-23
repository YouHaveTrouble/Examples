package me.youhavetrouble.examples;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;


/**
 * An example on how to handle denying putting shulker box inside ender chest with minimal impact on regular experience.
 * You should still be able to move and drag the shulker in the bottom inventory
 */
public class PreventShulkersInEnderChest implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryInteract(InventoryClickEvent event) {

        // We only care about enderchest
        if (!InventoryType.ENDER_CHEST.equals(event.getInventory().getType())) return;

        switch (event.getAction()) {
            case MOVE_TO_OTHER_INVENTORY, HOTBAR_SWAP -> {
                if (event.getCurrentItem() == null) return;
                // Our only concer is shulker box
                if (!Material.SHULKER_BOX.equals(event.getCurrentItem().getType())) return;
                event.setCancelled(true);
            }
            case PLACE_ALL, PLACE_SOME, PLACE_ONE, SWAP_WITH_CURSOR -> {
                if (event.getCursor() == null) return;
                // Our only concer is shulker box
                if (!Material.SHULKER_BOX.equals(event.getCursor().getType())) return;
                // Only interfere if item was placed in top inventory
                if (event.getClickedInventory() != event.getView().getTopInventory()) return;
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        // We only care about enderchest
        if (!InventoryType.ENDER_CHEST.equals(event.getInventory().getType())) return;

        // Our only concer is shulker box
        if (!Material.SHULKER_BOX.equals(event.getOldCursor().getType())) return;

        for (int slot : event.getRawSlots()) {
            // Check if the slots that were dragged through were container slots (top inventory)
            if (!InventoryType.SlotType.CONTAINER.equals(event.getView().getSlotType(slot))) continue;
            event.setCancelled(true);
            return;
        }

    }

}
