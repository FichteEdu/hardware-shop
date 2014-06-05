package model.serialization.db;

import java.sql.SQLException;


public class JDBCMain {
	
	public static void main (String[] args) {
		JDBCConnector connector = new JDBCConnector();
		try {
			connector.getDebug();
			System.out.println(connector.insert("test", 100, 2));
			System.out.println(connector.read(3731).getName());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
