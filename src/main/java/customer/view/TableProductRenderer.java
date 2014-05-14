package customer.view;

import java.util.Observable;

import javax.swing.JTable;

import fpt.com.Product;
import model.ProductList;


public class TableProductRenderer {

	private Object[][]		data;
	private static String[]	title	= { "Name", "Price", "MaxCount", "OrderCount" };
	private JTable			table;
	private ProductList		plist;

	public JTable ProductTable(ProductList plist) {

		this.plist = plist;
		data = new Object[plist.size()][4];
		int i = 0;
		for (Product p : plist) {
			setValueAt(p.getName(), p.getPrice(), p.getQuantity(), 0, i++);
		}
		
		// Create table and set options
		table = new JTable(data, title);
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
		table.setEditingColumn(3);
		table.getTableHeader().setReorderingAllowed(false);
		
		return table;
	}

	public void setValueAt(Object name, Object price, Object maxCount, Object orderCount, int row) {
		data[row][0] = name;
		data[row][1] = price;
		data[row][2] = maxCount;
		data[row][3] = orderCount;
		fireTableRowUpdate(row);
	}

	public void fireTableRowUpdate(int row) {
		for (int i = 0; i < 4; ++i) {
			table.setValueAt(data[row][1], row, i);
		}
	}

	public void update(Observable o, Object arg) {
		int i = 0;
		for (Product p : plist) {
			setValueAt(p.getName(), p.getPrice(), p.getQuantity(), 0, i++);
		}
		i = 0;
	}

}
