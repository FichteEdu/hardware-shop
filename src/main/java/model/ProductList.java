package model;

import java.util.ArrayList;

import fpt.com.Product;


public class ProductList extends ArrayList<Product> implements fpt.com.ProductList {

    private static final long serialVersionUID = 2001L;

    @Override
    public Product findProductById(long id) {
        for (Product p : this) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public Product findProductByName(String name) {
        for (Product p : this) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean delete(Product p) {
        return this.remove(p);
    }

}
