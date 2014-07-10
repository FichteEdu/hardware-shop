package time;

import java.io.*;
import java.net.*;

public class Server extends Thread {
	
	public void run() {
		
		try (DatagramSocket socket = new DatagramSocket(6667)) {
			while (true) {
				
				DatagramPacket packet = new DatagramPacket(new byte[4], 4);
				
				try {
					socket.receive(packet);
					if (new String(packet.getData(), "UTF-8").equals("time")) {
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