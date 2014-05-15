package customer;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import model.Order;
import customer.view.OrderRenderer;
import customer.view.ProductListTableModel;
import fpt.com.Product;
import fpt.com.ProductList;


public class ViewCustomer extends JFrame implements Observer {

	private static final long		serialVersionUID	= 8575042175879061593L;

	private JList<Order>			orderJList;
	private OrderRenderer			orderRenderer;

	private ProductListTableModel	tableModel;
	private JTable					table;

	public ViewCustomer() {
		setTitle("ViewCustomer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 500);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		setLocation(500, 0);

		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

		orderJList = new JList<Order>();
		orderRenderer = new OrderRenderer();
		orderJList.setCellRenderer(orderRenderer);
		add(new JScrollPane(orderJList));

		// Create table and set options
		table = new JTable();
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
		// table.getTableHeader().setReorderingAllowed(false);
		tableModel = new ProductListTableModel();
		table.setModel(tableModel);
		right.add(new JScrollPane(table));
		right.add(new JButton("Buy"));
		add(right);
		
		// set test data for jlist

		Order order = new model.Order();
		order.add(new model.Product("test", 2.2, 12));
		order.add(new model.Product("test2", 12.2, 12));
		Order[] array = { order };
		orderJList.setListData(array);
	}

	public void setModel(ProductList m) {
		tableModel.setPlist(m);
	}

	@Override
	public void update(Observable o, Object arg) {
		tableModel.fireTableDataChanged();
	}


}
