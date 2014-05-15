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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import model.serialization.BinaryStrategy;
import model.serialization.XMLStrategy;
import model.serialization.XStreamStrategy;
import shop.view.ListProductRenderer;
import fpt.com.Product;
import fpt.com.ProductList;
import fpt.com.SerializableStrategy;


@SuppressWarnings("serial")
public class ViewShop extends JFrame implements Observer {

	private JList<Product>			productJList;
	private ProductList				plist;

	private TextField				tfName;
	private JFormattedTextField		ftfPrice;
	private JFormattedTextField		ftfQuantity;

	private JRadioButtonMenuItem	binarySer;
	private JRadioButtonMenuItem	beansSer;
	private JRadioButtonMenuItem	xstreamSer;
	private JMenuItem				loadSer;
	private JMenuItem				saveSer;

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
		tfName = new TextField("");

		NumberFormat fmt = NumberFormat.getNumberInstance();
		fmt.setMinimumFractionDigits(2);
		fmt.setMaximumFractionDigits(2);
		ftfPrice = new JFormattedTextField(fmt);
		ftfPrice.setValue(0);

		fmt = NumberFormat.getNumberInstance();
		fmt.setMaximumFractionDigits(0);
		ftfQuantity = new JFormattedTextField(fmt);
		ftfQuantity.setValue(0);

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
		JMenuBar jMenuBar = new JMenuBar();
		JMenu serStrat = new JMenu("Serialization");
		binarySer = new JRadioButtonMenuItem("Bin", true);
		beansSer = new JRadioButtonMenuItem("beans");
		xstreamSer = new JRadioButtonMenuItem("xstream");

		// Button group for serialization startegy
		ButtonGroup serializeGroup = new ButtonGroup();
		serializeGroup.add(beansSer);
		serializeGroup.add(binarySer);
		serializeGroup.add(xstreamSer);

		serStrat.add(binarySer);
		serStrat.add(beansSer);
		serStrat.add(xstreamSer);
		jMenuBar.add(serStrat);

		JMenu loadSaveStrat = new JMenu("Load/Save");
		loadSer = new JMenuItem("load");
		saveSer = new JMenuItem("save");

		loadSaveStrat.add(loadSer);
		loadSaveStrat.add(saveSer);

		jMenuBar.add(loadSaveStrat);
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
			if (child instanceof Container) {
				addActionListener((Container) child, al);
			}
			// JMenu's popups are not included in getComponents, so we do this
			// too.
			// http://bugs.java.com/bugdatabase/view_bug.do;jsessionid=a629cf8a493f96b514e074842711?bug_id=4146596
			if (child instanceof JMenu) {
				JMenu menu = (JMenu) child;
				for (int i = 0; i < menu.getItemCount(); i++) {
					menu.getItem(i).addActionListener(al);
				}
			}
			if (child instanceof JButton) {
				((JButton) child).addActionListener(al);
			}
			if (child instanceof JMenuItem) {
				((JMenuItem) child).addActionListener(al);
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
	 * Get the currently selected products in the table.
	 * 
	 * @return ^
	 */
	public Product[] getSelected() {
		int[] sel = productJList.getSelectedIndices();
		Product[] ret = new Product[sel.length];

		for (int i = 0; i < sel.length; i++) {
			ret[i] = getProduct(sel[i]);
		}
		return ret;
	}

	/**
	 * Get a newly created product constructed from values in the input
	 * 
	 * @return
	 */
	public Product getNewProduct() {
		String name = tfName.getText();
		if (name.isEmpty())
			return null;

		double price = ((Number) ftfPrice.getValue()).doubleValue();
		int quantity = ((Number) ftfQuantity.getValue()).intValue();
		return new model.Product(name, price, quantity);
	}

	/**
	 * Get the selected serializable strategy (from menu radios)
	 * 
	 * @return ^
	 */
	public SerializableStrategy getSelectedStrat() {
		if (binarySer.isSelected())
			return new BinaryStrategy();
		if (beansSer.isSelected())
			return new XMLStrategy();
		else
			return new XStreamStrategy();
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

		// Get the product at position i
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
}
