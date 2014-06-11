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

			// Find open cashpoint with fewest customers
			Cashpoint minCashpoint = null;
			for (Cashpoint c : cs) {
				int l = c.length();
				// See if we can append
				if (c.canAdd() && (minCashpoint == null || l < minCashpoint.length()))
					minCashpoint = c;
			}

			// Add to cashpoint's queue
			if (minCashpoint != null) {
				minCashpoint.add(customer);
				int l = minCashpoint.length();

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
