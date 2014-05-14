package shop;

import java.util.Iterator;
import java.util.Observable;

import model.ProductList;
import fpt.com.Product;


public class ModelShop extends Observable implements fpt.com.ProductList {

	private static final long	serialVersionUID	= 4001L;
	private ProductList			plist				= new ProductList();
	private byte				strat;										//0=bin, 1=beans, 2=xstream

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
	
	public byte getStrat() {
		return this.strat;
	}
	
	public void setStrat(byte strat) {
		this.strat = strat;
	}

	/**
	 * Signal that we changed something and notify our observers.
	 */
	private void changed() {
		setChanged();
		notifyObservers();
	}
	
	public ProductList getProductList(){
		return plist;
	}
	
}
