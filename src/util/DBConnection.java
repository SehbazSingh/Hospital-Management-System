package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
	private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/hospital_db";
	private static final String DEFAULT_USER = "root";
	private static final String DEFAULT_PASSWORD = "7626";

	private DBConnection() {
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DEFAULT_URL, DEFAULT_USER, DEFAULT_PASSWORD);
	}

	public static boolean testConnection() {
		try (Connection connection = getConnection()) {
			return connection != null && !connection.isClosed();
		} catch (SQLException e) {
			System.err.println("Database connection failed: " + e.getMessage());
			return false;
		}
	}
}
