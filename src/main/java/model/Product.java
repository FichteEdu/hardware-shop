package model;

public class Product implements fpt.com.Product {

	private static final long			serialVersionUID	= 1001L;

	// This ID generator will not save states between restarts of the program
	// and its state is not loaded if you load a stored productlist.
	// There are ways to circumvent this, but the exercises didn't say anything
	// about it, so we're taking it easy.
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
