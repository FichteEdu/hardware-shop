package fpt.com;

import java.sql.Connection;
import java.sql.SQLException;

public interface JDBCInterface {
	void insert(Product product) throws SQLException;

	long insert(String name, double price, int quantity) throws SQLException;

	Product read(long productId) throws SQLException;

	ProductList findByName(String name) throws SQLException;

	ProductList findByQuantity(int quantity) throws SQLException;

	ProductList find(String name, double price, int quantity)
			throws SQLException;

	Connection getConnection();

	void setConnection(Connection connection);

	void dumpMetaData() throws SQLException;
}
