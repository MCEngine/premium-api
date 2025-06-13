package io.github.mcengine.api.premium.database.mysql;

import io.github.mcengine.api.premium.database.IMCEnginePremiumApiDB;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * MySQL implementation of the Premium API database using plugin configuration.
 */
public class MCEnginePremiumApiMySQL implements IMCEnginePremiumApiDB {

    private Connection connection;

    /**
     * Initializes MySQL connection using plugin configuration.
     *
     * Required config paths in plugin.yml:
     * - database.mysql.host
     * - database.mysql.port
     * - database.mysql.database
     * - database.mysql.user
     * - database.mysql.password
     *
     * @param plugin Bukkit plugin instance
     */
    public MCEnginePremiumApiMySQL(Plugin plugin) {
        String host = plugin.getConfig().getString("database.mysql.host", "localhost");
        String port = plugin.getConfig().getString("database.mysql.port", "3306");
        String database = plugin.getConfig().getString("database.mysql.database", "mcengine");
        String user = plugin.getConfig().getString("database.mysql.user", "root");
        String password = plugin.getConfig().getString("database.mysql.password", "");

        try {
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to MySQL: " + e.getMessage());
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void createPremiumRank(String rankType) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(String.format("""
                CREATE TABLE IF NOT EXISTS premium_rank_%s (
                    uuid VARCHAR(36) NOT NULL PRIMARY KEY,
                    rank INT NOT NULL
                );
            """, rankType.toLowerCase()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getPremiumRank() {
        // Implementation placeholder
    }

    @Override
    public void disConnection() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
