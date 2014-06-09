package model.serialization.db;

import java.sql.SQLException;

import model.Product;

public class DBMain {
	
	public static void main (String[] args) {
		
		JDBCConnector jdbc = new JDBCConnector();
		OpenJPAStrategy openjpa = new OpenJPAStrategy();	
		
		Product prod = new Product("name", 123, 20);
		Product prod2;
		
		try {
			/*
			jdbc.insert(prod);
			
			prod2 = new Product(jdbc.insert("name", 1234, 200), "name", 123, 20);
			
			System.out.println(jdbc.read(200L).getName());
			System.out.println(jdbc.read(200L).getQuantity());
			jdbc.dumpMetaData();
			*/
			
			openjpa.writeObject(prod);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			//jdbc.close();
		}
	}
}
