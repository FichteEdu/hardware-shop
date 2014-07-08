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

	QuantityListener			quantityListener	= null;

	private static Object[][]	columns				= { { "Name", String.class },
			{ "Price", Double.class }, { "Available", Integer.class },
			{ "OrderCount", Integer.class }		};

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
				return p.getQuantity();
			case 3:
				// Ordercount, if any
				Product op = order != null ? order.findProductById(p.getId()) : null;
				return op != null ? op.getQuantity() : 0;
			default:
				System.out.println("Invalid column number");
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return col == 3;
	}

	/**
	 * Get the product at position i in {@code plist} using an iterator.
	 * 
	 * @param i
	 * @return The Product at that position or null.
	 */
	public Product getProduct(int i) {
		Iterator<Product> it = plist.iterator();

		// Get the product at position X
		for (int j = 0; it.hasNext(); it.next(), j++)
			if (j == i)
				return it.next();

		return null;
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		// Cell has been edited (we only allow this for the quantity)
		int quantity;
		try {
			quantity = Integer.valueOf(aValue.toString());
		} catch (NumberFormatException ex) {
			return;
		}
		if (quantityListener != null)
			quantityListener.quantityChanged(new QuantityEvent(this, getProduct(row), quantity));
	}

	public void addQuantityListener(QuantityListener listener) {
		quantityListener = listener;
	}

}
