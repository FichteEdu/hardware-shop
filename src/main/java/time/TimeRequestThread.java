package time;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class TimeRequestThread extends Thread {

	private TimeRequester	rq;
	private DatagramSocket	socket;
	private DatagramPacket	packet;
	private byte[]			data;

	public TimeRequestThread(	TimeRequester rq) throws SocketException {

		this.rq = rq;

		socket = new DatagramSocket();

		try {
			data = "time".getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {

		while (true) {
			try {
				packet = new DatagramPacket(data, data.length, InetAddress.getByName("localhost"),
						6667);
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
				rq.setTime(new String(packet.getData(), "UTF-8"));
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
}
