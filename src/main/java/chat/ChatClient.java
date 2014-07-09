package chat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JTextArea;


public class ChatClient extends UnicastRemoteObject implements ClientService {

	protected ChatClient(String name, JTextArea ta) throws RemoteException {
		super();
		this.name = name;
		this.textArea = ta;
	}

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5870896030748820006L;
	
	private String name = "";
	private JTextArea textArea;

	@Override
	public void send(String s) {
		System.out.println("msg: " + s);
		textArea.setText(textArea.getText() + s + "\n");
	}

	@Override
	public String getName() {
		return this.name;
	}

}
