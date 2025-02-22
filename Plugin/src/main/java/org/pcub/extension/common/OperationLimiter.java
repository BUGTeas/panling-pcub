package org.pcub.extension.common;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

// 频繁操作限制
public class OperationLimiter {
    private final Map<Player, Integer> map = new HashMap<>();
    private final Plugin plugin;

    public int get(Player player) {
        Integer value = map.get(player);
        if (value == null) return 0;
        return value;
    }

    public int put(Player player, long delay) {
        int count = get(player) % 2147483646 + 1; // 加一分，达最大值会导致限制被重置
        map.put(player, count);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (get(player) == count) map.remove(player);
            }
        }.runTaskLaterAsynchronously(plugin, delay);
        // 增加后的值
        return count;
    }

    public OperationLimiter(Plugin plugin) {
        this.plugin = plugin;
    }
}
