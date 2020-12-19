package ua.itea.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectorImpl implements Connector {
	private static final String URL = "jdbc:sqlite:contacts.db/?";
	private static final String USER = "root";
	private static final String PASSWORD = "";
	
	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

}
