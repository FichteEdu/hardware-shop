package time;

import java.io.*;
import java.net.*;


public class TimeServer extends Thread {

	public static void main(String[] args) {
		new TimeServer().run();
	}

	@Override
	public void run() {

		try (DatagramSocket socket = new DatagramSocket(6667)) {

			System.out.println("Time sever running");
			while (true) {

				DatagramPacket packet = new DatagramPacket(new byte[4], 4);

				try {
					socket.receive(packet);
					if (new String(packet.getData(), "UTF-8").equals("time")) {
						System.out.println("Time request from " + packet.getAddress() + ":"
								+ packet.getPort());
						new ServerResponseThread(packet, socket).start();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
