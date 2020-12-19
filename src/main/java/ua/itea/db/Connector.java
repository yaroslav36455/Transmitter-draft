package ua.itea.db;

import java.sql.Connection;

public interface Connector {
	public Connection getConnection();
}
