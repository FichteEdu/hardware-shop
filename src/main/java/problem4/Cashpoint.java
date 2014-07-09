package problem4;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Cashpoint extends Thread {

	private static final Random		rand	= new Random();

	private Balance					balance;

	private BlockingQueue<Customer>	q;

	private boolean					open	= false;
	private boolean					opening	= false;

	public Cashpoint(	String name, Balance balance) {
		super(name);
		// We don't really need to use a blocking queue here, but it's an
		// implementation that works
		this.q = new LinkedBlockingQueue<>();
		this.balance = balance;
	}

	@Override
	public void run() {
		try {
			// Idling~
			while (true) {
				// Prepare to open
				if (opening) {
					// Wait 6 seconds
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					out("Finished opening");
					opening = false;
					open = true;
				}
				// Wait until cashpoint is open
				else if (!isOpen()) {
					sleep(100);
					continue;
				}

				// Start processing customers
				Customer c;
				while (true) {
					// Make sure that we close the cashpoint before more
					// customers can be added
					synchronized (this) {
						if (q.size() == 0) {
							out("Queue is empty! Closing...");
							open = false;
							break;
						}
						c = q.poll();
					}

					// Process a customer
					out("Start processing customer %s; %d still in queue", c, length());

					// Wait between 6 and 10 seconds
					Thread.sleep((long) (6000 + 1000 * 4 * Math.random()));

					// Add a random amount between 0 and 50 (with two
					// decimal digits) to the balance
					balance.increase(this, (rand.nextInt(5000) + 1) / 100d);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the cashpoint.
	 */
	public synchronized void open() {
		if (!isClosed())
			return;
		out("Opening...");
		opening = true;
	}

	public synchronized boolean isOpen() {
		return open;
	}

	/**
	 * Adds a customer to the cashpoint's queue.
	 * 
	 * @param c
	 */
	public synchronized void add(Customer c) {
		if (!canAdd())
			throw new RuntimeException("Cashpoint is closed");
		q.add(c);
		out("Customer %s added to queue; now %d in queue", c, length());
	}

	/**
	 * Whether the cashpoint is closed (note that this is different from
	 * !isOpen()).
	 */
	public synchronized boolean isClosed() {
		return !(isOpen() || opening);
	}

	/**
	 * Whether customerscan be added to the queue.
	 */
	public synchronized boolean canAdd() {
		return !isClosed();
	}

	/**
	 * Get the lenght of the queue this cashpoint processes.
	 */
	public synchronized int length() {
		return q.size();
	}

	private void out(String msg, Object... args) {
		System.out.format(String.format("%s: %s%n", getName(), msg), args);
	}
}
