package pl.ucraft.udrop;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class DropPlugin extends JavaPlugin {

    private String secret = "2113_UCRAFT_NAJLEPSZY_JEST_HE4223";

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new DropListener(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean couldPickup(ItemStack is){
        return (is == null) || (is.getItemMeta() == null) || (is.getItemMeta().getLore() == null) ||
                (!is.getItemMeta().getLore().contains(secret));
    }

    public ItemStack removeDropLore(ItemStack itemStack){
        if(!couldPickup(itemStack)){
            ItemMeta itemMeta = itemStack.getItemMeta();
            if(itemMeta.hasLore()){
                List<String> lore = itemMeta.getLore();
                lore.remove(secret);
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
            }
        }
        return itemStack;
    }

    public ItemStack addDropLore(ItemStack itemStack){
        if(couldPickup(itemStack)){
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = new ArrayList();
            if(itemMeta.hasLore()) {
                lore = itemMeta.getLore();
            }
            lore.add(secret);
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public Player getNearbyPlayer(Entity entity, double range){
        Player player = null;
        double mindist = range + 1.0D;

        for (Entity nearbyEntity : entity.getNearbyEntities(range, range, range)) {
            if(nearbyEntity instanceof Player){
                if(nearbyEntity.isDead()){
                    continue;
                }

                if(((Player) nearbyEntity).getGameMode().equals(GameMode.SPECTATOR)){
                    continue;
                }

                if(((Player) nearbyEntity).getGameMode().equals(GameMode.CREATIVE)){
                    continue;
                }


                double distance = nearbyEntity.getLocation().distance(entity.getLocation());
                if (distance < mindist) {
                    mindist = distance;
                    player = (Player) nearbyEntity;
                }
            }
        }

        return player;
    }

    public ArrayList<ItemStack> addToInventory(Player p, Collection<ItemStack> finalItems) {
        ArrayList<ItemStack> remaining = new ArrayList<>();
        if (finalItems != null) {
            ItemStack[] itemBoard = new ItemStack[finalItems.size()];
            for (int i = 0; i < finalItems.size(); i++) {
                itemBoard[i] = ((ArrayList<ItemStack>) finalItems).get(i);
            }
            remaining.addAll(p.getInventory().addItem(itemBoard).values());
        }
        return remaining;
    }
}
