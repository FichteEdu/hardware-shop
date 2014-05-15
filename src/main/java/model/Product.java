package model;

public class Product implements fpt.com.Product {

	private static final long			serialVersionUID	= 1001L;

	private static final IDGenerator	idgen				= new IDGenerator();

	private long						id;
	private String						name;
	private double						price;
	private int							quantity;

	// We need this for Beans serialization
	public Product() {
		this("", 0, 0);
	}

	public Product(	String name, double price, int quantity) {
		try {
			setId(idgen.generate());
		} catch (Exception e) {
			e.printStackTrace();
			setId(0);
		}
		setName(name);
		setPrice(price);
		setQuantity(quantity);
	}

	public static IDGenerator getIdgen() {
		return idgen;
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
		this.quantity = Math.abs(quantity);
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
