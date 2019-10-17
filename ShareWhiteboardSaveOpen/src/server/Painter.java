package server;


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;
 
public class Painter {
 
	Color col = Color.BLACK;
	JButton clearBtn, colorBtn, newBtn, saveBtn, saveAsBtn, openBtn, freeHand, drawLine, drawRect, drawCircle, drawOval, sendMsg, setBrush, eraseBtn;
  	DrawArea1 drawArea;
	private String[] brushSizeList = {"1","2","3","4","5","6","8","10","12","14","16","20","24","32","48"};
	private int brushSize = 1;
	JComboBox<String> comboBox;
	Boolean freeHandState = true, lineState = false, rectState = false, circleState = false, ovalState = false;
	
	BufferedImage biOpen;
	
	JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	BufferedImage bi;
	Container content;
	

  ActionListener actionListener = new ActionListener() {
 
  public void actionPerformed(ActionEvent e) {
      	if (e.getSource() == clearBtn) {
      		drawArea.clear();
      	} else if (e.getSource() == setBrush) {
      		drawArea.setBrushSize();
      	} else if (e.getSource() == colorBtn) {
      		drawArea.colorChooser();
      	} else if (e.getSource() == freeHand) {
      		drawArea.brush();
      	} else if (e.getSource() == drawLine) {
      		drawArea.line();
      	} else if (e.getSource() == drawRect) {
      		drawArea.rectangle();
      	} else if (e.getSource() == drawCircle) {
      		drawArea.circle();
      	} else if (e.getSource() == drawOval) {
      		drawArea.oval();
      	} else if (e.getSource() == eraseBtn) {
      		drawArea.erase();
      	} else if (e.getSource() == newBtn) {
      		drawArea.newCanvas();
      	} else if (e.getSource() == saveBtn) {
      		drawArea.saveCanvas();
      	} else if (e.getSource() == saveAsBtn) {
      		drawArea.saveAsCanvas();
      	} else if (e.getSource() == openBtn) {
      		drawArea.openCanvas();
      	} else if (e.getSource() == sendMsg) {
      		drawArea.sendMessage();
      	}
    }
  };
 
  public static void main(String[] args) {
    new Painter().show();
  }
 
  public void show() {
    // create main frame
    JFrame frame = new JFrame("Canvas");
    content = frame.getContentPane();
    // set layout on content pane
    content.setLayout(new BorderLayout());
    // create draw area
    drawArea = new DrawArea1();
 
//    JPanel canvas = new JPanel();
    // add to content pane
    content.add(drawArea, BorderLayout.CENTER);
//    canvas.add(drawArea);
 
    // create controls to apply colors and call clear feature
    JPanel controls = new JPanel();
 
    clearBtn = new JButton("Clear");
    clearBtn.addActionListener(actionListener);
    colorBtn = new JButton("Color");
    colorBtn.addActionListener(actionListener);
    saveBtn = new JButton("Save");
    saveBtn.addActionListener(actionListener);
    saveAsBtn = new JButton("Save As");
    saveAsBtn.addActionListener(actionListener);
    newBtn = new JButton("New");
    newBtn.addActionListener(actionListener);
    openBtn = new JButton("Open");
    openBtn.addActionListener(actionListener);
 
    // add to panel
    
//    controls.add(colorBtn);
    controls.add(newBtn);
    controls.add(saveBtn);
    controls.add(saveAsBtn);
    controls.add(openBtn);
//    controls.add(clearBtn);
    
    //2nd jpanel
    JPanel shapes = new JPanel();
    freeHand = new JButton("Brush");
    freeHand.addActionListener(actionListener);
    drawLine = new JButton("Line");
    drawLine.addActionListener(actionListener);
    drawRect = new JButton("Rectangle");
    drawRect.addActionListener(actionListener);
    drawCircle = new JButton("Circle");
    drawCircle.addActionListener(actionListener);
    drawOval = new JButton("Oval");
    drawOval.addActionListener(actionListener);
    setBrush = new JButton("Set Brush Size");
    setBrush.addActionListener(actionListener);
    eraseBtn = new JButton("Eraser");
    eraseBtn.addActionListener(actionListener);
    
    comboBox = new JComboBox<String>(brushSizeList);
    
    shapes.add(comboBox);
    shapes.add(setBrush);
    shapes.add(colorBtn);
    shapes.add(freeHand);
    shapes.add(drawLine);
    shapes.add(drawRect);
    shapes.add(drawCircle);
    shapes.add(drawOval);
    shapes.add(eraseBtn);
    shapes.add(clearBtn);
    
//    shapes.add(clearBtn);
//    openBtn = new JButton("Send");
//    openBtn.addActionListener(actionListener);
    
    //Chat
    JPanel chatBox = new JPanel(); 
    JTextArea textArea = new JTextArea();
//	textArea.setBounds(742, 6, 202, 424);
//	frame.getContentPane().add(textArea);
	
//	JTextArea textArea_1 = new JTextArea();
	JTextField sendText = new JTextField();
	sendText.setSize(250, 200);
//	textArea_1.setBounds(742, 433, 202, 71);
	
	sendMsg = new JButton("Send");
    sendMsg.addActionListener(actionListener);
	
	
	chatBox.add(sendText);
//	chatBox.add(textArea_1);
	chatBox.add(sendMsg);
	
//	frame.getContentPane().add(textArea_1);
 
    // add to content pane
//	content.add(canvas, BorderLayout.CENTER);
    content.add(controls, BorderLayout.NORTH);
    content.add(shapes, BorderLayout.SOUTH);
//    content.add(chatBox, BorderLayout.AFTER_LAST_LINE);
 
   // bi = new BufferedImage(content.getSize().width, content.getSize().height, BufferedImage.TYPE_INT_ARGB); 
	
    frame.setSize(1000, 800);
   // bi = new BufferedImage(content.getSize().width, content.getSize().height, BufferedImage.TYPE_INT_ARGB); 
    // can close frame
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // show the swing paint result
    frame.setVisible(true);
 
    // Now you can try our Swing Paint !!! Enjoy :D
  }
  
