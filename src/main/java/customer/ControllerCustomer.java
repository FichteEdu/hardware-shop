package customer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

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
		// TODO auth
		// By saving changing the order references prior to sending it we can
		// make sure that no data is missing.
		Order order = currentOrder;
		newOrder();
		sendOrderThread.send(order);
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
					// Assure we have an order here
					if (!(input instanceof Order)) {
						System.out.println("WARNING: Received object that is NOT an Order!");
						return;
					}

					// Process our input
					Order order = (Order) input;
					m.getOrders().add(1, order);
					m.changed();
				}
			} catch (IOException
					| ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(2);
			}
		}
	}

	class SendOrderThread extends Thread {

		LinkedBlockingQueue<Order>	queue	= new LinkedBlockingQueue<>();

		@Override
		public void run() {
			try (ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());) {

				Order order = null;
				while (!socket.isClosed()) {
					if (null != (order = queue.poll())) {
						oout.writeObject(order);
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

		public void send(Order order) {
			queue.add(order);
		}
	}
}
