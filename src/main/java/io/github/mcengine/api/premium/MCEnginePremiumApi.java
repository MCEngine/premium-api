package io.github.mcengine.api.premium;

import io.github.mcengine.api.premium.database.IMCEnginePremiumApiDB;
import io.github.mcengine.api.premium.database.mysql.MCEnginePremiumApiMySQL;
import io.github.mcengine.api.premium.database.sqlite.MCEnginePremiumApiSQLite;
import org.bukkit.plugin.Plugin;

/**
 * Premium API main access class.
 */
public class MCEnginePremiumApi {

    private static MCEnginePremiumApi instance;
    private final Plugin plugin;
    private final IMCEnginePremiumApiDB db;

    /**
     * Constructs the Premium API instance with plugin reference and database setup.
     *
     * @param plugin Bukkit plugin instance
     */
    public MCEnginePremiumApi(Plugin plugin) {
        instance = this;
        this.plugin = plugin;

        String dbType = plugin.getConfig().getString("database.type", "sqlite").toLowerCase();
        switch (dbType) {
            case "sqlite" -> this.db = new MCEnginePremiumApiSQLite(plugin);
            case "mysql" -> this.db = new MCEnginePremiumApiMySQL(plugin);
            default -> throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }
    }

    /**
     * Gets the singleton API instance.
     *
     * @return instance of {@link MCEnginePremiumApi}
     */
    public static MCEnginePremiumApi getApi() {
        return instance;
    }

    /**
     * Gets the plugin associated with this API.
     *
     * @return the Bukkit plugin instance
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Gets the initialized Premium database implementation.
     *
     * @return the Premium DB interface
     */
    public IMCEnginePremiumApiDB getDB() {
        return db;
    }

    /**
     * Gets the premium rank value of the given player and rank type.
     *
     * @param uuid     Player UUID
     * @param rankType Type of rank table (e.g. vip, vvip)
     * @return Integer rank value, or -1 if not found
     */
    public int getPremiumRank(String uuid, String rankType) {
        return db.getPremiumRank(uuid, rankType);
    }
}
