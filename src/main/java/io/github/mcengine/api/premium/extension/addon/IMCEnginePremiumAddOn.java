package io.github.mcengine.api.premium.extension.addon;

import org.bukkit.plugin.Plugin;

/**
 * Interface for AI AddOn modules that can be dynamically loaded
 * as part of the Premium AddOn system in MCEngine.
 */
public interface IMCEnginePremiumAddOn {

    /**
     * Called when the AddOn is loaded by the AI engine.
     *
     * @param plugin The plugin instance providing context.
     */
    void onLoad(Plugin plugin);
}
