package me.youhavetrouble.examples;

import org.bukkit.plugin.java.JavaPlugin;

public final class Examples extends JavaPlugin {

    private static Examples pluginInstance;

    @Override
    public void onEnable() {
        pluginInstance = this;
        getLogger().severe("This is not meant to be used as a literal plugin, it's only loose group of examples on how to do things. cease.");
        getServer().getPluginManager().disablePlugin(this);
        return;
    }

    public static Examples getPluginInstance() {
        return pluginInstance;
    }
}
