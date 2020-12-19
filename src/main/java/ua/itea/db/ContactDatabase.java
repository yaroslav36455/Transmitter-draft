package ua.itea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ContactDatabase {
	private static final String CREATE_TABLE
	= "CREATE TABLE IF NOT EXISTS `contacts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT"
			+ ", `name` VARCHAR(20) NOT NULL, `address` VARCHAR(15) NOT NULL,"
			+ " `port` INT NOT NULL);";
	private static final String UNIQUE_INDEX
			= " CREATE UNIQUE INDEX IF NOT EXISTS `name_address_port` on `contacts`"
			+ " (`name`, `address`, `port`);";
	private static final String INSERT = "INSERT OR IGNORE INTO `contacts` "
			+ "(name, address, port) VALUES (?, ?, ?);";
	
	public ContactDatabase(Connection conn) throws SQLException {
		Statement statement = conn.createStatement();
		
		statement.execute(CREATE_TABLE);
		statement.execute(UNIQUE_INDEX);
		
		statement.close();
		conn.close();
	}

	public void insert(Connection conn, Contact contact) throws SQLException {
		PreparedStatement statement = conn.prepareStatement(INSERT);
		statement.setString(1, contact.getName());
		statement.setString(2, contact.getAddress());
		statement.setInt(3, contact.getPort());
		
		statement.execute();
		
		statement.close();
		conn.close();
	}
	
	public Contact[] read(Connection conn) {
		return null;
	}
	
	public void remove(Contact ...contact) {
		
	}
}
