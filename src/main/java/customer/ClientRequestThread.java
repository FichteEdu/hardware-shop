package customer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;

public class ClientRequestThread extends Thread {
	
	private ControllerCustomer cc;
	private DatagramSocket socket;
	private DatagramPacket packet;
	private byte[] data;
	
	@Override
	public void run() {
		
		while (true) {
			try {
				packet = new DatagramPacket(data, data.length, InetAddress.getByName("localhost"), 6667);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

			try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}

			byte[] answer = new byte[24];
			
			packet = new DatagramPacket(answer, answer.length);

			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				cc.setTime(new String(packet.getData(), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ClientRequestThread(ControllerCustomer cc) {
				
		this.cc = cc;
		
		try {
			socket = new DatagramSocket(6668);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		try {
			data = "time".getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		
	}
}
