package server;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileSystemView;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

class BufferedImage2 extends BufferedImage implements Serializable{

	public BufferedImage2(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied,
			Hashtable<?, ?> properties) {
		super(cm, raster, isRasterPremultiplied, properties);
		// TODO Auto-generated constructor stub
	}
}


public class Canvas {

	private JFrame frame;
	private Color col; 
	private int brushSize;
	private String[] brushSizeList = {"1","2","3","4","5","6","8","10","12","14","16","20","24","32","48"};
	BufferedImage biOpen;
	
	JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Canvas window = new Canvas();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Canvas() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 896, 557);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
//		JPanel panel = new JPanel();
//		panel.setBackground(Color.WHITE);
//		panel.setBounds(149, 16, 531, 483);
//		frame.getContentPane().add(panel);
		TestPane panel = new TestPane();
		BufferedImage bi = new BufferedImage(frame.getSize().width, frame.getSize().height, BufferedImage.TYPE_INT_ARGB); 
		
//		frame.getContentPane().add(panel);
//		frame.add(panel);
//        frame.pack();
//        frame.setLocationRelativeTo(null);
		
//		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(149, 16, 531, 483);
		frame.getContentPane().add(panel);
		
		JButton btnColor = new JButton("Color");
		btnColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				col = JColorChooser.showDialog(null, "Choose a color", Color.RED);
			}
		});
		btnColor.setBounds(20, 16, 117, 29);
		frame.getContentPane().add(btnColor);
		
		JButton btnLine = new JButton("Line");
		btnLine.setBounds(20, 290, 117, 29);
		frame.getContentPane().add(btnLine);
		
		JButton btnCircle = new JButton("Circle");
		btnCircle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnCircle.setBounds(20, 317, 117, 29);
		frame.getContentPane().add(btnCircle);
		
		JButton btnOval = new JButton("Oval");
		btnOval.setBounds(20, 344, 117, 29);
		frame.getContentPane().add(btnOval);
		
		JButton btnSquare = new JButton("Square");
		btnSquare.setBounds(20, 372, 117, 29);
		frame.getContentPane().add(btnSquare);
		
//		JComboBox<String> comboBox = new JComboBox<String>(brushSizeList);
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(20, 441, 117, 27);
		frame.getContentPane().add(comboBox);
		
		JButton btnSetBrushSize = new JButton("Set Brush Size");
		btnSetBrushSize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				brushSize = Integer.parseInt((String) comboBox.getSelectedItem());
			}
		});
		btnSetBrushSize.setBounds(18, 470, 117, 29);
		frame.getContentPane().add(btnSetBrushSize);
		
		JLabel lblBrushSize = new JLabel("Brush Size");
		lblBrushSize.setHorizontalAlignment(SwingConstants.CENTER);
		lblBrushSize.setBounds(18, 413, 119, 16);
		frame.getContentPane().add(lblBrushSize);
		
		JLabel lblShapes = new JLabel("Shapes");
		lblShapes.setHorizontalAlignment(SwingConstants.CENTER);
		lblShapes.setBounds(18, 262, 119, 16);
		frame.getContentPane().add(lblShapes);
		
		JButton btnEraser = new JButton("Eraser");
		btnEraser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				col = Color.WHITE;
			}
		});
		btnEraser.setBounds(20, 41, 117, 29);
		frame.getContentPane().add(btnEraser);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Component[] savePanel = panel.getComponents();
				System.out.println(savePanel);
				Graphics g = bi.createGraphics();
				panel.paint(g);  //this == JComponent
				g.dispose();
//				try{ImageIO.write(bi,"png",new File("test.png"));}catch (Exception e1) {}
//				int returnValue = jfc.showOpenDialog(null);
				 int returnValue = jfc.showSaveDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
//					System.out.println(selectedFile.getAbsolutePath());
					try {
						FileOutputStream fileOut = new FileOutputStream(selectedFile);
						ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
						objectOut.writeObject(bi);
						objectOut.close();
					
					}
					catch (Exception e1) {}
//					try{ImageIO.write(bi, "png", selectedFile);}catch (Exception e1) {}
				}
			}
		});
		btnSave.setBounds(20, 99, 117, 29);
		frame.getContentPane().add(btnSave);
		
		JButton btnOpen = new JButton("Open");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnValue = jfc.showOpenDialog(null);
//				 int returnValue = jfc.showSaveDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
//					System.out.println(selectedFile.getAbsolutePath());
					try {
						FileInputStream fileOut = new FileInputStream(selectedFile);
						ObjectInputStream objectOut = new ObjectInputStream(fileOut);
						BufferedImage bi = (BufferedImage) objectOut.readObject();
						Graphics g = bi.createGraphics();
						panel.paint(g);
						g.dispose();
						objectOut.close();
					
					}
					catch (Exception e1) {
						System.out.println(e1.getMessage());
					}
//					try{ImageIO.write(bi, "png", selectedFile);}catch (Exception e1) {}
				}
			}
		});
		btnOpen.setBounds(20, 127, 117, 29);
		frame.getContentPane().add(btnOpen);
		
		JButton btnNew = new JButton("New");
		btnNew.setBounds(20, 156, 117, 29);
		frame.getContentPane().add(btnNew);
		
		JTextArea textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setBounds(692, 16, 187, 400);
		frame.getContentPane().add(textArea);
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setWrapStyleWord(true);
		textArea_1.setLineWrap(true);
		textArea_1.setBounds(692, 423, 187, 76);
		frame.getContentPane().add(textArea_1);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(762, 500, 117, 29);
		frame.getContentPane().add(btnSend);
	}
	
	public class TestPane extends JPanel {

        private List<List<Point>> points;

        public TestPane() {
            points = new ArrayList<>(25);
            MouseAdapter ma = new MouseAdapter() {

                private List<Point> currentPath;  // = new ArrayList<>();
//                currentPath = new ArrayList<>();


                @Override
                public void mousePressed(MouseEvent e) {
                    currentPath = new ArrayList<>();
                    currentPath.add(e.getPoint());

                    points.add(currentPath);
//                    repaint();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    Point dragPoint = e.getPoint();
                    currentPath.add(dragPoint);
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    currentPath = null;
                }

            };

            addMouseListener(ma);
            addMouseMotionListener(ma);
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            
//            g2d.fillRect(getX(), getY(), 1, 1);
            for (List<Point> path : points) {
            	Point from = null;
                for (Point p : path) {
                    if (from != null) {
                    	
//                    	g2d.
                        
                       	g2d.setColor(col);
                    	g2d.setStroke(new BasicStroke(brushSize));
                    	g2d.drawLine(from.x, from.y, p.x, p.y);
//                      g2d.drawRect(from.x, from.y, 1, 1);
                    	
                    	
//                        g2d.fillRect(from.x, from.y, 5, 5);
                    }
                    from = p;
                }
//                g2d.dispose();
            }
            g2d.dispose();
        }

    }
}
