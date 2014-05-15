package customer.view;

import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import fpt.com.Order;
import fpt.com.Product;
import fpt.com.ProductList;


@SuppressWarnings("serial")
public class ProductListTableModel extends AbstractTableModel {

	private ProductList			plist;
	private Order				order;

	private static Object[][]	columns	= { { "Name", String.class }, { "Price", Double.class },
			{ "Quantity", Integer.class }, { "OrderCount", Integer.class } };

	public ProductListTableModel() {
	}

	public ProductListTableModel(	Order order, ProductList plist) {
		setOrder(order);
		setPlist(plist);
	}

	/**
	 * Set the table model's ProductList to fetch values from.
	 * 
	 * @param plist
	 */
	public void setPlist(ProductList plist) {
		this.plist = plist;
	}

	public ProductList getPlist() {
		return plist;
	}

	/**
	 * Set the table model's Order to fetch values from.
	 * 
	 * @param order
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	public Order getOrder() {
		return order;
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public String getColumnName(int i) {
		return (String) columns[i][0];
	}

	@Override
	public Class<?> getColumnClass(int i) {
		return (Class<?>) columns[i][1];
	}

	@Override
	public int getRowCount() {
		return plist != null ? plist.size() : 0;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Product p = getProduct(row);

		switch (col) {
			case 0:
				return p.getName();
			case 1:
				return p.getPrice();
			case 2:
				// Available products
				Product pp = plist.findProductById(p.getId());
				return pp.getQuantity();
			case 3:
				return p.getQuantity();
			default:
				System.out.println("Invalid column number");
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		switch (col) {
			case 3:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Get the product at position i in {@code plist} using an iterator.
	 * 
	 * @param i
	 * @return The Product at that position or null.
	 */
	Product getProduct(int i) {
		Iterator<Product> it = plist.iterator();

		// Get the product at position X
		for (int j = 0; it.hasNext(); it.next(), j++)
			if (j == i)
				return it.next();

		return null;
	}

}
