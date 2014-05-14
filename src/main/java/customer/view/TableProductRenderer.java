package customer.view;

import javax.swing.JTable;

import fpt.com.ProductList;
import fpt.com.Product;


// TODO make this into a TableModel (because currently it doesn't work!)
public class TableProductRenderer {

	private Object[][]		data	= { {"", "" ,"" ,""} };
	private static String[]	title	= { "Name", "Price", "MaxCount", "OrderCount" };
	private JTable			table;
	private ProductList		plist;

	public JTable createProductTable(ProductList plist) {
		this.plist = plist;

		// Create (initially empty) table and set options
		table = new JTable(data, title);
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
		table.setEditingColumn(3);
		table.getTableHeader().setReorderingAllowed(false);

		// Fill the table
		refill();

		return table;
	}

	public void refill() {
		int row = 0;
		Object[] data = new Object[4];

		for (Product p : plist) {
			data[0] = p.getName();
			data[1] = p.getPrice();
			data[2] = p.getQuantity();
			data[3] = 0;
			for (int i = 0; i < 4; ++i)
				table.setValueAt(data, row, i);
		}
	}

}
