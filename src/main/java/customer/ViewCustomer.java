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

import model.Order;
import customer.view.OrderRenderer;
import customer.view.TableProductRenderer;


public class ViewCustomer extends JFrame implements Observer {

	private static final long		serialVersionUID	= 8575042175879061593L;

	private JList<Order>			cartJList;

	private TableProductRenderer	tr					= new TableProductRenderer();

	public ViewCustomer(fpt.com.ProductList m) {
		setTitle("ViewCustomer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 500);
		setResizable(false);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		setLocation(500, 0);

		JPanel right = new JPanel();
		new BoxLayout(right, BoxLayout.Y_AXIS);

		cartJList = new JList<Order>();
		cartJList.setCellRenderer(new OrderRenderer());
		add(new JScrollPane(cartJList));

		JTable productTable = tr.createProductTable(m);
		right.add(new JScrollPane(productTable));
		right.add(new JButton("Buy"));
		add(right);
	}

	@Override
	public void update(Observable o, Object arg) {
		tr.refill();
	}

}
