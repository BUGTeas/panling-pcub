package org.pcub.extension;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Main extends JavaPlugin {
    // 获取控制台
    public final Server server = getServer();
    public final CommandSender console = server.getConsoleSender();
    public final Logger logger = getLogger();

    // 检查插件
    public boolean haveGeyser = false;
    public boolean haveFloodgate = false;
    public boolean havePAPI = false;



    @Override
    public void onLoad() {
        PluginManager pluginManager = server.getPluginManager();
        if (pluginManager.getPlugin("Geyser-Spigot") != null) this.haveGeyser = true;
        if (pluginManager.getPlugin("floodgate") != null) this.haveFloodgate = true;
        if (pluginManager.getPlugin("PlaceholderAPI") != null) this.havePAPI = true;
    }


    @Override
    public void onEnable() {
        Common common = new Common(this);
        server.getPluginManager().registerEvents(new EventListener(common), this);
        CommandExecuter commandExec = new CommandExecuter(common);
        PluginCommand pluginCommand = Bukkit.getPluginCommand("pcub");
        pluginCommand.setExecutor(commandExec);
        pluginCommand.setTabCompleter(commandExec);
    }
}