  public class DrawArea1 extends JPanel{
	  
	  // Image in which we're going to draw
	  private Image image;
	  // Graphics2D object ==> used to draw on
	  private Graphics2D g2;
	  // Mouse coordinates
	  private int currentX, currentY, oldX, oldY;
	 
	  public DrawArea1() {
	    setDoubleBuffered(false);
	    addMouseListener(new MouseAdapter() {
	      public void mousePressed(MouseEvent e) {
	        // save coord x,y when mouse is pressed
	        oldX = e.getX();
	        oldY = e.getY();
	      }
	    });
	    
	    addMouseMotionListener(new MouseMotionAdapter() {
	      public void mouseDragged(MouseEvent e) {
	        // coord x,y when drag mouse
	        currentX = e.getX();
	        currentY = e.getY();
	        
//	        System.out.println(e.getLocationOnScreen());
	 
	        if ((g2 != null) & freeHandState == true){
	          // draw line if g2 context not null
	        	g2.setPaint(col);
	          g2.drawLine(oldX, oldY, currentX, currentY);
	          // refresh draw area to repaint
	          repaint();
	          // store current coords x,y as olds x,y
	          oldX = currentX;
	          oldY = currentY;
	        } 
//	        else if((freeHandState == false) & (lineState == true)){
//	        	g2.setPaint(col);
//		        g2.drawLine(oldX, oldY, currentX, currentY);
//	        	
//	        } //else if(){
	        	
//	        }
	      }
	      
	      
	    });
	    
	    addMouseListener(new MouseAdapter() {
	    	public void mouseReleased(MouseEvent e) {
	    		int shapeWidth = Math.abs(currentX - oldX);
	    		int shapeHeight = Math.abs(currentY - oldY);
		    	  if((freeHandState == false) & (lineState == true)){
//		    		  if (col == Color.WHITE){
//		    			  col = Color.BLACK;
//		    		  }
		    		  
			        	g2.setPaint(col);
				        g2.drawLine(oldX, oldY, currentX, currentY);
				        repaint();
			        	
			        } 
		    	  else if((freeHandState == false) & (rectState == true)){
//		    		  if (col == Color.WHITE){
//		    			  col = Color.BLACK;
//		    		  }
			        	g2.setPaint(col);
				        g2.drawRect(oldX, oldY, shapeWidth, shapeHeight);
//			        	g2.drawRect(oldX, oldY, currentX-oldX, currentY-oldY);
				        repaint();
			        	
			        } 
		    	  else if((freeHandState == false) & (circleState == true)){
//		    		  if (col == Color.WHITE){
//		    			  col = Color.BLACK;
//		    		  }
			        	g2.setPaint(col);
				        g2.drawOval(oldX, oldY, shapeWidth, shapeWidth);
//			        	g2.drawRect(oldX, oldY, currentX-oldX, currentY-oldY);
				        repaint();
			        	
			        } 
		    	  else if((freeHandState == false) & (ovalState == true)){
//		    		  if (col == Color.WHITE){
//		    			  col = Color.BLACK;
//		    		  }
			        	g2.setPaint(col);
				        g2.drawOval(oldX, oldY, shapeWidth, shapeHeight);
//			        	g2.drawRect(oldX, oldY, currentX-oldX, currentY-oldY);
				        repaint();
			        	
			        } 
		      }
		});
	  }
	 
