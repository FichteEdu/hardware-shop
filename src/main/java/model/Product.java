package model;

import javax.persistence.*;

@Entity()
@Table(name="products")
public class Product implements fpt.com.Product {

	private static final long			serialVersionUID	= 1001L;

	//private static final IDGenerator	idgen				= new IDGenerator();

	@Id
	@GeneratedValue (strategy=GenerationType.SEQUENCE, generator="products_SEQ")
	@SequenceGenerator (name="products_SEQ", sequenceName="products_id_seq ", allocationSize=1)
	private long 						id;
	private String						name;
	private double						price;
	private int							quantity;

	// We need this for Beans serialization
	public Product() {
		this(/*0,*/ "", 0, 0);
	}

	// Generate the id only for this type of constructor
//	public Product(	String name, double price, int quantity) {
//		this(/*0,*/ name, price, quantity);
//		try {
//			setId(id);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public Product(	/*long id,*/ String name, double price, int quantity) {
		//setId(id);
		setName(name);
		setPrice(price);
		setQuantity(quantity);
	}

//	public static IDGenerator getIdgen() {
//		return idgen;
//	}

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
