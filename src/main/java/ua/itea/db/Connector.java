package ua.itea.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface Connector {
	public Connection getConnection() throws SQLException;
}