	  protected void paintComponent(Graphics g) {
	    if (image == null) {
	      // image to draw null ==> we create
	      image = createImage(getSize().width, getSize().height);
	      g2 = (Graphics2D) image.getGraphics();
	      // enable antialiasing
	      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	      // clear draw area
	      clear();
	    }
	 
	    g.drawImage(image, 0, 0, null);
	  }
	 
	  // now we create exposed methods
	  public void clear() {
	    g2.setPaint(Color.white);
	    // draw white on entire draw area to clear
	    g2.fillRect(0, 0, getSize().width, getSize().height);
	    g2.setPaint(Color.black);
	    repaint();
	  }
	 
	  public void setBrushSize() {
	    // apply red color on g2 context
		  brushSize = Integer.parseInt((String) comboBox.getSelectedItem());
		  g2.setStroke(new BasicStroke(brushSize));
		  
	  }
	 
	  public void colorChooser() {
		  col = JColorChooser.showDialog(null, "Choose a color", col);
//	    g2.setPaint(Color.black);
		  
	  }
	 
	  public void brush() {
		  if (col == Color.WHITE){
			  col = Color.BLACK;
		  }
//		  	col = Color.BLACK;
		    freeHandState = true;
		    lineState = false;
		    rectState = false;
		    circleState = false;
		    ovalState = false;
	  }
	 
	  public void line() {
//		  col = Color.BLACK;
		  if (col == Color.WHITE){
			  col = Color.BLACK;
		  }
		  	freeHandState = false;
		    lineState = true;
		    rectState = false;
		    circleState = false;
		    ovalState = false;
//		    col = Color.BLACK;
	  }
	 
	  public void rectangle() {
		  if (col == Color.WHITE){
			  col = Color.BLACK;
		  }
		  	freeHandState = false;
		    lineState = false;
		    rectState = true;
		    circleState = false;
		    ovalState = false;
	  }
	  
	  public void circle() {
		  if (col == Color.WHITE){
			  col = Color.BLACK;
		  }
		  	freeHandState = false;
		    lineState = false;
		    rectState = false;
		    circleState = true;
		    ovalState = false;
	  }
	  
	  public void oval() {
		  if (col == Color.WHITE){
			  col = Color.BLACK;
		  }
		  	freeHandState = false;
		    lineState = false;
		    rectState = false;
		    circleState = false;
		    ovalState = true;
	  }
	  
	  public void erase() {
		  if (col == Color.WHITE){
			  col = Color.BLACK;
		  }
		  freeHandState = true;
		    lineState = false;
		    rectState = false;
		    circleState = false;
		    ovalState = false;
		  col = Color.WHITE;
		    g2.setPaint(col);
//		    col = Color.BLACK;
	  }
	  public void newCanvas() {
		    g2.setPaint(Color.blue);
	  }
	  
	  public void saveCanvas() {
		    g2.setPaint(Color.blue);
	  }
	  
	  public void saveAsCanvas() {
		  g2 = bi.createGraphics();
			this.paint(g2);  //this == JComponent
			g2.dispose();
			try{ImageIO.write(bi,"png",new File("test.png"));}catch (Exception e1) {}
//			int returnValue = jfc.showOpenDialog(null);
			 int returnValue = jfc.showSaveDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = jfc.getSelectedFile();
				System.out.println(selectedFile.getAbsolutePath());
				try{ImageIO.write(bi,"png", selectedFile);}catch (Exception e1) {}
			}
	  }
	  
	  public void openCanvas() {
		    g2.setPaint(Color.blue);
	  }
	  
	  public void sendMessage() {
		    g2.setPaint(Color.blue);
	  }
	 
	}
 
}