package io.github.mcengine.api.premium.extension.api;

import org.bukkit.plugin.Plugin;

/**
 * Interface for AI DLC modules that can be dynamically loaded
 * as part of the Premium API system in MCEngine.
 */
public interface IMCEnginePremiumAPI {

    /**
     * Called when the DLC is loaded by the AI engine.
     *
     * @param plugin The plugin instance providing context.
     */
    void onLoad(Plugin plugin);
}
