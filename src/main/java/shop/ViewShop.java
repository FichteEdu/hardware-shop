package shop;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import shop.view.ListProductRenderer;
import fpt.com.Product;
import fpt.com.ProductList;


@SuppressWarnings("serial")
public class ViewShop extends JFrame implements Observer {

	private JList<Product>		productJList;
	private ProductList			plist;

	private TextField			tfName;
	private JFormattedTextField	ftfPrice;
	private JFormattedTextField	ftfQuantity;
	
	private JMenuBar			jMenuBar;
	private JMenu				serStrat;
	private JMenuItem			binarySer;
	private JMenuItem			beansSer;
	private JMenuItem			xstreamSer;

	public ViewShop() {
		setTitle("ViewShop");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(450, 300);

		productJList = new JList<Product>();
		productJList.setCellRenderer(new ListProductRenderer());
		getContentPane().add(new JScrollPane(productJList), BorderLayout.CENTER);

		JPanel sidePanel = new JPanel();
		getContentPane().add(sidePanel, BorderLayout.EAST);
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

		// Create text fields and formatters
		tfName = new TextField();

		NumberFormat fmt = NumberFormat.getNumberInstance();
		fmt.setMinimumFractionDigits(2);
		fmt.setMaximumFractionDigits(2);
		ftfPrice = new JFormattedTextField(fmt);

		fmt = NumberFormat.getNumberInstance();
		fmt.setMaximumFractionDigits(0);
		ftfQuantity = new JFormattedTextField(fmt);

		// Create input boxes
		sidePanel.add(wrapTextField(tfName, "Name"));
		sidePanel.add(wrapTextField(ftfPrice, "Price"));
		sidePanel.add(wrapTextField(ftfQuantity, "Quantity"));

		// Buttons panel
		Box buttons = Box.createHorizontalBox();
		buttons.add(new JButton("Add"));
		buttons.add(new JButton("Delete (selected)"));
		sidePanel.add(buttons);
		
		// JMenuBar
		jMenuBar = new JMenuBar();
		serStrat = new JMenu("Serialization Strategy");
		binarySer = new JMenuItem("binary");
		beansSer = new JMenuItem("beans");
		xstreamSer = new JMenuItem("xstream");
		
		serStrat.add(binarySer);
		serStrat.add(beansSer);
		serStrat.add(xstreamSer);
		jMenuBar.add(serStrat);
		setJMenuBar(jMenuBar);
		
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
			if (child instanceof Container)
				addActionListener((Container) child, al);
			if (child instanceof JButton) {
				((JButton) child).addActionListener(al);
			}
		}
	}

	/**
	 * Set the ProductList to display and work on.
	 * 
	 * @param plist
	 *            ^
	 */
	public void setModel(ProductList plist) {
		this.plist = plist;
		productJList.setListData(toArray());
	}

	public int getSelectedIndex() {
		return productJList.getSelectedIndex();
	}

	/**
	 * Return the currently selected product in the table.
	 * 
	 * @return ^
	 */
	public Product getSelected() {
		return getProduct(productJList.getSelectedIndex());
	}

	@Override
	public void update(Observable o, Object arg) {
		productJList.setListData(toArray());
	}

	/**
	 * Get the product at position i in the ProductList.
	 * 
	 * @param i
	 * @return ^
	 */
	private Product getProduct(int i) {
		Iterator<Product> it = plist.iterator();

		// Get the product at position X
		for (int j = 0; it.hasNext(); it.next(), j++)
			if (j == i)
				return it.next();

		return null;
	}

	/**
	 * Convert the saved ProductList to an array for JList
	 * 
	 * @return ^
	 */
	private Product[] toArray() {
		Product[] arr = new Product[plist.size()];
		Iterator<Product> it = plist.iterator();
		int i = 0;
		while (it.hasNext())
			arr[i++] = it.next();
		return arr;
	}

	private Component wrapTextField(Component c, String title) {
		Box box = Box.createHorizontalBox();
		box.add(c);
		box.setBorder(BorderFactory.createTitledBorder(title));
		return box;
	}

	public Product getNewProduct() {
		String name = tfName.getText();
		if (name.isEmpty())
			return null;
		
		double price = ((Number) ftfPrice.getValue()).doubleValue();
		int quantity = ((Number) ftfQuantity.getValue()).intValue();
		// TODO Auto-generated method stub
		return new model.Product(name, price, quantity);
	}
}
