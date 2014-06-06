package problem4;

import java.util.concurrent.BlockingQueue;


public class Cashpoint implements Runnable {

	private BlockingQueue<Customer>	q;

	public Cashpoint(	BlockingQueue<Customer> q) {
		this.q = q;
	}

	@Override
	public void run() {
		while (q.size() != 0) {
			// Process a customer
			Customer c = q.poll();
			System.out.format("Start processing customer %s %n", c);
			
			// Wait between 0 and 2 seconds
			try {
				Thread.sleep((long) (6000 + 1000 * 4 * Math.random()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.format("Finished processing customer %s %n", c);
		}
		
		System.out.println("Queue is empty! Terminating...");
	}

}
