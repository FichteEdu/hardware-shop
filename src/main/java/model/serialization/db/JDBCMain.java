package model.serialization.db;

import java.sql.SQLException;
import model.Product;


public class JDBCMain {
	
	public static void main (String[] args) {
		
		JDBCConnector connector = new JDBCConnector();
		
		Product prod = new Product("name", 123, 20);
		Product prod2;
		
		try {
			connector.insert(prod);
			prod2 = new Product(connector.insert("name", 1234, 200), "name", 123, 20);
			
			System.out.println(connector.read(200L).getName());
			System.out.println(connector.read(200L).getQuantity());
			connector.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
