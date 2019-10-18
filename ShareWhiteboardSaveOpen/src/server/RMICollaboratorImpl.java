package server;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.IOException; 
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.rmi.Naming; 
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject; 
import java.rmi.RMISecurityManager;

public class RMICollaboratorImpl extends UnicastRemoteObject implements RMICollaborator
{  
	protected Identity id = null;  
	protected RMIMediator mediator = null;
	public RMICollaboratorImpl(String name, String host, String mname)throws RemoteException {    
			id = new Identity(0);    
			id.setName(name);    
			Properties p = new Properties();    
			p.put("host", host);    
			p.put("mediatorName", mname);    
			connect(p);  
	}
	public RMICollaboratorImpl(String name) throws RemoteException {    
		id = new Identity(0);    
		id.setName(name);  
	}
	
	public Identity getIdentity() throws RemoteException { 
		return id; 
	}
	public boolean connect(Properties p) throws RemoteException {    
		boolean success = false;    
		//String host = p.getProperty("host");    
		//String mName = p.getProperty("mediatorName");    
		//if (host != null && mName != null)       
			try {        
				//String url = "rmi://" + host + "/" + mName;        
				//System.out.println("looking up " + url);
				Registry registry = LocateRegistry.getRegistry();
				mediator = (RMIMediator)registry.lookup("mediator");        
				System.out.println("Got mediator " + mediator);        
				Identity newId = mediator.newMember();
				mediator.register(newId, this);        
				newId.setName(id.getName());        
				id = newId;        
				success = true;      
				}      
			catch (Exception e) {        
				e.printStackTrace();  
				System.out.println("Error connecting with mediatior");
				success = false;      
			}    
		return success;  
		
	}
	
	public boolean send(String tag, String msg, Identity dst)throws IOException, RemoteException 
	{    
		boolean success = false;    
		if (mediator != null) {      
			success = mediator.send(dst, getIdentity(), tag, msg);    
			}    
		return success;  
		}
	
	public boolean send(String tag, Object data, Identity dst)throws IOException, RemoteException {    
		boolean success = false;    
		if (mediator != null) {      
			success = mediator.send(dst, getIdentity(), tag, data);    
			}    
		return success;  
		}
	
	public boolean broadcast(String tag, String msg)throws IOException, RemoteException 
	{    
		boolean success = false;

	if (mediator != null) {
		success = mediator.broadcast(getIdentity(), tag, msg);
		}   
		return success;  
	}

	public boolean broadcast(String tag, Object data)throws IOException, RemoteException {    
		boolean success = false;    
		if (mediator != null) {      
			success = mediator.broadcast(getIdentity(), tag, data);    
			}    
		return success;  
	}
	
	public boolean broadcastPaint(String shape, Color col, MouseEvent e, int X, int Y) throws RemoteException, IOException {
		boolean success = false;    
		if (mediator != null) {      
			success = mediator.broadcastPaint(getIdentity(),shape, col, e, X, Y);    
			System.out.println("Sent to mediator");
			}
		
		return success;  
	}
	
	public boolean notifyPaint(String shape, Color col, MouseEvent e, int X, int Y) {
		System.out.println("Got message to Paint");    
		return true;  
	}

	public boolean notify(String tag, String msg, Identity src) throws IOException, RemoteException {    
		System.out.println("Got message: \"" + tag + " " + msg + "\""  + " from " + src.getName());    
		return true;  
	}
	
	public boolean notify(String tag, Object data, Identity src)throws IOException, RemoteException {    
		System.out.println("Got message: \"" + tag + " " + data + "\"" + " from " + src.getName());    
		return true;  	
	}

	public static void main(String argv[]) {    // Install a security manager       
		try {      
			/*String name = argv[0];      
			String host = argv[1];      
			String mname = argv[2];*/
			Properties props = new Properties();      
			//props.put("host", host);      
			//props.put("mediatorName", mname);      
			//RMIChatClient c = new RMIChatClient("ecc","e","z");
			Color col = Color.black; 
			Color col2 = Color.blue; 
			//RMIMediatorImpl mediatorobj = new RMIMediatorImpl();
			//mediatorobj.main(argv);
			//WhiteboardUser w = new WhiteboardUser ("ClientWhiteboard 1",col,"host","TheMediator");
			JFrame frame = new JFrame();
		    String result = JOptionPane.showInputDialog(frame, "Please enter your username to connect to canvas");
//		    String clientName = result;

			ThreadedWhiteboardUser tobj = new ThreadedWhiteboardUser(result, col, "host","TheMediator");
			//ThreadedWhiteboardUser tobj1 = new ThreadedWhiteboardUser("WB Client 2", col2, "host","TheMediator");
			/*
			 * 
			if (c.connect(props)) {        
				System.out.println("Got mediator...");        
				c.broadcast("msg", "hello world");      
				}
			
			}
			*/    
		}
		catch (Exception e) {      
			System.out.println("Caught exception:");      
			e.printStackTrace();    
		}  
	}
}