package io.github.mcengine.api.premium.database;

import java.sql.Connection;

/**
 * Interface for managing database operations related to MCEngine Premium system.
 */
public interface IMCEnginePremiumApiDB {

    /**
     * Gets the current database connection.
     *
     * @return a {@link Connection} object to the database.
     */
    Connection getConnection();

    /**
     * Creates the necessary tables or schema for storing premium rank data.
     */
    void createPremiumRank();

    /**
     * Retrieves premium rank data from the database.
     */
    void getPremiumRank();

    /**
     * Closes the current database connection.
     */
    void disConnection();
}
