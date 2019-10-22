package server;

import java.awt.Color;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

import remote.RMIMediator;

public class CreateWhiteBoard {
	
	  public static void main(String argv[]) {    
	    try {      
	    	RMIMediator foo = new RMIMediatorImpl();          	
	    	String name = "TheMediator";      
	    	System.out.println("Registering RMIMediatorImpl as \""+ name + "\"");      
	    	RMIMediator mediator = new RMIMediatorImpl();      
	    	System.out.println("Created mediator, binding...");      
	    	Registry registry = LocateRegistry.getRegistry();
	    	registry.bind("mediator", mediator);      
	    	System.out.println("Remote mediator ready...");
	    	
	    	Properties props = new Properties();      
			Color col = Color.black; 
			String username = argv[2];
		    ThreadedWhiteboardUser tobj = new ThreadedWhiteboardUser(username, col, "host","TheMediator"); 
	    	}    
	    catch (Exception e) {      
	    	System.out.println("Caught exception while registering: " + e);
	    	}  
	    } 
}