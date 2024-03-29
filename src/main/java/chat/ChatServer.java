package chat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


public class ChatServer extends UnicastRemoteObject implements ChatService {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4770948517004604445L;
	
	private List<String> userlist = new ArrayList<String>();

	protected ChatServer() throws RemoteException {
		super();
	}

	@Override
	public synchronized void login(String s) {
		userlist.add(s);
	}

	@Override
	public synchronized void logout(String s) {
		userlist.remove(s);
	}

	@Override
	public synchronized void send(String s) {
		System.out.println("msg: " + s);
		for(String ts : getUserList()) {
			try {
				ClientService cl = (ClientService) Naming.lookup(ts);
				cl.send(s);
			} catch (MalformedURLException | NotBoundException | RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public synchronized List<String> getUserList() {
		return userlist;
	}

}
