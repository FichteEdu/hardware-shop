package customer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import chat.Client;
import shop.ModelShop;
import customer.view.QuantityEvent;
import customer.view.QuantityListener;
import fpt.com.Order;
import fpt.com.Product;


public class ControllerCustomer implements ActionListener, QuantityListener {

	private ViewCustomer	v;
	private ModelCustomer	m;
	private Order			currentOrder;
	private Socket			socket;
	private SendOrderThread	sendOrderThread;

	public ControllerCustomer() {
		try {
			socket = new Socket("localhost", 6666);
		} catch (IOException e1) {
			System.out.println("Unable to connect to Warehouse. Please start the Warehouse first.");
			System.exit(1);
		}

		// Start IO threads
		new ReceiveOrderThread().start();
		sendOrderThread = new SendOrderThread();
		sendOrderThread.start();
	}

	public ControllerCustomer(	ModelShop ms, ModelCustomer m, ViewCustomer v) {
		this();
		link(ms, m, v);
	}

	public void link(ModelShop ms, ModelCustomer m, ViewCustomer v) {
		this.v = v;
		this.m = m;
		v.setModels(m, ms);
		// When "Buy" was pressed
		v.addActionListener(this);
		// When a quantity cell was edited
		v.addQuantityListener(this);

		m.newOrders();
		newOrder();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();

		switch (btn.getText()) {
			case "Buy":
				// Ask for credentials
				String user = JOptionPane.showInputDialog("Enter username:");
				if (user.isEmpty())
					return;

				String pass = JOptionPane.showInputDialog("Enter password:");
				if (pass.isEmpty())
					return;

				Order order = currentOrder;
				// By changing the references prior to sending it we can
				// make sure that no data is missing.
				newOrder();
				Object[] data = { user, pass, order };
				// Credentials are checked on the server. If they are wrong the
				// order is lost, but who cares (I currently do not).
				sendOrderThread.send(data);
				break;
			case "Support Chat":
				try {
					Client.open();
				} catch (RemoteException
						| MalformedURLException
						| NotBoundException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error opening chat", null,
							JOptionPane.ERROR_MESSAGE);
				}
				break;
		}
	}

	@Override
	public void quantityChanged(QuantityEvent e) {

		Product p = e.getProduct();
		Product op = currentOrder.findProductById(p.getId());

		// Limit to maximum available
		int quantity = Math.min(e.getNewQuantity(), p.getQuantity());

		// Remove product from order if below 1 (and in the order)
		// Since this is not supported by the interface we just cast to out
		// model version.
		if (quantity < 1) {
			if (op != null)
				((model.Order) currentOrder).remove(op);
			return;
		}
		// Add product to order if necessary
		if (op == null) {
			op = model.Product.clone(p);
			currentOrder.add(op);
		}
		op.setQuantity(quantity);
		m.changed();
	}

	private void newOrder() {
		ArrayList<Order> orders = m.getOrders();

		currentOrder = new model.Order();
		v.setCurrentOrder(currentOrder);
		if (orders.size() > 0)
			orders.set(0, currentOrder);
		else
			orders.add(0, currentOrder);
		m.changed();
	}

	class ReceiveOrderThread extends Thread {

		@Override
		public void run() {
			try (@SuppressWarnings("resource")
			ObjectInputStream oin = new ObjectInputStream(socket.getInputStream());) {
				while (!socket.isClosed()) {
					Object input = oin.readObject();

					// Process our input
					if (input instanceof Order) {
						Order order = (Order) input;
						m.getOrders().add(1, order);
						m.changed();
					} else if (input instanceof String) {
						String msg = (String) input;
						JOptionPane.showMessageDialog(null, msg + "\n\nYour order has been lost.",
								"Error submitting order", JOptionPane.ERROR_MESSAGE);
					} else {
						System.out.println("WARNING: Received unknown Object!");
						return;
					}
				}
			} catch (SocketException e) {
				if (e.getMessage().equals("Connection reset")) {
					System.out.println("Connection to Warehouse closed.");
					System.exit(2);
				}
			} catch (EOFException e) {
				System.out.println("Connection to Warehouse closed.");
				System.exit(3);
			} catch (IOException
					| ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(3);
			}
		}
	}

	class SendOrderThread extends Thread {

		LinkedBlockingQueue<Object[]>	queue	= new LinkedBlockingQueue<>();

		@Override
		public void run() {
			try (ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());) {

				Object[] data = null;
				while (!socket.isClosed()) {
					if (null != (data = queue.poll())) {
						oout.writeObject(data);
						oout.flush();
					} else {
						// Wait for orders to write
						try {
							sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Unable to send order.");
			} finally {
				try {
					socket.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}

		public void send(Object[] data) {
			queue.add(data);
		}
	}
}
