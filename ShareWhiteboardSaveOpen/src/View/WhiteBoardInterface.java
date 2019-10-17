package View;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import javax.swing.JMenuBar;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Component;
import javax.swing.Box;
import java.awt.TextArea;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.EmptyBorder;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JInternalFrame;
import java.awt.Canvas;
import java.awt.GridBagConstraints;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class WhiteBoardInterface {

	private JFrame frmSharedWhitboard;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmNew;
	private JMenuItem mntmOpen;
	private JMenuItem mntmSave;
	private JMenuItem mntmSaveAs;
	private JMenuItem mntmClose;
	private JSeparator separator;
	private JPanel canvas_panel;
	private JPanel secondary_panel;
	private JSeparator separator_2;
	private JTextField textField;
	private JSeparator separator_4;
	private JSeparator separator_5;
	private JSeparator separator_1;
	private JButton btnRectangle;
	private JButton btnOval;
	private JButton btnEraser;
	private JButton btnColor;
	private JButton btnClear;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WhiteBoardInterface window = new WhiteBoardInterface();
					window.frmSharedWhitboard.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WhiteBoardInterface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSharedWhitboard = new JFrame();
		frmSharedWhitboard.setTitle("Shared Whiteboard");
		frmSharedWhitboard.setBounds(100, 100, 1118, 764);
		frmSharedWhitboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menuBar = new JMenuBar();
		frmSharedWhitboard.setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmNew = new JMenuItem("New");
		mnFile.add(mntmNew);
		
		mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		
		mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		mntmSaveAs = new JMenuItem("Save As...");
		mnFile.add(mntmSaveAs);
		
		separator = new JSeparator();
		mnFile.add(separator);
		
		mntmClose = new JMenuItem("Close");
		mnFile.add(mntmClose);
		
		JPanel status_panel = new JPanel();
		status_panel.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY), new EmptyBorder(4, 4, 4, 4)));
		final JLabel status = new JLabel();
		status.setHorizontalAlignment(SwingConstants.CENTER);
		status_panel.add(status);
		
		canvas_panel = new JPanel();
		
		JPanel tools_panel = new JPanel();
		
		secondary_panel = new JPanel();
		
		separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);

		GroupLayout groupLayout = new GroupLayout(frmSharedWhitboard.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(tools_panel, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(canvas_panel, GroupLayout.PREFERRED_SIZE, 703, GroupLayout.PREFERRED_SIZE)
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
						.addComponent(canvas_panel, GroupLayout.PREFERRED_SIZE, 662, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(status_panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(6))
		);
		canvas_panel.setLayout(new BorderLayout(0, 0));
		
		separator_2 = new JSeparator();
		
		JTextArea chatArea = new JTextArea();
		chatArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chatArea.setEditable(false);
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setEditable(false);
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
					.addComponent(textArea_1, GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_secondary_panel.setVerticalGroup(
			gl_secondary_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_secondary_panel.createSequentialGroup()
					.addComponent(textArea_1, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
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
		
		JSeparator separator_3 = new JSeparator();
		
		separator_4 = new JSeparator();
		
		separator_5 = new JSeparator();
		
		JButton btnBrush = new JButton("Brush");
		
		JButton btnLine = new JButton("Line");
		
		JComboBox comboBoxSize = new JComboBox();
		
		JButton btnSelSize = new JButton("Size");
		
		JButton btnCircle = new JButton("Circle");
		btnCircle.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		btnRectangle = new JButton("Rectangle");
		btnRectangle.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		btnOval = new JButton("Oval");
		btnOval.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		btnEraser = new JButton("Eraser");
		btnEraser.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		btnColor = new JButton("Color");
		
		btnClear = new JButton("Clear");
		GroupLayout gl_tools_panel = new GroupLayout(tools_panel);
		gl_tools_panel.setHorizontalGroup(
			gl_tools_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_tools_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tools_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnCircle, GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
						.addComponent(separator_5, GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
						.addComponent(btnBrush, GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
						.addComponent(btnOval, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxSize, Alignment.TRAILING, 0, 68, Short.MAX_VALUE)
						.addComponent(btnSelSize, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
						.addComponent(separator_3, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
						.addComponent(btnRectangle, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnEraser, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnLine, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
						.addComponent(separator_4, GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
						.addComponent(btnColor, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
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
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSelSize)
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
					.addComponent(separator_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnColor)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnClear)
					.addPreferredGap(ComponentPlacement.RELATED, 178, Short.MAX_VALUE)
					.addComponent(separator_5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(108))
		);
		tools_panel.setLayout(gl_tools_panel);
		frmSharedWhitboard.getContentPane().setLayout(groupLayout);
	}
}

//*************




