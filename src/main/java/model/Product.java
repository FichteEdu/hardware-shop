package model;

import javax.persistence.*;


@Entity()
@Table(name = "products")
public class Product implements fpt.com.Product {

	private static final long	serialVersionUID	= 1001L;

	// private static final IDGenerator idgen = new IDGenerator();

	private long				id;
	private String				name;
	private double				price;
	private int					quantity;

	// We need this for Beans & OpenJPA serialization
	public Product() {
		this(0, "", 0, 0);
	}

	// Generate the id only for this type of constructor
	// public Product( String name, double price, int quantity) {
	// this(/*0,*/ name, price, quantity);
	// try {
	// setId(id);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public Product(	long id, String name, double price, int quantity) {
		setId(id);
		setName(name);
		setPrice(price);
		setQuantity(quantity);
	}

	public Product(	String name, double price, int quantity) {
		setName(name);
		setPrice(price);
		setQuantity(quantity);
	}

	// public static IDGenerator getIdgen() {
	// return idgen;
	// }

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "products_SEQ")
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	@Column(name = "price")
	public double getPrice() {
		return price;
	}

	@Override
	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	@Column(name = "quantity")
	public int getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(int quantity) {
		this.quantity = Math.abs(quantity);
	}

	@Override
	@Column(name = "name")
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}
