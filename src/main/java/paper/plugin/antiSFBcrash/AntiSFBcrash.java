package paper.plugin.antiSFBcrash;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class AntiSFBcrash extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // 注册事件监听器
        getServer().getPluginManager().registerEvents(this, this);
        
    }

    @Override
    public void onDisable() {
        
    }

    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
        // 检查发射器是否尝试发射火焰弹
        if (event.getItem().getType() == Material.FIRE_CHARGE) {
            // 取消事件，阻止发射
            event.setCancelled(true);
        }
    }
}