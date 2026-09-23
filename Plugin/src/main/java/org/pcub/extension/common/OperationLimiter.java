package org.pcub.extension.common;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.pcub.extension.Common;

import java.util.concurrent.ConcurrentHashMap;

// 频繁操作限制
public class OperationLimiter<T> {
    private final ConcurrentHashMap<T, Integer> map = new ConcurrentHashMap<>();
    private final Plugin plugin = Common.getInstance().main;

    public int get(T player) {
        Integer value = map.get(player);
        if (value == null) return 0;
        return value;
    }

    public int put(T player, long delay) {
        int count = map.compute(player, (k, origin) ->
                origin == null ? 1 : origin % 2147483646 + 1); // 加一分，达最大值会导致限制被重置
        new BukkitRunnable() {
            @Override
            public void run() {
                map.remove(player, count);
            }
        }.runTaskLaterAsynchronously(plugin, delay);
        // 增加后的值
        return count;
    }
}
