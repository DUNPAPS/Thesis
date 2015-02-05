package com.sap.on.ibm.i.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class OutputTestEditor extends JFrame implements ActionListener,
		PropertyChangeListener, ItemListener {
	private static final long serialVersionUID = 1L;
	private JTextField sid_field;
	private JTextField user_field;
	private JTextField downloadUrlField = new JTextField(10);
	private JCheckBox stop_SAP_Checkbox;
	private JMenuBar menuBar;
	private JMenu menu1;
	private JMenuItem close;
	private JMenu menu2;
	private JMenuItem infor;
	private JProgressBar progressBar;

	public OutputTestEditor() {
		super("OutputTestEditor");
		buildGUI();
		setIconImage(null);
	}

	/**
	 * Diese Methode setzt die Menue und die Untermenue
	 */
	private void setzeMenue() {
		this.menuBar = new JMenuBar();
		setJMenuBar(this.menuBar);

		this.menu1 = new JMenu("File");
		this.menuBar.add(this.menu1);

		ImageIcon iconClose = new ImageIcon("exit.jpg");
		this.close = new JMenuItem("Exit", iconClose);
		this.menu1.add(this.close);
		this.close.addActionListener(this);
		this.close.setActionCommand("Exit");
		this.close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				Event.CTRL_MASK));

		ImageIcon help = new ImageIcon("icons/help.png");
		this.menu2 = new JMenu("Help");
		this.menuBar.add(this.menu2);
		this.infor = new JMenuItem(null, help);
		this.menu2.add(infor);
		this.infor.addActionListener(this);
		this.infor.setActionCommand("info");

	}

	private void buildGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 795);

		setzeMenue();

		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JPanel panInputLabels = new JPanel(new BorderLayout(0, 5));
		JPanel panInputFields = new JPanel(new BorderLayout(0, 5));

		JPanel settingsPanel = new JPanel();
		settingsPanel.setForeground(new Color(0, 0, 0));
		settingsPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Settings"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		JPanel tasksPanel = new JPanel();
		tasksPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Tasks"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		JPanel panControls = new JPanel(new BorderLayout());
		panControls.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		tasksPanel.setLayout(new GridLayout(0, 1, 0, 0));

		// Checkbox:Tasks
		stop_SAP_Checkbox = new JCheckBox("Stop SAP");
		stop_SAP_Checkbox.setFont(new Font("Arial", Font.PLAIN, 12));
		stop_SAP_Checkbox.addItemListener(this);
		tasksPanel.add(stop_SAP_Checkbox);

		JCheckBox checkboxPanel = new JCheckBox("Apply Kernel");
		checkboxPanel.setFont(new Font("Arial", Font.PLAIN, 12));
		tasksPanel.add(checkboxPanel);

		JCheckBox checkBox = new JCheckBox("Start SAP");
		checkBox.setFont(new Font("Arial", Font.PLAIN, 12));
		tasksPanel.add(checkBox);

		JPanel panUpper = new JPanel(new BorderLayout());
		panUpper.add(settingsPanel, BorderLayout.NORTH);

		JLabel lblNewLabel = new JLabel("Password");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 12));

		sid_field = new JTextField();
		sid_field.setFont(new Font("Arial", Font.PLAIN, 12));
		sid_field.setHorizontalAlignment(SwingConstants.CENTER);
		sid_field.setColumns(10);

		user_field = new JTextField();
		user_field.setHorizontalAlignment(SwingConstants.CENTER);
		user_field.setFont(new Font("Arial", Font.PLAIN, 12));
		user_field.setColumns(10);

		JLabel label = new JLabel("User");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		GroupLayout gl_panInput = new GroupLayout(settingsPanel);
		gl_panInput
				.setHorizontalGroup(gl_panInput
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								gl_panInput
										.createSequentialGroup()
										.addGap(91)
										.addGroup(
												gl_panInput
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_panInput
																		.createSequentialGroup()
																		.addGap(187)
																		.addComponent(
																				panInputLabels,
																				GroupLayout.PREFERRED_SIZE,
																				203,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				panInputFields,
																				GroupLayout.PREFERRED_SIZE,
																				203,
																				GroupLayout.PREFERRED_SIZE))
														.addGroup(
																gl_panInput
																		.createSequentialGroup()
																		.addGroup(
																				gl_panInput
																						.createParallelGroup(
																								Alignment.TRAILING)
																						.addComponent(
																								label,
																								GroupLayout.PREFERRED_SIZE,
																								36,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								lblNewLabel,
																								GroupLayout.PREFERRED_SIZE,
																								111,
																								GroupLayout.PREFERRED_SIZE))
																		.addGap(27)
																		.addGroup(
																				gl_panInput
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								user_field,
																								GroupLayout.PREFERRED_SIZE,
																								177,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								sid_field,
																								GroupLayout.PREFERRED_SIZE,
																								177,
																								GroupLayout.PREFERRED_SIZE))))
										.addGap(178)));
		gl_panInput.setVerticalGroup(gl_panInput.createParallelGroup(
				Alignment.TRAILING).addGroup(
				Alignment.LEADING,
				gl_panInput
						.createSequentialGroup()
						.addGroup(
								gl_panInput
										.createParallelGroup(Alignment.LEADING)
										.addComponent(panInputLabels,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(panInputFields,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(
								gl_panInput
										.createParallelGroup(
												Alignment.TRAILING, false)
										.addComponent(label, Alignment.LEADING,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(sid_field,
												Alignment.LEADING,
												GroupLayout.DEFAULT_SIZE, 42,
												Short.MAX_VALUE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(
								gl_panInput
										.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel,
												GroupLayout.DEFAULT_SIZE, 26,
												Short.MAX_VALUE)
										.addComponent(user_field,
												GroupLayout.DEFAULT_SIZE, 42,
												Short.MAX_VALUE))
						.addContainerGap()));
		settingsPanel.setLayout(gl_panInput);

		contentPane.add(panUpper, BorderLayout.NORTH);
		panUpper.add(tasksPanel, BorderLayout.SOUTH);
		contentPane.add(logViewerPanel(), BorderLayout.CENTER);

	}

	private JPanel logViewerPanel() {
		JPanel main = new JPanel(new BorderLayout());
		JToolBar toolBar = new JToolBar();
		toolBar.setBorder(new EtchedBorder());

		JCheckBox chckbxInf = new JCheckBox("Info");
		chckbxInf.setFont(new Font("Arial", Font.PLAIN, 12));
		toolBar.add(chckbxInf);

		JCheckBox chckbxError = new JCheckBox("Error");
		chckbxError.setFont(new Font("Arial", Font.PLAIN, 12));
		toolBar.add(chckbxError);

		JCheckBox chckbxDebug = new JCheckBox("Debug");
		chckbxDebug.setFont(new Font("Arial", Font.PLAIN, 12));
		toolBar.add(chckbxDebug);

		ImageIcon copyicon = new ImageIcon("icons/page_copy.png");
		JButton copy = new JButton();
		copy.setIcon(copyicon);
		copy.setPreferredSize(new Dimension(toolBar.getWidth(), 16));
		toolBar.add(copy);

		JButton clearLogViewButton = new JButton("Clear Log Viewer");
		clearLogViewButton.setFont(new Font("Arial", Font.PLAIN, 12));
		clearLogViewButton.setPreferredSize(new Dimension(toolBar.getWidth(),
				16));
		toolBar.add(clearLogViewButton);

		JPanel northPanel = new JPanel();
		main.add(toolBar, BorderLayout.PAGE_START);
		main.add(northPanel.add(new JLabel("File to Download:")));
		main.add(northPanel.add(downloadUrlField));
		main.add(northPanel.add(Box.createHorizontalStrut(15)));

		Font myFont = new Font("Arial", Font.BOLD, 12);
		Color myColor = Color.BLACK;
		TitledBorder titledBorder = BorderFactory.createTitledBorder(null,
				" Log Viewer", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, myFont, myColor);
		JScrollPane eastSPane = new JScrollPane();
		eastSPane.setPreferredSize(new Dimension(200, 100));

		// create the status bar panel and shove it down the bottom of the panel
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setPreferredSize(new Dimension(main.getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		JLabel statusLabel = new JLabel("Running Tasks");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);

		main.setBorder(titledBorder);
		main.add(new JScrollPane(new JTextArea(20, 30)), BorderLayout.CENTER);
		main.add(eastSPane, BorderLayout.EAST);
		main.add(statusPanel, BorderLayout.SOUTH);

		JPanel panel = new JPanel();
		eastSPane.setViewportView(panel);
		panel.setLayout(null);

		JButton button = new JButton("Run Tasks");
		button.setFont(new Font("Arial", Font.BOLD, 12));
		button.setBounds(28, 30, 142, 65);
		panel.add(button);

		JButton btnStoptasks = new JButton("Stop");
		btnStoptasks.setFont(new Font("Arial", Font.BOLD, 12));
		btnStoptasks.setBounds(28, 106, 142, 65);
		panel.add(btnStoptasks);

		return main;

	}

	@SuppressWarnings("unused")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress".equals(evt.getPropertyName())) {
			int progress = (Integer) evt.getNewValue();
			// progressAll.setValue(progress);
		}

	}

	// @Override
	// public void actionPerformed(ActionEvent e) {
	// if (this.btnStart == e.getSource()) {
	// downloadAction();
	// }
	// if (this.newMenuItem == e.getSource()) {
	// System.exit(0);
	// }
	// if (this.stop_SAP_Checkbox == e.getSource()) {
	//
	// }
	// }
	//
	// @SuppressWarnings("unchecked")
	// public void downloadAction() {
	// String downloadUrl = downloadUrlField.getText();
	// final MyData myData = new MyData(downloadUrl);
	// myListModel.addElement(myData);
	// myData.addPropertyChangeListener(new PropertyChangeListener() {
	// public void propertyChange(PropertyChangeEvent evt) {
	// if (evt.getPropertyName().equals(MyData.VALUE)) {
	// myList.repaint();
	// if (myData.getValue() >= 100) {
	// myListModel.removeElement(myData);
	// System.out.println("Done");
	// }
	// }
	// }
	//
	// });
	// }

	@Override
	public void itemStateChanged(ItemEvent event) {
		boolean b = this.stop_SAP_Checkbox.isSelected();
		System.out.println(b);
	}

	/**
	 * 
	 * Main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new OutputTestEditor().setVisible(true);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}