package model.serialization;

import java.sql.SQLException;

import model.serialization.db.DatabaseStrategy;
import model.serialization.db.JDBCConnector;
import fpt.com.Product;


public class JDBCStrategy implements DatabaseStrategy {

	@Override
	public void write(Product p) throws SQLException {
		// Only write to database if the id is -1 (empty)
		if (p.getId() == -1) {
			try (JDBCConnector con = new JDBCConnector()) {
				con.insert(p);
			} catch (ClassNotFoundException e1) {
				throw new SQLException("Unable to find driver");
			}
		}
	}

	@Override
	public void write(Product[] products) throws SQLException {
		try (JDBCConnector con = new JDBCConnector()) {
			for (Product p : products)
				// Only write to database if the id is -1 (empty)
				if (p.getId() == -1)
					con.insert(p);
		} catch (ClassNotFoundException e1) {
			throw new SQLException("Unable to find driver");
		}
	}

	@Override
	public Product[] read() throws SQLException {
		try (JDBCConnector con = new JDBCConnector()) {
			return con.readSome();
		} catch (ClassNotFoundException e1) {
			throw new SQLException("Unable to find driver");
		}
	}

	@Override
	public Product[] read(int n) throws SQLException {
		try (JDBCConnector con = new JDBCConnector()) {
			return con.readSomeLast(n);
		} catch (ClassNotFoundException e1) {
			throw new SQLException("Unable to find driver");
		}
	}

}
