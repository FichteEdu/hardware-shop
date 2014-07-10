package customer;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	
	private JLabel timeLabel;
	
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
			tableModel.setCurrentOrder(orders.size() > 0 ? orders.get(0) : null);
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

		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttons.add(new JButton("Buy"));
		buttons.add(new JButton("Support Chat"));
		timeLabel = new JLabel();
		buttons.add(timeLabel);
		right.add(buttons);
		add(right);
	}

	public void setModels(ModelCustomer m, ModelShop ms) {
		this.m = m;
		ms.addObserver(plistObserver);
		m.addObserver(orderObserver);

		tableModel.setPlist(ms);
	}

	public void addQuantityListener(QuantityListener listener) {
		tableModel.addQuantityListener(listener);
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
	
	public void setTime(String time) {
		timeLabel.setText(time);
	}

	public void setCurrentOrder(Order currentOrder) {
		tableModel.setCurrentOrder(currentOrder);
		orderRenderer.setCurrentOrder(currentOrder);
	}
	

	/**
	 * Recursively iterate through all containers and add `action` as action
	 * listener to all buttons.
	 * 
	 * @param action
	 *            The listener to add
	 */
	public void addActionListener(ActionListener al) {
		addActionListener(this, al);
	}

	/**
	 * Recursively iterate through all containers and add `action` as action
	 * listener to all buttons.
	 * 
	 * @param c
	 *            Container to iterate through
	 * @param al
	 *            The listener to add
	 */
	private void addActionListener(Container c, ActionListener al) {
		for (Component child : c.getComponents()) {
			if (child instanceof Container) {
				addActionListener((Container) child, al);
			}
			if (child instanceof JButton) {
				((JButton) child).addActionListener(al);
			}
		}
	}

}
