package model;

import java.util.Iterator;
import java.util.Observable;

import fpt.com.Product;


public class ModelShop extends Observable implements fpt.com.ProductList {

    private static final long serialVersionUID = 4001L;
    private ProductList       plist            = new ProductList();

    @Override
    public Iterator<Product> iterator() {
        return plist.iterator();
    }

    @Override
    public boolean add(Product p) {
        boolean ret = plist.add(p);
        changed();
        return ret;
    }

    @Override
    public boolean delete(Product p) {
        boolean ret = plist.delete(p);
        changed();
        return ret;
    }

    @Override
    public int size() {
        return plist.size();
    }

    @Override
    public Product findProductById(long id) {
        return plist.findProductById(id);
    }

    @Override
    public Product findProductByName(String name) {
        return plist.findProductByName(name);
    }

    private void changed() {
        hasChanged();
        notifyObservers();
    }

}
