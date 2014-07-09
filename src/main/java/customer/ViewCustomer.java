package customer;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import shop.ModelShop;
import customer.view.OrderRenderer;
import customer.view.ProductListTableModel;
import customer.view.QuantityListener;
import fpt.com.Order;
import fpt.com.Product;


public class ViewCustomer extends JFrame {

	private static final long		serialVersionUID	= 8575042175879061593L;

	private JList<Order>			orderJList;
	private OrderRenderer			orderRenderer;

	private ProductListTableModel	tableModel;
	private JTable					table;

	private ModelCustomer			m;
	private Order					currentOrder	= null;
	
	private JButton btnBuy;
	
	Observer plistObserver = new Observer() {
		@Override
		public void update(Observable o, Object arg) {
			tableModel.fireTableDataChanged();
		}
	};
	
	Observer orderObserver = new Observer() {
		@Override
		public void update(Observable o, Object arg) {
			ArrayList<? extends Order> orders = m.getOrders();
			orderJList.setListData(orders.toArray(new Order[]{}));
			tableModel.setOrder(orders.size() > 0 ? orders.get(0) : null);
			tableModel.fireTableDataChanged();
		}
	};

	public ViewCustomer() {
		setTitle("ViewCustomer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 500);
		setLocation(500, 0);

		setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

		orderJList = new JList<Order>();
		orderJList.setPreferredSize(new Dimension(200, 0));
		orderRenderer = new OrderRenderer();
		orderJList.setCellRenderer(orderRenderer);
		add(new JScrollPane(orderJList));

		// Create table and set options
		table = new JTable();
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
		tableModel = new ProductListTableModel();
		table.setModel(tableModel);
		right.add(new JScrollPane(table));
		btnBuy = new JButton("Buy");
		right.add(btnBuy);
		add(right);
	}

	public void setModels(ModelCustomer m, ModelShop ms) {
		this.m = m;
		ms.addObserver(plistObserver);
		m.addObserver(orderObserver);

		tableModel.setPlist(ms);
		tableModel.setOrder(currentOrder);
	}

	public void addQuantityListener(QuantityListener listener) {
		tableModel.addQuantityListener(listener);
	}

	public void addActionListener(ActionListener listener) {
		btnBuy.addActionListener(listener);
	}

	/**
	 * Get the product at position i in the ProductList.
	 */
	public Product getProduct(int i) {
		return tableModel.getProduct(i);
	}

	public int getEditingRow() {
		return table.convertRowIndexToModel(table.getEditingRow());
	}

}
