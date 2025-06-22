package org.pcub.extension.common;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.pcub.extension.Common;

import java.util.HashMap;
import java.util.Map;

// 频繁操作限制
public class OperationLimiter<T> {
    private final Map<T, Integer> map = new HashMap<>();
    private final Plugin plugin = Common.getInstance().main;

    public int get(T player) {
        Integer value = map.get(player);
        if (value == null) return 0;
        return value;
    }

    public int put(T player, long delay) {
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
}
