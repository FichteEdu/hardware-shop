package problem4;

public class Acquisition extends Thread {

	private Cashpoint[]	cs;
	private int			maxlen;
	private int			newCashThreshold;

	public Acquisition(	String name, Cashpoint[] cs, int newCashThreshold, int maxlen) {
		super(name);
		this.cs = cs;
		this.newCashThreshold = newCashThreshold;
		this.maxlen = maxlen;
	}

	@Override
	public void run() {
		int i = 0;

		while (true) {
			// "Found" a customer
			Customer customer = new Customer("C" + i++);
			out("Found new customer: %s", customer);

			// Find open cashpoint with fewest customers (this is inaccurate)
			Cashpoint minCashpoint = null;
			int l = 0;
			while (true) {
				for (Cashpoint c : cs) {
					int minl = c.length();
					// See if we can append
					if (c.canAdd() && (minCashpoint == null || minl < minCashpoint.length()))
						minCashpoint = c;
				}
				if (minCashpoint == null)
					break;

				// Make sure the cashpoint doesn't close on us, otherwise
				// we'd get an exception (or lose our customer if we just
				// caught it)
				synchronized (minCashpoint) {
					if (minCashpoint.canAdd()) {
						minCashpoint.add(customer);
						l = minCashpoint.length();
						break;
					}
				}
			}

			// Add to cashpoint's queue
			if (minCashpoint != null) {

				// Open new cashpoint if threshold exceeded
				if (l >= newCashThreshold)
					for (Cashpoint c : cs)
						if (c.isClosed()) {
							c.open();
							break;
						}

				// Check if queue is full and terminate
				if (l >= maxlen) {
					out("%s reached maximum lenght of %d; Terminating...", minCashpoint.getName(),
							maxlen);
					return;
				}
			} else
				out("No Cashpoint open! Abandoning customer...");

			// Wait between 0 and 2 seconds
			try {
				Thread.sleep((long) (1000 * 2 * Math.random()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void out(String msg, Object... args) {
		System.out.format(String.format("%s: %s%n", getName(), msg), args);
	}

}
