package problem4;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Main {

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<Customer> q = new LinkedBlockingQueue<>();
		new Thread(new Acquisition(q)).start();
		Thread.sleep(500);
		new Thread(new Cashpoint(q)).start();
	}

}
