package customer;

import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import model.Order;
import shop.ModelShop;
import customer.view.OrderRenderer;
import customer.view.TableProductRenderer;


public class ViewCustomer extends JFrame implements Observer {

	private static final long		serialVersionUID	= 8575042175879061593L;

	private model.ProductList		plist;
	private JPanel					right;
	private JTable					ProductTable;

	private JList<Order>			cartJList;

	private TableProductRenderer	tr					= new TableProductRenderer();

	// TOCHECK sinnvoll?
	public ViewCustomer() {
	}

	public ViewCustomer(ModelShop m) {
		setTitle("ViewCustomer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 500);
		setResizable(false);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		setLocation(500, 0);
		
		plist = m.getProductList();
		right = new JPanel();
		new BoxLayout(right, BoxLayout.Y_AXIS);

		cartJList = new JList<Order>();
		cartJList.setCellRenderer(new OrderRenderer());
		add(new JScrollPane(cartJList));

		ProductTable = tr.ProductTable(plist);
		right.add(new JScrollPane(ProductTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
		right.add(new JButton("Buy"));
		add(right);
	}

	// TODO
	@Override
	public void update(Observable o, Object arg) {
		// ProductTable.updateUI();
	}

}
