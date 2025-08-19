package paper.plugin.antiSFBcrash;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;

public final class AntiSFBcrash extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("AntiSFBcrash plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("AntiSFBcrash plugin has been disabled.");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block != null && block.getType() == Material.DISPENSER) {
            // 如果玩家与发射器交互，添加自定义逻辑（可选）
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getType() == InventoryType.DISPENSER) {
            checkDispenser(event.getInventory());
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.DISPENSER) {
            checkDispenser(event.getInventory());
        }
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        ItemStack pickedItem = event.getItem().getItemStack();
        if (pickedItem.getType() == Material.FIRE_CHARGE) {
            // 检查是否是玩家
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                Block block = event.getItem().getLocation().getBlock();
                if (block.getType() == Material.DISPENSER) {
                    Inventory inventory = Bukkit.createInventory(null, 9, Component.text("Dispenser"));

                    // 复制原始发射器的内容到库存
                    inventory.setContents(Bukkit.createInventory(null, 9).getContents());

                    // 打开发射器给玩家
                    player.openInventory(inventory);

                    // 延迟关闭发射器
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            // 检查内容并清除烈焰弹
                            checkDispenser(inventory);

                            // 关闭发射器
                            player.closeInventory();
                        }
                    }.runTaskLater(this, 20L); // 20 ticks = 1秒
                }
            }
        }
    }

    // 阻止烈焰弹放入发射器
    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        if (event.getDestination().getType() == InventoryType.DISPENSER) {
            ItemStack item = event.getItem();
            if (item.getType() == Material.FIRE_CHARGE) {
                event.setCancelled(true); // 取消传输
            }
        }
    }

    private void checkDispenser(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && item.getType() == Material.FIRE_CHARGE) {
                inventory.clear(i); // 清除烈焰弹
            }
        }
    }
}
