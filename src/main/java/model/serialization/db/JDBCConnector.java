package model.serialization.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import fpt.com.Product;


public class JDBCConnector implements AutoCloseable {

	private Connection	con;

	public JDBCConnector() throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");

		con = DriverManager.getConnection("jdbc:postgresql://java.is.uni-due.de/ws1011", "ws1011",
				"ftpw10");
	}

	public void insert(Product p) throws SQLException {
		p.setId(insert(p.getName(), p.getPrice(), p.getQuantity()));
	}

	public long insert(String name, double price, int quantity) throws SQLException {
		try (
				PreparedStatement pstmt = con.prepareStatement(
						"INSERT INTO products (name, quantity, price) VALUES(?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, name);
			pstmt.setDouble(2, price);
			pstmt.setInt(3, quantity);
			pstmt.executeUpdate();

			try (ResultSet rs = pstmt.getGeneratedKeys()) {
				rs.next();
				Integer key = rs.getInt(1);
				return key.longValue();
			}
		}
	}

	public Product read(long productId) throws SQLException {
		try (
				PreparedStatement pstmt = con
						.prepareStatement("SELECT id, name, price, quantity FROM products WHERE id=?")) {
			pstmt.setLong(1, productId);

			try (ResultSet rs = pstmt.executeQuery()) {
				return new model.Product(productId, rs.getString(2), rs.getDouble(3), rs.getInt(4));
			}
		}

	}

	/**
	 * Returns an array of length 0 to n with products.
	 * 
	 * @param n
	 * @return
	 * @throws SQLException
	 */
	public Product[] readSomeLast(int n) throws SQLException {
		ArrayList<Product> prodList = new ArrayList<Product>();

		try (
				PreparedStatement pstmt = con
						.prepareStatement("SELECT id, name, price, quantity FROM products ORDER BY id DESC")) {

			pstmt.setMaxRows(n);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					prodList.add(new model.Product(rs.getLong(1), rs.getString(2), rs.getDouble(3),
							rs.getInt(4)));
				}
			}

			// Reverse list to have the last n products back in ascending order
			Collections.reverse(prodList);
			return prodList.toArray(new Product[0]);
		}
	}

	/**
	 * Returns an array with all products.
	 * 
	 * @param n
	 * @return
	 * @throws SQLException
	 */
	public Product[] readSome() throws SQLException {
		ArrayList<Product> prodList = new ArrayList<>();

		try (
				PreparedStatement pstmt = con
						.prepareStatement("SELECT id, name, price, quantity FROM products ORDER BY id DESC")) {

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					prodList.add(new model.Product(rs.getLong(1), rs.getString(2), rs.getDouble(3),
							rs.getInt(4)));
				}
			}

			return (Product[]) prodList.toArray();
		}
	}

	public void writeSome(Product[] products) throws SQLException {
		for (Product p : products) {
			insert(p);
		}
	}

	public void dumpMetaData() throws SQLException {
		DatabaseMetaData dmd = con.getMetaData();
		System.out.println("URL: " + dmd.getURL());
		System.out.println("Username: " + dmd.getUserName());
		System.out.println("Tables: ");
		try (ResultSet rs = dmd.getTables(null, null, null, null)) {
			while (rs.next()) {
				System.out.println(rs.getString("TABLE_NAME"));
			}
		}
	}

	@Override
	public void close() throws SQLException {
		con.close();
	}
}
