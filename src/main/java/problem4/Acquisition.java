package problem4;

import java.util.concurrent.BlockingQueue;


public class Acquisition implements Runnable {

	private BlockingQueue<Customer>	q;

	public Acquisition(	BlockingQueue<Customer> q) {
		this.q = q;
	}

	@Override
	public void run() {
		int i = 0;
		while (q.size() <= 8) {
			// Add a customer to the queue
			Customer c = new Customer("C" + i++);
			System.out.format("Adding customer to the queue: %s%n", c);
			q.add(c);

			// Wait between 0 and 2 seconds
			try {
				Thread.sleep((long) (1000 * 2 * Math.random()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Queue is full! Terminating...");
	}

}
