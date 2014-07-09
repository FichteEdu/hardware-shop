package chat;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ClientService extends Remote {
	void send (String s) throws RemoteException;
	String getName() throws RemoteException;
}
