package com.carrental;

import com.carrental.database.DatabaseConnection;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.sql.Connection;

public class AppTest {

    @Test
    void testDatabaseConnection() {
        Connection connection = DatabaseConnection.getConnection();
        assertNotNull(connection, "Connection should not be null");
        System.out.println("✅ Database connection test passed.");
    }
}
