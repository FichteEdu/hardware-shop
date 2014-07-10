package time;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ServerResponseThread extends Thread {

	private DatagramPacket	packet;
	private DatagramSocket	socket;
	private byte[]			date;

	@Override
	public void run() {

		try {
			date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()).toString()
					.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		packet = new DatagramPacket(date, date.length, packet.getAddress(), packet.getPort());

		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ServerResponseThread(DatagramPacket packet, DatagramSocket socket) {
		this.packet = packet;
		this.socket = socket;
	}
}
