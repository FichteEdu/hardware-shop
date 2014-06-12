package model.serialization.db;

import java.sql.SQLException;

import fpt.com.Product;


public class OpenJPAStrategy implements DatabaseStrategy {

	@Override
	public void write(Product p) throws SQLException {
		// Only write to database if the id is -1 (empty)
		if (p.getId() == -1) {
			try (OpenJPAConnector con = new OpenJPAConnector()) {
				con.insert(p);
			}
		}
	}

	@Override
	public void write(Product[] products) throws SQLException {
		try (OpenJPAConnector con = new OpenJPAConnector()) {
			for (Product p : products)
				// Only write to database if the id is -1 (empty)
				if (p.getId() == -1) {
					p.setId(0); // Assign default value again because OpenJPA needs it
					con.insert(p);
				}
		}
	}

	@Override
	public Product[] read() throws SQLException {
		try (OpenJPAConnector con = new OpenJPAConnector()) {
			return con.readSome();
		}
	}

	@Override
	public Product[] read(int n) {
		try (OpenJPAConnector con = new OpenJPAConnector()) {
			return con.readSomeLast(n);
		}
	}

}
