package warehouse;

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
				orders.add(order);
				// Send back to client
				sendOrderThread.send(order);

				// Some console output
				System.out.format("Received new order (%s):%n", socket.getInetAddress());
				printOrder(order);
				System.out.format("---%n");
				printOrders();
				System.out.format("...%n%n");
			}
		} catch (SocketException e0) {
			if (e0.getMessage().equals("Connection reset"))
			System.out.format("Connection closed (%s).%n", socket.getInetAddress());
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
		for (Order order : orders){
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
		System.out.format("Count: %d, Sum: %.2f EUR%n", order.getQuantity(),
				order.getSum());
	}

}


class SendOrderThread extends Thread {

	LinkedBlockingQueue<Order>	queue	= new LinkedBlockingQueue<>();
	private Socket				socket;

	public SendOrderThread(	Socket socket) {
		this.socket = socket;
	}

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
