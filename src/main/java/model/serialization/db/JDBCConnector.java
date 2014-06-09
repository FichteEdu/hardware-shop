package model.serialization.db;

import java.sql.*;

import model.Product;

public class JDBCConnector {
	
	private Connection con;
	
	public void insert(Product product) throws SQLException {
		
		PreparedStatement pstmt = con.prepareStatement("INSERT INTO products (name, quantity, price) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		pstmt.setString(1, product.getName());
		pstmt.setDouble(2, product.getPrice());
		pstmt.setInt(3, product.getQuantity());		
		pstmt.executeUpdate();
		
		ResultSet rs = pstmt.getGeneratedKeys();		
		rs.next();		
		Integer key = rs.getInt(1);
		
		product.setId(key.longValue());

		rs.close();
		pstmt.close();
	}
	
	public long insert (String name, double price, int quantity) throws SQLException {
		
		PreparedStatement pstmt = con.prepareStatement("INSERT INTO products (name, quantity, price) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		pstmt.setString(1, name);
		pstmt.setDouble(2, price);
		pstmt.setInt(3, quantity);		
		pstmt.executeUpdate();
		
		ResultSet rs = pstmt.getGeneratedKeys();		
		rs.next();		
		Integer key = rs.getInt(1);

		rs.close();
		pstmt.close();
		
		return key.longValue();
	}
	
	public Product read (long productId) throws SQLException {
		
		PreparedStatement pstmt = con.prepareStatement("SELECT id, name, price, quantity FROM products WHERE id=?");
		pstmt.setLong(1, productId);
		
		ResultSet rs = pstmt.executeQuery();
		
		if (rs != null && rs.next()) {
			try {
				return new Product(productId, rs.getString(2), rs.getDouble(3), rs.getInt(4));
			} finally {
				rs.close();
				pstmt.close();
			}
		} else {
			try {
				return null;
			} finally {
				rs.close();
				pstmt.close();
				throw new SQLException("There's no entry with such an ID");
			}
		}
	}	
	
	public void dumpMetaData() throws SQLException {
		
		DatabaseMetaData dmd = con.getMetaData();
		System.out.println("URL: " + dmd.getURL());
		System.out.println("Username: " + dmd.getUserName());
		System.out.println("Tables: ");
		ResultSet rs = dmd.getTables(null, null, null, null);
		while(rs.next()) {
			System.out.println(rs.getString("TABLE_NAME"));
		}
		
		rs.close();
	}
	
	public void close() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				//Should only rarely occur and DB will close connection itself after appropriate amount of time.
			}
		}
	}
	
	public JDBCConnector () {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Treiber wurde nicht gefunden.");
		}
		
		try {
			con = DriverManager.getConnection("jdbc:postgresql://java.is.uni-due.de/ws1011", "ws1011", "ftpw10");
		} catch (SQLException e) {
			System.out.println("Verbindung zu Datenbank konnte nicht hergestellt werden.");
		}
	}
}
