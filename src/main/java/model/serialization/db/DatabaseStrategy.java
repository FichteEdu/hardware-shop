package model.serialization.db;

import java.sql.SQLException;

import fpt.com.Product;


public interface DatabaseStrategy {

	Product[] read() throws SQLException;

	Product[] read(int n) throws SQLException;

	void write(Product p) throws SQLException;

	void write(Product[] p) throws SQLException;

}
