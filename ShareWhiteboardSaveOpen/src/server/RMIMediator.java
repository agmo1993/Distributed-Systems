package server;

import java.rmi.RemoteException;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.IOException; 
import java.rmi.Remote; 
import java.util.Vector;
public interface RMIMediator extends Remote {  
	public boolean register(Identity i, RMICollaborator c)throws RemoteException;  
	public Identity newMember() throws RemoteException;  
	public boolean  remove(Identity i) throws RemoteException;  
	public Vector   getMembers() throws RemoteException;
	public boolean send(Identity to, Identity from, String mtag, String msg)throws IOException, RemoteException;  
	public boolean broadcast(Identity from, String mtag, String msg)throws IOException, RemoteException;  
	public boolean send(Identity to, Identity from, String mtag, Object data)throws IOException, RemoteException;  
	public boolean broadcast(Identity from, String mtag, Object data)throws IOException, RemoteException; 
	public boolean broadcastPaint(Identity from, String shape, Color col, MouseEvent e, int X, int Y)throws IOException, RemoteException; 
	}