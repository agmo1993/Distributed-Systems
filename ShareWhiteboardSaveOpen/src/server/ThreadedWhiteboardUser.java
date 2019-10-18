package server;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.*;
import java.util.Hashtable;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.border.EmptyBorder;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JInternalFrame;

import View.WhiteBoardInterface;


public class ThreadedWhiteboardUser extends RMICollaboratorImpl implements java.awt.event.MouseListener,java.awt.event.MouseMotionListener{
	protected Hashtable lastPts = new Hashtable();
	protected Component whiteboard;
	protected Image buffer;
	protected BufferedImage buffer1;
	protected CommHelper helper;
	
	protected JFrame frmSharedWhitboard;
	protected JMenuBar menuBar;
	protected JMenu mnFile;
	protected JMenuItem mntmNew;
	protected JMenuItem mntmOpen;
	protected JMenuItem mntmSave;
	protected JMenuItem mntmSaveAs;
	protected JMenuItem mntmClose;
	protected JSeparator separator;
	protected JSeparator separator_1;
	protected JSeparator separator_2;
	protected JSeparator separator_3;
	protected JSeparator separator_4;
	protected JSeparator separator_5;
	protected JPanel canvas_panel;
	protected JPanel secondary_panel;
	protected JPanel tools_panel;

	protected JTextField textField;
	protected JButton btnSend;
	protected JTextArea logArea;
	protected JTextArea chatArea;
	
	protected JButton btnBrush;
	protected JButton btnLine;
	protected JComboBox comboBoxSize;
	protected JButton btnSelSize;
	protected JButton btnCircle;
	protected JButton btnRectangle;
	protected JButton btnOval;
	protected JButton btnEraser;
	protected JButton btnColor;
	protected JButton btnClear;
	protected JButton btnText;
	private JTextField textField_1;
	private JTextArea textArea;

	
	
	
	//***********************
	
	private int _shape = 2;
    private int _currentStartX = 0;  // where mouse first pressed
    private int _currentStartY = 0;
    private int _currentEndX   = 0;  // where dragged to or released
    private int _currentEndY   = 0;
	
	public static final int NONE      = 0;
    public static final int LINE      = 1;
    public static final int RECTANGLE = 2;
    public static final int CIRCLE    = 3;
    public static final int OVAL      = 4;
    public Graphics2D grafarea;
    
    //***********************
    Color col = Color.BLACK;
	//JButton clearBtn, colorBtn, newBtn, saveBtn, saveAsBtn, openBtn, freeHand, drawLine, drawRect, drawCircle, drawOval, sendMsg, setBrush, eraseBtn;
  	DrawArea1 drawArea;
	private String[] brushSizeList = {"1","2","3","4","5","6","8","10","12","14","16","20","24","32","48"};
	private int brushSize = 1;
	Boolean freeHandState = true, lineState = false, rectState = false, circleState = false, ovalState = false, textState = false;
	
	BufferedImage biOpen;
	
	JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	BufferedImage bi;
	Container content;
	File selectedFile = null;
	private String textInput;
	
	protected static boolean isNew = true;
	protected static boolean isSaved = false;
	   
    //***********************
    
	ActionListener actionListener = new ActionListener() {
		 
		public void actionPerformed(ActionEvent e) {
	      	if (e.getSource() == btnClear) {
	      		drawArea.clear();
	      	} else if (e.getSource() == comboBoxSize) {
	      		drawArea.setBrushSize();
	      	} else if (e.getSource() == btnColor) {
	      		drawArea.colorChooser();
	      	} else if (e.getSource() == btnBrush) {
	      		drawArea.brush();
	      	} else if (e.getSource() == btnLine) {
	      		drawArea.line();
	      	} else if (e.getSource() == btnRectangle) {
	      		drawArea.rectangle();
	      	} else if (e.getSource() == btnCircle) {
	      		drawArea.circle();
	      	} else if (e.getSource() == btnOval) {
	      		drawArea.oval();
	      	} else if (e.getSource() == btnText) {
	      		drawArea.text();
	      	}else if (e.getSource() == btnEraser) {
		      		drawArea.erase();
	      	} else if (e.getSource() == mntmNew) {
	      		drawArea.newCanvas();
	      	} else if (e.getSource() == mntmSave) {
	      		drawArea.saveCanvas();
	      	} else if (e.getSource() == mntmSaveAs) {
	      		drawArea.saveAsCanvas();
	      	} else if (e.getSource() == mntmOpen) {
	      		drawArea.openCanvas();
	      	} else if (e.getSource() == mntmClose) {
	      		drawArea.closeCanvas();
	      	} 
	    }
	  };
	  
