package warehouse;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang.StringUtils;

import fpt.com.Order;
import fpt.com.Product;


public class Warehouse {

	public static void main(String[] args) throws IOException {
		ArrayList<Order> orders = new ArrayList<>();
		try (ServerSocket server = new ServerSocket(6666)) {

			System.out.println("Warehouse initialized");
			while (true) {
				@SuppressWarnings("resource")
				Socket socket = server.accept();
				System.out.format("New connection established (%s)%n", socket.getInetAddress());
				SendOrderThread t = new SendOrderThread(socket);
				t.start();
				new ReceiveOrderThread(orders, socket, t).start();
			}
		}
	}

}


class ReceiveOrderThread extends Thread {

	private static final String	USER	= "admin";
	private static final String	PASS	= "admin";

	private Socket				socket;
	private ArrayList<Order>	orders;
	private SendOrderThread		sendOrderThread;

	public ReceiveOrderThread(	ArrayList<Order> orders, Socket socket,
								SendOrderThread sendOrderThread) {
		this.orders = orders;
		this.socket = socket;
		this.sendOrderThread = sendOrderThread;
	}

	@Override
	public void run() {
		try (ObjectInputStream oin = new ObjectInputStream(socket.getInputStream());) {

			while (!socket.isClosed()) {
				Object input = oin.readObject();
				// Assure we have an array here
				if (!(input instanceof Object[])) {
					out("WARNING: Received object that is NOT an Array!");
					continue;
				}
				// With the correct types (signature)
				Object[] array = (Object[]) input;
				if (array.length != 3
						|| !(array[0] instanceof String && array[1] instanceof String && array[2] instanceof Order)) {
					out("WARNING: Received invalid array signature!");
					continue;
				}

				// Check credentials
				String user = (String) array[0], pass = (String) array[1];
				if (!(user.equals(USER) && pass.equals(PASS))) {
					out("Invalid credentials submitted");
					sendOrderThread.send("Invalid credentials");
					continue;
				}

				// Process our input
				Order order = (Order) array[2];
				orders.add(order);
				// Send back to client
				sendOrderThread.send(order);

				// Some console output
				out("Received new order: ");
				printOrder(order);
				System.out.format("---%n");
				printOrders();
				System.out.format("...%n%n");
			}
		} catch (SocketException e0) {
			if (e0.getMessage().equals("Connection reset"))
				out("Connection closed.");
		} catch (EOFException e) {
			out("Connection closed.");
		} catch (IOException
				| ClassNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	void printOrders() {
		int count = 0;
		double sum = 0;
		for (Order order : orders) {
			count += order.getQuantity();
			sum += order.getSum();
		}
		System.out.format("Total count: %d%nTotal price: %.2f EUR%n", count, sum);
	}

	void printOrder(Order order) {
		int width = 30 + 8 + 10 + 4;
		System.out.println(StringUtils.repeat("=", width));
		for (Product p : order)
			System.out.format("%-30s%8d%10.2f EUR%n", p.getName(), p.getQuantity(), p.getQuantity()
					* p.getPrice());
		System.out.println(StringUtils.repeat("=", width));
		System.out.format("Count: %d, Sum: %.2f EUR%n", order.getQuantity(), order.getSum());
	}

	private void out(String msg, Object... args) {
		System.out.format("%s (%s)%n", String.format(msg, args), socket.getInetAddress());
	}
}


class SendOrderThread extends Thread {

	LinkedBlockingQueue<Object>	queue	= new LinkedBlockingQueue<>();
	private Socket				socket;

	public SendOrderThread(	Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());) {

			Object data = null;
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

	public void send(Object data) {
		queue.add(data);
	}
}
