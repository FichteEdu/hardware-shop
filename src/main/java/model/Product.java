package model;

public class Product implements fpt.com.Product {

	private static final long	serialVersionUID	= 1001L;

	private long				id;
	private String				name;
	private double				price;
	private int					quantity;

	// TOCHECK useful?
	public Product() {
		this(0, "", 0, 0);
	}

	public Product(	int id, String name, double price, int quantity) {
		setId(id);
		setName(name);
		setPrice(price);
		setQuantity(quantity);
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public double getPrice() {
		return price;
	}

	@Override
	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public int getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}
