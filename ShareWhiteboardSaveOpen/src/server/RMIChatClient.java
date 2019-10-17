package server;

import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Button;
import java.awt.Label;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.swing.JFrame;

import java.io.IOException;

public class RMIChatClient extends RMICollaboratorImpl implements java.awt.event.ActionListener {
	TextArea chatArea;
	TextField chatInput;
	public RMIChatClient(String name, String host, String mname) throws RemoteException {
		super(name);
		Properties p = new Properties();
		p.put("host", host);
		p.put("mediatorName", mname);
		connect(p);
		initGraphics();
	}
	public boolean notify(String tag, String msg, Identity src) throws IOException, RemoteException {
		// Print the message in the chat area.
		chatArea.append("\n" + src.getName() + ": " + msg);
		return true;
	}
	
	protected void initGraphics() throws RemoteException {
		JFrame f = new JFrame();
		f.setLayout(null);
		f.addNotify();
		f.setSize(f.getInsets().left + 405, f.getInsets().top + 324);
		chatArea = new java.awt.TextArea();
		chatArea.setBounds(f.getInsets().left, f.getInsets().top,405, 300);
		f.add(chatArea);
		chatInput = new java.awt.TextField();
		chatInput.setBounds(f.getInsets().left + 84,
		f.getInsets().top + 300,264,24);
		f.add(chatInput);
		Button button = new java.awt.Button("Send");
		button.setBounds(f.getInsets().left + 348,
		f.getInsets().top + 300,60,24);
		f.add(button);
		button.addActionListener(this);
		Label label = new java.awt.Label("Chat here:");
		label.setBounds(f.getInsets().left,f.getInsets().top + 300,84,24);
		label.setAlignment(label.RIGHT);
		f.add(label);
		f.setTitle("RMI Chat Client");
		f.setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		// See if there's something to say...
		String msg = chatInput.getText();
		if (msg.length() > 0) {
			try {
				// Broadcast our message to the rest of the chat clients
				boolean success = broadcast("chat", msg);
				if (success) {
					System.out.println("Sent message OK.");
				}
				else {
					System.out.println("Failed to send message.");
				}
				// Clear the chat input field
				chatInput.setText("");
			}
			catch (Exception exc) {
			}
		}

	}
}