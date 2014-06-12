package model.serialization.db;

import java.sql.SQLException;

import model.Product;


public class JDBCTest {

	public static void main(String[] args) {

		try (JDBCConnector jdbc = new JDBCConnector()) {

			long prod1id = jdbc.insert("prod1", 123, 20);

			Product prod2 = new Product("prod2", 1234, 200);
			jdbc.insert(prod2);

			System.out.println(jdbc.read(prod1id).getName());
			System.out.println(jdbc.read(prod2.getId()).getQuantity());
			System.out.println();

			jdbc.dumpMetaData();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
