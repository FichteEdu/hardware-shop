package model;

public class Product implements fpt.com.Product {

    private static final long serialVersionUID = 1001L;

    private long              id;
    private double            price;
    private int               quantity;
    private String            name;

    // TOCHECK useful?
    public Product() {
        this(0, 0, 0, "");
    }

    public Product(int id, int price, int quantity, String name) {
        setId(id);
        setPrice(price);
        setQuantity(quantity);
        setName(name);
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
