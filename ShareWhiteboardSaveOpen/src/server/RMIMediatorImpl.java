package server;

import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import remote.Identity;
import remote.RMICollaborator;
import remote.RMIMediator;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException; 
import java.rmi.Remote; 
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;

public class RMIMediatorImpl extends UnicastRemoteObject implements RMIMediator {  
	private static Enumeration idlistnow;
	Hashtable clients = new Hashtable();  
	Vector idList = new Vector();
	ArrayList<String> usersArrayList = new ArrayList<String>();
	byte[] currentImage;
	public RMIMediatorImpl() throws RemoteException {    
		super();  }
	
  public boolean register(Identity i, RMICollaborator c) throws RemoteException {    
	  System.out.println("Registering member " + i.getId()+ " as " + c.getIdentity().getName());
	  clients.put(i, c);
	  usersArrayList.add(c.getIdentity().getName());
	  
	  return true;  
	  }
  
  
  public static enum idlistnow {
  }
  
  
  public byte[] presentImage() {
	  return this.currentImage;
  }
  
  
  
  public Identity newMember(String userRequest)  {    
	  int max = -1;    
	  boolean found = true;    
	  Enumeration x;
	  if (idList.size() > 0) {
		  Object[] options = {"Yes", "No"};
		  int n = JOptionPane.showOptionDialog(null,
		      "Would you like to grant whiteboad access to "+userRequest +" ?",
		      "Save Canvas",
		      JOptionPane.YES_NO_OPTION,
		      JOptionPane.QUESTION_MESSAGE,
		      null,
		      options,
		      options[1]);				  
		  if (n == 1) {
			  return null;
		  }
		  else {
			RMICollaborator target = null;  
			Enumeration ids = clients.keys(); 
			Identity i = (Identity) ids.nextElement();
			target = (RMICollaborator) clients.get(i);
			try {
			currentImage = target.imageLoad();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
		  }
	  }
	  
	  synchronized (idList) {       
		  x = idList.elements();    
		  }    
	  while (x.hasMoreElements()) {   
		  Identity i = (Identity) x.nextElement();
		        
		  if (i.getId() > max) { 
			  max = i.getId();  
			  }    
		  }
  	
	  Identity newId = new Identity(max + 1);    
	  synchronized (idList) {      
		  idList.addElement(newId);    
    		}   
	  
	  System.out.println("New Member registered");
	  return newId;
	  
    }
  
  
  public boolean remove(Identity i) throws RemoteException {    
	  boolean success = true;    
	  usersArrayList.remove(i.getName());
	  System.out.println(clients.get(i));
	  return success;  
	  }
  
  
  public Vector getMembers() throws RemoteException {    
	  synchronized (idList) {      
		  return (Vector)idList.clone();    
		  }  
	  }
  
  public boolean send(Identity to, Identity from, String mtag, String msg)throws IOException, RemoteException {    
	  boolean success = false;    
	  RMICollaborator c = getMember(to);    
	  synchronized (c) {      
		  if (c != null) {        
			  success = c.notify(mtag, msg, from);      
			  }    
		  }
  return success;  
  }
  
  public boolean send(Identity to, Identity from, String mtag, Object data) throws IOException, RemoteException {    
	  boolean success = false;    
	  RMICollaborator c = getMember(to);    
	  synchronized (c) {      
		  if (c != null) {        
			  success = c.notify(mtag, data, from);      
			  }    
		  }    
	  return success;  
	  }
  
  public boolean broadcast(Identity from, String mtag, String msg)throws IOException, RemoteException {    
	  System.out.println("Broadcasting...");    
	  boolean success = true;    
	  Enumeration ids;    
	  synchronized (clients) {      
		  ids = clients.keys();    
		  }    
	  RMICollaborator target = null;    
	  while (ids.hasMoreElements()) {      
		  Identity i = (Identity)ids.nextElement();      
		  synchronized (clients) {        
			  target = (RMICollaborator)clients.get(i);      
			  }      
		  synchronized (target) {        
			  if (target == null ||!target.notify(mtag, msg, from)) 
			  {          
				  success = false;        
				  }      
			  }
		  
		  }
	  return success;
	  }
  
  public boolean broadcast(Identity from, String mtag, Object data)throws IOException, RemoteException {    
	  boolean success = true;    
	  Enumeration ids;    
	  synchronized (clients) {      
		  ids = clients.keys();    
		  }    
	  RMICollaborator target = null;    
	  while (ids.hasMoreElements()) {      
		  Identity i = (Identity)ids.nextElement();      
		  synchronized (clients) {        
			  target = (RMICollaborator)clients.get(i);      
			  }      
		  synchronized (target) {        
			  if (target == null ||!target.notify(mtag, data, from)) {
				  success = false;        
				  System.out.print(success);
				  }      
			  }    
		  }    
	  return success;  
	  }
  public boolean broadcastUsers() throws RemoteException, IOException {
	  boolean success = true;    
	  Enumeration ids;
//	  ArrayList<String> usersArrayList = new ArrayList<String>();
	  synchronized (clients) {      
		  ids = clients.keys();    
		  }    
	  RMICollaborator target = null;
//	  while (ids.hasMoreElements()) {
//		  Identity i = (Identity)ids.nextElement();
//		  target = (RMICollaborator)clients.get(i); 
//		  usersArrayList.add(target.getIdentity().getName());
////	  }
//	  while (ids.hasMoreElements()) {      
//		  Identity i = (Identity)ids.nextElement();
//		  synchronized (clients) {        
//			  target = (RMICollaborator)clients.get(i);
//			  usersArrayList.add(target.getIdentity().getName());
//			  }
//	  }
	  
	  synchronized (clients) {      
		  ids = clients.keys();    
		  }    
	  while (ids.hasMoreElements()) {      
		  Identity i = (Identity)ids.nextElement();
		  synchronized (clients) {        
			  target = (RMICollaborator)clients.get(i);
			  synchronized (target) {        
				  if (target == null ||!target.notifyUsers(usersArrayList)) {
					  success = false;        
					  System.out.print(success);
					  }      
				  }    
			  }
	  }
	  return success;  
	}
  
  public boolean broadcastPaint(Identity from, String shape, Color col, MouseEvent e, int X, int Y, int brushSize) throws RemoteException, IOException {
	  boolean success = true;    
	  Enumeration ids;    
	  synchronized (clients) {      
		  ids = clients.keys();    
		  }    
	  RMICollaborator target = null;    
	  while (ids.hasMoreElements()) {      
		  Identity i = (Identity)ids.nextElement();      
		  synchronized (clients) {        
			  target = (RMICollaborator)clients.get(i);      
			  }      
		  synchronized (target) {
			  if (!(target.getIdentity().equals(from))) {
				  if (target == null ||!target.notifyPaint(shape, col, e, X, Y, brushSize)) {
					  success = false;        
					  System.out.print(success);
					  }      
			  }    
		  }
	  }
	  System.out.println("Painting broadcasted successfully");
	  return success;  
	  }
  
  protected RMICollaborator getMember(Identity i) {    
	  Enumeration ids;    
	  synchronized (clients) {      
		  ids = clients.keys();    
		  }    
	  RMICollaborator c = null;    
	  Identity tmp;    
	  while (c == null && ids.hasMoreElements()) {       
		  tmp = (Identity)ids.nextElement();       
		  if (tmp.equals(i)) {         
			  synchronized (clients) {           
				  c = (RMICollaborator)clients.get(tmp);
				  }       
			  }    
		  }    
	  return c;  
	  
  }
  public static void main(String argv[]) {    
	  // Install a security manager    System.setSecurityManager(new RMISecurityManager());
    try {      
    	RMIMediator foo = new RMIMediatorImpl();          	
    	String name = "TheMediator";      
    	System.out.println("Registering RMIMediatorImpl as \""+ name + "\"");      
    	RMIMediator mediator = new RMIMediatorImpl();      
    	System.out.println("Created mediator, binding...");      
    	Registry registry = LocateRegistry.getRegistry();
    	registry.bind("mediator", mediator);      
    	System.out.println("Remote mediator ready...");    
    	}    
    catch (Exception e) {      
    	System.out.println("Caught exception while registering: " + e);
    	}  
    }

public boolean broadcastBI(BufferedImage image, Identity from) throws IOException {
	boolean success = true;    
	  Enumeration ids;    
	  synchronized (clients) {      
		  ids = clients.keys();    
		  }    
	  RMICollaborator target = null;    
	  while (ids.hasMoreElements()) {      
		  Identity i = (Identity)ids.nextElement();      
		  synchronized (clients) {        
			  target = (RMICollaborator)clients.get(i);      
			  }      
		  synchronized (target) {
			  if (!(target.getIdentity().equals(from))) {
				  if (target == null ||!target.notifyBI(image)) {
					  success = false;        
					  System.out.print(success);
					  }      
			  }    
		  }
	  }
	  return success;
}
	
}


 