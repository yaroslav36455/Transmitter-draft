package ua.itea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContactDatabase {
	private static final String CREATE_TABLE
	= "CREATE TABLE IF NOT EXISTS `contacts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT"
			+ ", `name` VARCHAR(20) NOT NULL, `address` VARCHAR(15) NOT NULL,"
			+ " `port` INT NOT NULL);";
	private static final String UNIQUE_INDEX
			= " CREATE UNIQUE INDEX IF NOT EXISTS `name_address_port` on `contacts`"
			+ " (`name`, `address`, `port`);";
	private static final String INSERT = "INSERT OR IGNORE INTO `contacts` "
			+ "(`name`, `address`, `port`) VALUES (?, ?, ?);";
	private static final String READ_ALL = "SELECT * FROM `contacts`;";
	private static final String REMOVE = "DELETE FROM `contacts` WHERE"
			+ " `contacts`.`name` = ? AND `contacts`.`address` = ?"
			+ " AND `contacts`.`port` = ?;";
	
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
	
	public List<Contact> read(Connection conn) throws SQLException {
		List<Contact> contacts = new ArrayList<>();
		Statement statement = conn.createStatement();
		
		ResultSet result = statement.executeQuery(READ_ALL);
		
		while(result.next()) {
			Contact contact = new Contact(result.getString("name"),
										  result.getString("address"),
										  result.getInt("port"));
			
			contacts.add(contact);
		}
		
		result.close();
		statement.close();
		conn.close();
		return contacts;
	}
	
	public void remove(Connection conn, List<Contact> contacts) throws SQLException {
		PreparedStatement statement = conn.prepareStatement(REMOVE);
		
		try {
			conn.setAutoCommit(false);
			for (Contact contact : contacts) {
				statement.setString(1, contact.getName());
				statement.setString(2, contact.getAddress());
				statement.setInt(3, contact.getPort());
				
				statement.execute();
			}
			conn.commit();
		} catch(SQLException e) {
			conn.rollback();
			throw e;
		}

		statement.close();
		conn.close();
	}
}
