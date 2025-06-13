package io.github.mcengine.api.premium.database.sqlite;

import io.github.mcengine.api.premium.database.IMCEnginePremiumApiDB;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SQLite implementation of the Premium API database using plugin configuration.
 */
public class MCEnginePremiumApiSQLite implements IMCEnginePremiumApiDB {

    private Connection connection;

    /**
     * Initializes SQLite connection using plugin configuration.
     *
     * Config path:
     * - database.sqlite.path (default: "premium.db")
     *
     * @param plugin Bukkit plugin instance
     */
    public MCEnginePremiumApiSQLite(Plugin plugin) {
        String dbPath = plugin.getConfig().getString("database.sqlite.path", "premium.db");
        File dbFile = new File(plugin.getDataFolder(), dbPath);

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to SQLite: " + e.getMessage());
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
                    uuid TEXT NOT NULL PRIMARY KEY,
                    rank INTEGER NOT NULL
                );
            """, rankType.toLowerCase()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getPremiumRank(String uuid, String rankType) {
        String query = String.format("SELECT rank FROM premium_rank_%s WHERE uuid = ?", rankType.toLowerCase());
        try (var pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, uuid);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("rank");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // not found
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
