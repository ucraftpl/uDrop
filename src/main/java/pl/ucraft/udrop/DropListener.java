package pl.ucraft.udrop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DropListener implements Listener {

    private DropPlugin plugin;
    private List<Material> usedByDispenser;
    public DropListener(DropPlugin plugin){
        this.plugin = plugin;
        this.usedByDispenser = new ArrayList<>();

        this.usedByDispenser.add(Material.EGG);
        this.usedByDispenser.add(Material.EXP_BOTTLE);
        this.usedByDispenser.add(Material.MONSTER_EGG);
        this.usedByDispenser.add(Material.ARROW);
        this.usedByDispenser.add(Material.SPECTRAL_ARROW);
        this.usedByDispenser.add(Material.TIPPED_ARROW);
        this.usedByDispenser.add(Material.MONSTER_EGGS);

        this.usedByDispenser.add(Material.LEATHER_BOOTS);
        this.usedByDispenser.add(Material.LEATHER_LEGGINGS);
        this.usedByDispenser.add(Material.LEATHER_CHESTPLATE);
        this.usedByDispenser.add(Material.LEATHER_HELMET);

        this.usedByDispenser.add(Material.IRON_BOOTS);
        this.usedByDispenser.add(Material.IRON_LEGGINGS);
        this.usedByDispenser.add(Material.IRON_CHESTPLATE);
        this.usedByDispenser.add(Material.IRON_HELMET);

        this.usedByDispenser.add(Material.GOLD_BOOTS);
        this.usedByDispenser.add(Material.GOLD_LEGGINGS);
        this.usedByDispenser.add(Material.GOLD_CHESTPLATE);
        this.usedByDispenser.add(Material.GOLD_HELMET);

        this.usedByDispenser.add(Material.CHAINMAIL_BOOTS);
        this.usedByDispenser.add(Material.CHAINMAIL_LEGGINGS);
        this.usedByDispenser.add(Material.CHAINMAIL_CHESTPLATE);
        this.usedByDispenser.add(Material.CHAINMAIL_HELMET);

        this.usedByDispenser.add(Material.DIAMOND_BOOTS);
        this.usedByDispenser.add(Material.DIAMOND_LEGGINGS);
        this.usedByDispenser.add(Material.DIAMOND_CHESTPLATE);
        this.usedByDispenser.add(Material.DIAMOND_HELMET);

        this.usedByDispenser.add(Material.ELYTRA);

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onItemSpawn(ItemSpawnEvent event){
        if(!plugin.couldPickup(event.getEntity().getItemStack())){
            event.getEntity().setItemStack(plugin.removeDropLore(event.getEntity().getItemStack()));
            return;
        }

        Player player = plugin.getNearbyPlayer(event.getEntity(), 6.0D);
        if (player == null) {
            return;
        }

        ArrayList<ItemStack> items = new ArrayList();
        items.add(event.getEntity().getItemStack());
        items = plugin.addToInventory(player, items);
        if(items.size() == 1){
            event.getEntity().setItemStack(items.get(0));
        }else{
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEntityDeath(EntityDeathEvent event){
        if (event.getEntity().getKiller() != null) {
            List<ItemStack> drops = new ArrayList(event.getDrops());
            event.getDrops().clear();
            if(event.getEntity().getLocation().distance(event.getEntity().getKiller().getLocation()) < 7 && !event.getEntity().getKiller().isDead()){
                drops = plugin.addToInventory(event.getEntity().getKiller(), drops);
            }
            for (ItemStack drop : drops) {
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), plugin.addDropLore(drop));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onItemDrop(PlayerDropItemEvent event){
        event.getItemDrop().setItemStack(plugin.addDropLore(event.getItemDrop().getItemStack()));
    }

    @EventHandler(ignoreCancelled =  true)
    private void onInventoryPickup(InventoryPickupItemEvent event){
        ItemStack itemStack = event.getItem().getItemStack();
        if(!plugin.couldPickup(itemStack)){
            event.getItem().setItemStack(plugin.removeDropLore(itemStack));
        }
    }

    @EventHandler(ignoreCancelled =  true)
    private void onPlayerPickup(EntityPickupItemEvent event){
        ItemStack itemStack = event.getItem().getItemStack();
        if(!plugin.couldPickup(itemStack)){
            event.getItem().setItemStack(plugin.removeDropLore(itemStack));
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onItemDispense(BlockDispenseEvent event){
        if(event.getBlock().getType().equals(Material.DISPENSER)){
            if(usedByDispenser.contains(event.getItem().getType())){
                return;
            }
        }
        ItemStack item = event.getItem();
        event.setItem(plugin.addDropLore(item));
    }

}
