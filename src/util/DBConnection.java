package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
	private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/hospital_db";
	private static final String DEFAULT_USER = "root";
	private static final String DB_URL = getEnvOrDefault("HMS_DB_URL", DEFAULT_URL);
	private static final String DB_USER = getEnvOrDefault("HMS_DB_USER", DEFAULT_USER);
	private static final String DB_PASSWORD = getEnvOrDefault("HMS_DB_PASSWORD", "");

	private DBConnection() {
	}

	private static String getEnvOrDefault(String key, String fallback) {
		String value = System.getenv(key);
		if (value == null || value.trim().isEmpty()) {
			return fallback;
		}
		return value;
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
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
