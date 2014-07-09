package problem4;

public class Main {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws InterruptedException {
		// Array to store all cashpoints
		Cashpoint[] cs = new Cashpoint[6];
		Balance balance = new Balance();

		// Create cashpoints
		for (int i = 0; i < cs.length; i++) {
			cs[i] = new Cashpoint("Cashpoint " + (i + 1), balance);
			cs[i].start();
		}

		// Open one cashpoint initially
		cs[0].open();

		// Create acquisition
		Thread aqThread = new Acquisition("Acquisition", cs, 6, 8);
		aqThread.start();

		// Now we wait for all cashpoints to close
		boolean someOpen;
		do {
			Thread.sleep(100);

			someOpen = false;
			for (Cashpoint c : cs) {
				someOpen |= !c.isClosed();
			}
		} while (someOpen);

		System.out.println("All cashpoints are closed. Exiting...");
		// With the default values there should be no casualties, but check anyway

		// Stop threads
		// Thread.stop() is deprecated but suits our needs just perfectly here
		if (aqThread.isAlive()) {
			System.out.println("WARNING! Cashpoints are closed despite Acquisition still running");
			aqThread.stop();
		}
		for (Cashpoint c : cs) {
			// In any case, let cashpoints finish their stuff
			synchronized (c) {
				while (!c.isClosed()) {
					Thread.sleep(100);
				}
				c.stop();
			}
		}

	}

}
