package customer;

import java.util.Iterator;
import java.util.Observable;

import fpt.com.Product;


// TODO this will have to be changed
public class ModelCustomer extends Observable implements fpt.com.ProductList {

	private static final long	serialVersionUID	= 6001L;
	private fpt.com.ProductList	cartlist			= new model.ProductList();

	@Override
	public Iterator<Product> iterator() {
		return cartlist.iterator();
	}

	@Override
	public boolean add(Product p) {
		boolean ret = cartlist.add(p);
		changed();
		return ret;
	}

	@Override
	public boolean delete(Product p) {
		boolean ret = cartlist.delete(p);
		changed();
		return ret;
	}

	@Override
	public int size() {
		return cartlist.size();
	}

	@Override
	public Product findProductById(long id) {
		return cartlist.findProductById(id);
	}

	@Override
	public Product findProductByName(String name) {
		return cartlist.findProductByName(name);
	}

	/**
	 * Signal that we changed something and notify our observers.
	 */
	private void changed() {
		setChanged();
		notifyObservers();
	}

}
