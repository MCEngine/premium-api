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

    /**
     * Returns the current MySQL database connection.
     *
     * @return active MySQL connection
     */
    @Override
    public Connection getConnection() {
        return connection;
    }

    /**
     * Creates a premium rank table for the specified rank type if it does not exist.
     *
     * @param rankType Type of rank (e.g., vip, vvip)
     */
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

    /**
     * Retrieves the rank of a player for the specified rank type from the database.
     *
     * @param uuid     Player UUID
     * @param rankType Rank type (e.g., vip, vvip)
     * @return Integer rank value, or -1 if not found
     */
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

    /**
     * Closes the MySQL database connection if open.
     */
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
