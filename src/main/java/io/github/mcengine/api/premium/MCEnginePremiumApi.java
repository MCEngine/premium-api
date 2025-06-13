package io.github.mcengine.api.premium;

import org.bukkit.plugin.Plugin;

public class MCEnginePremiumApi {

    private static MCEnginePremiumApi instance;
    private final Plugin plugin;

    public MCEnginePremiumApi(Plugin plugin) {
        instance = this;
        this.plugin = plugin;
    }

    public static MCEnginePremiumApi getApi() {
        return instance;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
