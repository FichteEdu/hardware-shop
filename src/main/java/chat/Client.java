package chat;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Client {

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		
		JFrame jf = new JFrame();
		jf.setLayout(new BoxLayout(jf.getContentPane(), BoxLayout.Y_AXIS)); 
		jf.setTitle("SupportChat");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(450, 300);
		
		JTextArea textArea = new JTextArea(5, 30);
		textArea.setEditable(false);
		textArea.setEnabled(false);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		JTextField textField = new JTextField(20);
		textField.setMaximumSize( new Dimension(textField.getMaximumSize().width, textField.getPreferredSize().height) );
		

		textField.addKeyListener(
			new KeyListener () {

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()){
						// send msg and clear
						ChatService cs;
						try {
							cs = (ChatService) Naming.lookup("server");
							JTextField tf = (JTextField)(e.getComponent());
							cs.send(tf.getText());
							tf.setText("");
						} catch (MalformedURLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (NotBoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			}
		);
		
		jf.add(scrollPane);
		jf.add(textField);
		jf.pack();
		jf.setVisible(true);
		
		ChatService cs = (ChatService) Naming.lookup("server");
		
		List<String> ul = cs.getUserList();
		
		int i = 0;
		while (ul.indexOf("user-" + i) != -1) {
			i++;
		}

		Remote client = new ChatClient("user-" + i, textArea);
		Naming.rebind("user-" + i, client);

		cs.login("user-" + i);
	}

}