	  WindowListener windowListener = new WindowListener() {
		
		@Override
		public void windowOpened(WindowEvent e) {}
		@Override
		public void windowIconified(WindowEvent e) {}
		@Override
		public void windowDeiconified(WindowEvent e) {}
		@Override
		public void windowDeactivated(WindowEvent e) {}
		@Override
		public void windowClosing(WindowEvent e) {
			drawArea.closeCanvas();	
		}
		@Override
		public void windowClosed(WindowEvent e) {}
		@Override
		public void windowActivated(WindowEvent e) {}
	};
	
	public ThreadedWhiteboardUser(String name, Color color, String host, String mname) throws RemoteException {
		super(name, host, mname);
		getIdentity().setProperty("color", color);
		startUI();
		helper = new CommHelper(this);
		helper.start();
	}
	
	protected void startUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					buildUI();
					frmSharedWhitboard.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//***********************
	
	protected void buildUI() {
		
		frmSharedWhitboard = new JFrame();
		frmSharedWhitboard.setTitle("Shared Whiteboard");
		frmSharedWhitboard.setBounds(100, 100, 1118, 764);
		//frmSharedWhitboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSharedWhitboard.addWindowListener(windowListener);
		
		
		//File menu creation
		menuBar = new JMenuBar();
		frmSharedWhitboard.setJMenuBar(menuBar);
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmNew = new JMenuItem("New");
		mnFile.add(mntmNew);
		mntmNew.addActionListener(actionListener);
		mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		mntmOpen.addActionListener(actionListener);
		mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		mntmSave.addActionListener(actionListener);
		mntmSaveAs = new JMenuItem("Save As...");
		mnFile.add(mntmSaveAs);
		mntmSaveAs.addActionListener(actionListener);
		separator = new JSeparator();
		mnFile.add(separator);
		mntmClose = new JMenuItem("Close");
		mnFile.add(mntmClose);
		mntmClose.addActionListener(actionListener);
		
		
		//Status panel Creation
		JPanel status_panel = new JPanel();
		status_panel.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY), new EmptyBorder(4, 4, 4, 4)));
		final JLabel status;
		
		canvas_panel = new JPanel();
		tools_panel = new JPanel();
		secondary_panel = new JPanel();
		drawArea = new DrawArea1();
		
		separator_1 = new JSeparator();
		separator_2 = new JSeparator();
		separator_3 = new JSeparator();
		separator_4 = new JSeparator();
		separator_5 = new JSeparator();
		
		separator_1.setOrientation(SwingConstants.VERTICAL);
		//canvas_panel.setBackground(Color.white);
		//canvas_panel.setLayout(new BorderLayout(0, 0));
		
		GroupLayout groupLayout = new GroupLayout(frmSharedWhitboard.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(tools_panel, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(drawArea, GroupLayout.PREFERRED_SIZE, 703, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(secondary_panel, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)
					.addGap(2))
				.addComponent(status_panel, GroupLayout.DEFAULT_SIZE, 1100, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(secondary_panel, GroupLayout.PREFERRED_SIZE, 661, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(separator_1, Alignment.LEADING)
							.addComponent(tools_panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE))
						.addComponent(drawArea, GroupLayout.PREFERRED_SIZE, 662, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(status_panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(6))
		);
				
		chatArea = new JTextArea();
		chatArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chatArea.setEditable(false);
		
		logArea = new JTextArea();
		logArea.setEditable(false);
		
		textField = new JTextField();
		textField.setColumns(10);
		btnSend = new JButton("Send");
				
		GroupLayout gl_secondary_panel = new GroupLayout(secondary_panel);
		gl_secondary_panel.setHorizontalGroup(
			gl_secondary_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_secondary_panel.createSequentialGroup()
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 201, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSend)
					.addGap(3))
				.addGroup(gl_secondary_panel.createSequentialGroup()
					.addGroup(gl_secondary_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(chatArea, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
						.addComponent(separator_2, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))
					.addContainerGap())
				.addGroup(Alignment.LEADING, gl_secondary_panel.createSequentialGroup()
					.addComponent(logArea, GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_secondary_panel.setVerticalGroup(
			gl_secondary_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_secondary_panel.createSequentialGroup()
					.addComponent(logArea, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addComponent(separator_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chatArea, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_secondary_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSend)))
		);
		secondary_panel.setLayout(gl_secondary_panel);
		
		btnBrush = new JButton("Brush");
		btnBrush.addActionListener(actionListener);
		btnLine = new JButton("Line");
			
		btnLine.addActionListener(actionListener);
		comboBoxSize = new JComboBox<String>(brushSizeList);
		comboBoxSize.addActionListener (actionListener);
		btnCircle = new JButton("Circle");
		btnCircle.addActionListener(actionListener);
		//btnCircle.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnRectangle = new JButton("Rectangle");
		btnRectangle.addActionListener(actionListener);
		//btnRectangle.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnOval = new JButton("Oval");
		btnOval.addActionListener(actionListener);
		//btnOval.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnEraser = new JButton("Eraser");
		btnEraser.addActionListener(actionListener);
		//btnEraser.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnColor = new JButton("Color");
		btnColor.addActionListener(actionListener);
		btnClear = new JButton("Clear");
		btnClear.addActionListener(actionListener);
		btnText = new JButton("Text");
		btnText.addActionListener(actionListener);
//		textField_1 = new JTextField();
//		textField_1.setColumns(10);
		
		textArea = new JTextArea();		
		GroupLayout gl_tools_panel = new GroupLayout(tools_panel);
		gl_tools_panel.setHorizontalGroup(
			gl_tools_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_tools_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tools_panel.createParallelGroup(Alignment.LEADING)
							.addComponent(btnBrush, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
							.addComponent(comboBoxSize, Alignment.TRAILING, 0, 80, Short.MAX_VALUE)
							.addComponent(btnEraser, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnCircle, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
							.addComponent(btnOval, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addComponent(separator_3, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
							.addComponent(btnRectangle, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnLine, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
							.addComponent(btnText, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
//							.addComponent(textField_1, GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
							.addComponent(separator_4, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
							.addComponent(btnColor, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addComponent(separator_5, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
							.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_tools_panel.setVerticalGroup(
			gl_tools_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tools_panel.createSequentialGroup()
					.addGap(21)
					.addComponent(btnBrush)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnEraser, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addGap(8)
					.addComponent(comboBoxSize, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(16)
					.addComponent(btnCircle)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRectangle, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnOval, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnLine)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnText, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
//					.addComponent(textField_1, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
//					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnColor)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnClear)
					.addPreferredGap(ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
					.addComponent(separator_5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(108))
		);
		tools_panel.setLayout(gl_tools_panel);
		frmSharedWhitboard.getContentPane().setLayout(groupLayout);
			
		//***********************
		
		String name = null;
		try {
			name = getIdentity().getName();
		}catch (Exception e) {
			name = "Unknown";
		}
		
		status = new JLabel("Your name: " + name);
		status_panel.add(status);
		
		whiteboard = canvas_panel;
		whiteboard.addMouseListener(this);
		whiteboard.addMouseMotionListener(this);
		//buffer1 = (BufferedImage) canvas_panel.createImage(frmSharedWhitboard.getSize().width, frmSharedWhitboard.getSize().height);
		buffer = whiteboard.createImage(canvas_panel.getSize().width,canvas_panel.getSize().height);
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// See if there's something to say...
				String msg = textField.getText();
				if (msg.length() > 0) {
					try {
						// Broadcast our message to the rest of the chat clients
						boolean success = broadcast("chat", msg);
						if (success) {
							//logArea.setText("Sent message OK.");
							logArea.append("Sent message OK.\n");
							//System.out.println("Sent message OK.");
						}
						else {
							//logArea.setText("Failed to send message.");
							logArea.append("Failed to send message.\n");
							//System.out.println("Failed to send message.");
						}
						// Clear the chat input field
						textField.setText("");
					}
					catch (Exception exc) {
					}
				}
			}
		});
		
		/*Frame f = new Frame();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		f.setLayout(gridbag);
		f.addNotify();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		Canvas canvas1 = new java.awt.Canvas();
		canvas1.setSize(400,400);
		canvas1.setBackground(Color.white);
		gridbag.setConstraints(canvas1, c);
		f.add(canvas1);
		Label label1 = new java.awt.Label("Your name: " + name);
		label1.setSize(100,30);
		gridbag.setConstraints(label1, c);
		f.add(label1);
		f.setSize(600,600);
		//f.show(); -- deprecated 
		f.setVisible(true);
		whiteboard = canvas1;
		whiteboard.addMouseListener(this);
		whiteboard.addMouseMotionListener(this);
		buffer = whiteboard.createImage(f.getSize().width,
		f.getSize().height);*/
	}
	
	//***********************
	
	protected void nextLine(String agent, Point pt, Color c) {
		Graphics g = buffer.getGraphics();
		g.setColor(c);
		Point lastPt = (Point)lastPts.get(agent);
		g.drawLine(lastPt.x, lastPt.y, pt.x, pt.y);
		canvas_panel.getGraphics().drawImage(buffer, 0, 0, canvas_panel);
	}
	public void mousePressed(MouseEvent ev) {
		System.out.println("mousePressed start");
		Point evPt = ev.getPoint();
		try {
			lastPts.put(getIdentity().getName(), evPt);
			CommHelper.addMsg("start", evPt);
			System.out.println("mousePressed end");
		}catch (Exception e) {
			System.out.println("MousePressed catch");
		}
		/*_currentStartX = ev.getX(); // save x coordinate of the click
        _currentStartY = ev.getY(); // save y
        _currentEndX   = _currentStartX;   // set end to same pixel
        _currentEndY   = _currentStartY;*/
	}	
	public void mouseReleased(MouseEvent ev) {
		System.out.println("MouseReleased start");
		Point evPt = ev.getPoint();
		try {
			nextLine(getIdentity().getName(), evPt,
					(Color)getIdentity().getProperty("color"));
			lastPts.remove(getIdentity().getName());
			helper.addMsg("end", evPt);
			System.out.println("MouseReleased end");
			helper.run();
		}catch (Exception e) {
			System.out.println(e);
		}
		/*_currentEndX = ev.getX(); // save ending coordinates
        _currentEndY = ev.getY();
        
        // Draw the current shape onto the buffered image.
        grafarea = buffer1.createGraphics();
        drawCurrentShape(grafarea);
        canvas_panel.repaint();*/
	}
	public void mouseDragged(MouseEvent ev) {
		/*_currentEndX = ev.getX();   // save new x and y coordinates
        _currentEndY = ev.getY();
        canvas_panel.repaint();            // show new shape*/
		Point evPt = ev.getPoint();
		try {
			nextLine(getIdentity().getName(), evPt,
					(Color)getIdentity().getProperty("color"));
			lastPts.put(getIdentity().getName(), evPt);
			helper.addMsg("drag", evPt);
		}catch (Exception e) {}
		
	}
	public void mouseExited(MouseEvent ev) {}
	public void mouseMoved(MouseEvent ev) {}
	public void mouseClicked(MouseEvent ev) {}
	public void mouseEntered(MouseEvent ev) {}
	
	public boolean notifyPaint(String shape, Color col, MouseEvent e, int X, int Y) {
		boolean success = false;
		success = drawArea.remotePaint(shape, col, e, X, Y);
		return success;
	}
	
	public boolean notify(String tag, String msg, Identity src) throws IOException, RemoteException {
		// Print the message in the chat area.
		chatArea.append("\n" + src.getName() + ": " + msg);
		return true;
	}
	public boolean notify(String tag, Object data, Identity src) throws IOException, RemoteException {
		// If this is our own event, ignore it since it's already been handled.
		if (src.getName().compareTo(getIdentity().getName()) == 0) {
			return true;
		}
		Color agentColor = null;
		try {
			agentColor = (Color)src.getProperty("color");
		}
		catch (Exception exc) {
			System.out.println("Exception while getting color.");
			exc.printStackTrace();
		}
		if (tag.compareTo("start") == 0) {
			// First point along a path, save it and continue
		lastPts.put(src.getName(), data);
		}
		else if (tag.compareTo("drag") == 0) {
			// Next point in a path, draw a line from the last
			// point to here, and save this point as the last point.
			nextLine(src.getName(), (Point)data, agentColor);
			lastPts.put(src.getName(), data);
		}
		else if (tag.compareTo("end") == 0) {
			// Last point in a path, so draw the line and remove
			// the last point.
			nextLine(src.getName(), (Point)data, agentColor);
			lastPts.remove(src.getName());
		}
		return true;
	}
	
	
	//***********************
	
	class DrawArea1 extends JPanel {
		  
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
		       
		        
//		        System.out.println(e.getLocationOnScreen());
		 
		        if ((g2 != null) & freeHandState == true){
		          // draw line if g2 context not null
		        	g2.setPaint(col);
		          g2.drawLine(oldX, oldY, currentX, currentY);
		          // refresh draw area to repaint
		          repaint();
		          isNew = false;
		          isSaved = false;
		          try {
						broadcastPaint("line",col,e,oldX,oldY);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		          // store current coords x,y as olds x,y
		          oldX = currentX;
		          oldY = currentY;
		          
		          
		          
		        } 
//		        else if((freeHandState == false) & (lineState == true)){
//		        	g2.setPaint(col);
//			        g2.drawLine(oldX, oldY, currentX, currentY);
//		        	
//		        } //else if(){
		        	
//		        }
		      }
		      
		      
		    });
		    
		    addMouseListener(new MouseAdapter() {
		    	public void mouseReleased(MouseEvent e) {
		    		int shapeWidth = Math.abs(currentX - oldX);
		    		int shapeHeight = Math.abs(currentY - oldY);
			    	  if((freeHandState == false) & (lineState == true)){
//			    		  if (col == Color.WHITE){
//			    			  col = Color.BLACK;
//			    		  }
			    		  
				        	g2.setPaint(col);
					        g2.drawLine(oldX, oldY, currentX, currentY);
					        repaint();
					        isSaved = false;
					        isNew = false;
					        try {
								broadcastPaint("line",col,e,oldX,oldY);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
				        	
				        } 
			    	  else if((freeHandState == false) & (rectState == true)){
//			    		  if (col == Color.WHITE){
//			    			  col = Color.BLACK;
//			    		  }
				        	g2.setPaint(col);
					        g2.drawRect(oldX, oldY, shapeWidth, shapeHeight);
//				        	g2.drawRect(oldX, oldY, currentX-oldX, currentY-oldY);
					        repaint();
					        isSaved = false;
					        isNew = false;
					        try {
								broadcastPaint("rectangle",col, e, oldX, oldY);
								System.out.println("Painting broadcasted");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								System.out.println("Error");
								e1.printStackTrace();
							}
				        	
				        } 
			    	  else if((freeHandState == false) & (circleState == true)){
//			    		  if (col == Color.WHITE){
//			    			  col = Color.BLACK;
//			    		  }
				        	g2.setPaint(col);
					        g2.drawOval(oldX, oldY, shapeWidth, shapeWidth);
//				        	g2.drawRect(oldX, oldY, currentX-oldX, currentY-oldY);
					        repaint();
					        isSaved = false;
					        isNew = false;
					        try {
								broadcastPaint("circle",col,e, oldX,oldY);
								System.out.println("Painting broadcasted");
							} catch (IOException e1) {
								System.out.println("Error");
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
				        	
				        } 
			    	  else if((freeHandState == false) & (ovalState == true)){
//			    		  if (col == Color.WHITE){
//			    			  col = Color.BLACK;
//			    		  }
				        	g2.setPaint(col);
					        g2.drawOval(oldX, oldY, shapeWidth, shapeHeight);
//				        	g2.drawRect(oldX, oldY, currentX-oldX, currentY-oldY);
					        repaint();
					        isSaved = false;
					        isNew = false;
					        try {
								broadcastPaint("oval",col,e, oldX,oldY);
								System.out.println("Painting broadcasted");
							} catch (IOException e1) {
								System.out.println("Error");
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
				        	
				        } 
			    	  else if((freeHandState == false) & (textState == true)){
//			    		  if (col == Color.WHITE){
//			    			  col = Color.BLACK;
//			    		  }
				        	g2.setPaint(col);
//					        g2.drawOval(oldX, oldY, shapeWidth, shapeHeight);
//				        	if(brushSize < 8) {
//				        		brushSize = 8;
//				        	}
//				        	int fontSize = shapeWidth;
//				        	JFrame frame = new JFrame();
//				        	Object result = JOptionPane.showInputDialog(frame, "Enter printer name:");
				        	Font font = new Font("Serif", Font.PLAIN, shapeWidth);
				        	 
				        	g2.setFont(font);
					        g2.drawString(textArea.getText(), oldX, oldY);
//				        	g2.drawRect(oldX, oldY, currentX-oldX, currentY-oldY);
					        repaint();
					        isSaved = false;
					        isNew = false;
					        try {
								broadcastPaint(textArea.getText(),col,e, oldX,oldY);
								System.out.println("Painting broadcasted");
							} catch (IOException e1) {
								System.out.println("Error");
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
				        	
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
			  col = Color.BLACK;
//			  freeHandState = true;
			  freeHandState = true;
			  lineState = false;
			  rectState = false;
			  circleState = false;
			  ovalState = false;
			  textState = false;
			  g2.setPaint(Color.white);
			  g2.fillRect(0, 0, getSize().width, getSize().height);
			  g2.setPaint(Color.black);
			  g2.setStroke(new BasicStroke(1));
			  repaint();
			  isSaved = false;
			  isNew = true;
			  logArea.setText("");
		  }
		 
		  public void setBrushSize() {
			  brushSize = Integer.parseInt((String) comboBoxSize.getSelectedItem());
			  g2.setStroke(new BasicStroke(brushSize));
			  
		  }
		 
		  public void colorChooser() {
			  col = JColorChooser.showDialog(null, "Choose a color", col);
//		    g2.setPaint(Color.black);
			  
		  }
		 
		  public void brush() {
			  if (col == Color.WHITE){
				  col = Color.BLACK;
			  }
//			  	col = Color.BLACK;
			    freeHandState = true;
			    lineState = false;
			    rectState = false;
			    circleState = false;
			    ovalState = false;
			    textState = true;
		  }
		 
		  public void line() {
//			  col = Color.BLACK;
			  if (col == Color.WHITE){
				  col = Color.BLACK;
			  }
			  	freeHandState = false;
			    lineState = true;
			    rectState = false;
			    circleState = false;
			    ovalState = false;
			    textState = true;
//			    col = Color.BLACK;
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
			    textState = true;
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
			    textState = true;
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
			    textState = true;
		  }
		  
		  public void text() {
			  if (col == Color.WHITE){
				  col = Color.BLACK;
			  }
			  	freeHandState = false;
			    lineState = false;
			    rectState = false;
			    circleState = false;
			    ovalState = false;
			    textState = true;
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
//			    col = Color.BLACK;
		  }
		  public void newCanvas() {
			  if(!isSaved && !isNew) {
				  Object[] options = {"Save", "Don't Save", "Cancel"};
				  int n = JOptionPane.showOptionDialog(null,
				      "Would you like to save the canvas before creating a new one?",
				      "Save Canvas",
				      JOptionPane.YES_NO_CANCEL_OPTION,
				      JOptionPane.QUESTION_MESSAGE,
				      null,
				      options,
				      options[2]);				  
				  if (n == 0) {
					  saveAsCanvas();
					  isSaved = true;
					  clear();
				  } else if(n == 1) {
					  clear();
					  logArea.append("...New Painting created.\n");
				  } else {}
				  
			  }else {
				  clear();
				  logArea.append("...New Painting created.\n");
			  }
		  }
		  
		  public void saveCanvas() {
			  
			  if (selectedFile != null) {
				  try {
					  	bi = new BufferedImage(drawArea.getSize().width, drawArea.getSize().height, BufferedImage.TYPE_INT_ARGB); 
						Graphics g = bi.createGraphics();
						drawArea.paint(g);  //this == JComponent
						g.dispose();
						//ImageIcon imageIcon = new ImageIcon(bi); 
						ImageIO.write(bi,"png",selectedFile);
						logArea.append("Saved... \n");
						isSaved = true;
					
					}
					catch (Exception e1) {
						logArea.append("Problems with saving!!!\n");
					}
			  } else {
				  saveAsCanvas();
			  }
			  
			    
		  }
		  
		  public void saveAsCanvas() {
			  
			  bi = new BufferedImage(drawArea.getSize().width, drawArea.getSize().height, BufferedImage.TYPE_INT_ARGB); 
				Graphics g = bi.createGraphics();
				drawArea.paint(g);  //this == JComponent
				g.dispose();
//				try{ImageIO.write(bi,"png",new File("test.png"));}catch (Exception e1) {}
//				int returnValue = jfc.showOpenDialog(null);
				int returnValue = jfc.showSaveDialog(null);
				ImageIcon imageIcon = new ImageIcon(bi); 

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					selectedFile = jfc.getSelectedFile();
//					System.out.println(selectedFile.getAbsolutePath());
					try {
						ImageIO.write(bi,"png",selectedFile);
						logArea.append("Saved... \n");
						isSaved = true;
					}
					catch (Exception e1) {
						logArea.append("Problems with saving!!!\n");
					}
//					try{ImageIO.write(bi, "png", selectedFile);}catch (Exception e1) {}
				}
		  }
		  
		  public void openCanvas() {
			  if(isNew) {
				  int returnValue = jfc.showOpenDialog(null);
//					 int returnValue = jfc.showSaveDialog(null);
				  clear();

					if (returnValue == JFileChooser.APPROVE_OPTION) {
						selectedFile = jfc.getSelectedFile();
//						System.out.println(selectedFile.getAbsolutePath());
						try {
							Image imageInput = ImageIO.read(selectedFile);
							//drawArea.image = imageInput;
							BufferedImage bi = (BufferedImage) imageInput;
							Graphics g = bi.createGraphics();
							clear();
							g2.drawImage(imageInput,0,0,null);
							repaint();
							isNew = false;
							logArea.append("File opened sucessfully.\n");
						}
						catch (Exception e1) {
							logArea.append("Error opening file!!!.\n");
						}
					}
			  } else if(!isSaved) {
				  Object[] options = {"Save", "Don't Save", "Cancel"};
				  int n = JOptionPane.showOptionDialog(null,
				      "Would you like to save the canvas before creating a new one?",
				      "Save Canvas",
				      JOptionPane.YES_NO_CANCEL_OPTION,
				      JOptionPane.QUESTION_MESSAGE,
				      null,
				      options,
				      options[2]);				  
				  if (n == 0) {
					  saveAsCanvas();
					  isSaved = true;
					  int returnValue = jfc.showOpenDialog(null);
//						 int returnValue = jfc.showSaveDialog(null);
					  clear();
						if (returnValue == JFileChooser.APPROVE_OPTION) {
							selectedFile = jfc.getSelectedFile();
//							System.out.println(selectedFile.getAbsolutePath());
							try {
								Image imageInput = ImageIO.read(selectedFile);
								BufferedImage bi = (BufferedImage) imageInput;
								Graphics g = bi.createGraphics();
								clear();
								g2.drawImage(imageInput,0,0,null);
								repaint();
								isNew = false;
							}
							catch (Exception e1) {
								logArea.append("Error opening file!!!.\n");
							}
//							try{ImageIO.write(bi, "png", selectedFile);}catch (Exception e1) {}
						}
					  
				  } else if(n == 1) {
					  int returnValue = jfc.showOpenDialog(null);
//						 int returnValue = jfc.showSaveDialog(null);
					  clear();

						if (returnValue == JFileChooser.APPROVE_OPTION) {
							selectedFile = jfc.getSelectedFile();
//							System.out.println(selectedFile.getAbsolutePath());
							try {
								Image imageInput = ImageIO.read(selectedFile);
								BufferedImage bi = (BufferedImage) imageInput;
								Graphics g = bi.createGraphics();
								clear();
								g2.drawImage(imageInput,0,0,null);
								repaint();
								isNew = false;
								logArea.append("File opened sucessfully.\n");
							}
							catch (Exception e1) {
								logArea.append("Error opening file!!!.\n");
							}
//							try{ImageIO.write(bi, "png", selectedFile);}catch (Exception e1) {}
						}
					  logArea.append("File opened sucessfully.\n");
				  } else {}
			  }	else {
				  int returnValue = jfc.showOpenDialog(null);
//					 int returnValue = jfc.showSaveDialog(null);
				  clear();

					if (returnValue == JFileChooser.APPROVE_OPTION) {
						selectedFile = jfc.getSelectedFile();
//						System.out.println(selectedFile.getAbsolutePath());
						try {
							Image imageInput = ImageIO.read(selectedFile);
							BufferedImage bi = (BufferedImage) imageInput;
							Graphics g = bi.createGraphics();
							clear();
							g2.drawImage(imageInput,0,0,null);
							repaint();
							isNew = false;
							logArea.append("File opened sucessfully.\n");
						}
						catch (Exception e1) {}
//						try{ImageIO.write(bi, "png", selectedFile);}catch (Exception e1) {}
					}
				  logArea.append("File opened sucessfully.\n");
			  }
		  }
		  
		  public boolean remotePaint(String shape, Color col, MouseEvent e, int X, int Y) {
			  int oldX = X;
			  int oldY = Y;
			  int xPos = e.getX();
			  int yPos = e.getY();
			  System.out.println("Painting from remote");
			  if (shape.equals("line")){
				  g2.setPaint(col);
			      g2.drawLine(oldX, oldY, xPos, yPos);
			      repaint();
			      isSaved = false;
			      isNew = false;

			  }
			  else if (shape.equals("circle")){
				  g2.setPaint(col);
			      g2.drawOval(oldX, oldY, xPos, yPos);
			      repaint();
			      isSaved = false;
			      isNew = false;
			  }
			  else if (shape.equals("circle")){
				  g2.setPaint(col);
			      g2.drawOval(oldX, oldY, xPos, yPos);
			      repaint();
			      isSaved = false;
			      isNew = false;
			  }
			  else if (shape.equals("rectangle")){
				  g2.setPaint(col);
			      g2.drawRect(oldX, oldY, xPos, yPos);
			      repaint();
			      isSaved = false;
			      isNew = false;
			  }
			  return true;
			  
			  
		  }
		  
		  public void sendMessage() {
			    g2.setPaint(Color.blue);
		  }
		  
		  public void closeCanvas() {
			  if(isNew) {
				  frmSharedWhitboard.dispose();
			  }else if(!isSaved) {
				  Object[] options = {"Save", "Don't Save", "Cancel"};
				  int n = JOptionPane.showOptionDialog(null,
				      "Would you like to save the canvas before creating a new one?",
				      "Save Canvas",
				      JOptionPane.YES_NO_CANCEL_OPTION,
				      JOptionPane.QUESTION_MESSAGE,
				      null,
				      options,
				      options[2]);				  
				  if (n == 0) {
					  saveAsCanvas();
					  isSaved = true;
					  frmSharedWhitboard.dispose();
				  } else if(n == 1) {
					  frmSharedWhitboard.dispose();
				  } else {}
			  }else {
				  frmSharedWhitboard.dispose();
			  }
		  }
		}
}

//***********************

class MyPanel extends JPanel{
	
}

class Msg {
	public Object data;
	public String tag;
	public Msg(String t, Object o) {
		data = o;
		tag = t;
	}
}

class CommHelper extends Thread {
	RMICollaborator collaborator;
	static Vector msgs = new Vector();
	public CommHelper(RMICollaborator c) {
		collaborator = c;
	}

	public static void addMsg(String t, Object o) {
		synchronized (msgs) {
			msgs.addElement(new Msg(t, o));
		}
	}
	public void run() {
		while (true) {
			try {
				Msg m = null;
				synchronized (msgs) {
					m = (Msg)msgs.elementAt(0);
					msgs.removeElementAt(0);
				}
				collaborator.broadcast(m.tag, m.data);
			}catch (Exception e) {}
		}
	}
}



