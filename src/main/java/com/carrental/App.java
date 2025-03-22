package com.carrental;
import com.carrental.database.DatabaseConnection;
import java.sql.Connection;
/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Successfully connected to MySQL Database!");
            }
        } catch (Exception e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
        }
    }
}
