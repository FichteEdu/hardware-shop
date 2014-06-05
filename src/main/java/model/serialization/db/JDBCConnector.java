package model.serialization.db;

import java.sql.*;

import model.Product;

public class JDBCConnector {
	
	private Connection con;
	
	public JDBCConnector () {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void getDebug () throws SQLException {
		if(this.con == null) {
				this.con = DriverManager.getConnection("jdbc:postgresql://java.is.uni-due.de/ws1011", "ws1011", "ftpw10");
		}
		DatabaseMetaData dmd = this.con.getMetaData();
		System.out.println("URL: " + dmd.getURL());
		System.out.println("Username: " + dmd.getUserName());
		System.out.println("Tables: ");
		ResultSet rs = dmd.getTables(null, null, null, null);
		while(rs.next()) {
			System.out.println(rs.getString("TABLE_NAME"));
		}
	}
	
	public long insert (String name, double price, int quantity) throws SQLException {
		if(this.con == null) {
			this.con = DriverManager.getConnection("jdbc:postgresql://java.is.uni-due.de/ws1011", "ws1011", "ftpw10");
		}
		
		PreparedStatement pstmt = con.prepareStatement("INSERT INTO products (name, quantity, price) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		pstmt.setString(1, name);
		pstmt.setDouble(2, price);
		pstmt.setInt(3, quantity);
		pstmt.executeUpdate();
		
		long key = -1L;
		ResultSet rs = pstmt.getGeneratedKeys();
		if (rs != null && rs.next()) {
			key = rs.getLong(1);
		}
		return key;
	}
	
	public void insert (Product product) throws SQLException {
		if(this.con == null) {
			this.con = DriverManager.getConnection("jdbc:postgresql://java.is.uni-due.de/ws1011", "ws1011", "ftpw10");
		}
		
		PreparedStatement pstmt = con.prepareStatement("INSERT INTO products (name, quantity, price) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		pstmt.setString(1, product.getName());
		pstmt.setDouble(2, product.getPrice());
		pstmt.setInt(3, product.getQuantity());
		pstmt.executeUpdate();
		
		long key = -1L;
		ResultSet rs = pstmt.getGeneratedKeys();
		if (rs != null && rs.next()) {
			key = rs.getLong(1);
		}
		product.setId(key);
	}
	
	public Product read (long productId) throws SQLException {
		if(this.con == null) {
			this.con = DriverManager.getConnection("jdbc:postgresql://java.is.uni-due.de/ws1011", "ws1011", "ftpw10");
		}
		PreparedStatement pstmt = con.prepareStatement("SELECT id, name, price, quantity FROM products WHERE id=?");
		pstmt.setLong(1, productId);
		
		
		ResultSet rs = pstmt.executeQuery();
		if (rs != null && rs.next()) {
			return new Product(rs.getLong(1), rs.getString(2), rs.getDouble(3), rs.getInt(4));
		} else {
			return null;
		}
	}
}
