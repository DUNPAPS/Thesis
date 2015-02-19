package com.sap.on.ibm.i.editor.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
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
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class OutputTestEditor extends JFrame {
	private JPasswordField sid_field;
	private JPasswordField password_field;
	private JCheckBox stop_SAP_Checkbox;
	private JCheckBox applyKernelCheckbox;
	private JCheckBox startSAPCheckBox;
	private JMenuBar menuBar;
	private JMenu file;
	private JMenuItem exitJMenuItem;
	private JMenu info;
	private JMenuItem inforJMenuItem;
	private JButton copy;
	private JButton clearLogViewButton;
	private JButton playButton;
	private JButton stopButton;
	private JScrollPane scrollPane;
	private JTextPane jtextArea;
	private JProgressBar jProgressBar;
	private JMenuItem importScriptJMenuItem;

	public OutputTestEditor() {
		super("OutputTestEditor");
			buildGUI();
	}

	private void setzeMenue() {
		this.menuBar = new JMenuBar();
		setJMenuBar(this.menuBar);

		this.file = new JMenu("File");
		this.menuBar.add(this.file);

		this.importScriptJMenuItem = new JMenuItem("Import Script");
		this.file.add(importScriptJMenuItem);
		this.importScriptJMenuItem.setActionCommand("Import");
		this.file.addSeparator();
		ImageIcon iconClose = new ImageIcon("exit.jpg");
		this.exitJMenuItem = new JMenuItem("Exit", iconClose);
		this.file.add(this.exitJMenuItem);
		this.exitJMenuItem.setActionCommand("Exit");
		this.exitJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				Event.CTRL_MASK));

		ImageIcon help = new ImageIcon("icons/help.png");
		this.info = new JMenu("Help");
		this.menuBar.add(this.info);
		this.inforJMenuItem = new JMenuItem(null, help);
		this.info.add(inforJMenuItem);
		this.inforJMenuItem.setActionCommand("info");

	}

	private void buildGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1107, 713);

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
		tasksPanel.add(stop_SAP_Checkbox);

		applyKernelCheckbox = new JCheckBox("Apply Kernel");
		applyKernelCheckbox.setFont(new Font("Arial", Font.PLAIN, 12));
		tasksPanel.add(applyKernelCheckbox);

		startSAPCheckBox = new JCheckBox("Start SAP");
		startSAPCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
		tasksPanel.add(startSAPCheckBox);

		// Settings
		JPanel panUpper = new JPanel(new BorderLayout());
		panUpper.add(settingsPanel, BorderLayout.NORTH);

		JLabel lblNewLabel = new JLabel("Password");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 12));

		sid_field = new JPasswordField();
		this.sid_field.setText("bigboss");
		this.sid_field.setEchoChar('*');

		sid_field.setFont(new Font("Arial", Font.PLAIN, 12));
		sid_field.setHorizontalAlignment(SwingConstants.CENTER);
		sid_field.setColumns(10);

		password_field = new JPasswordField();
		this.password_field.setText("qsecofer");
		this.password_field.setEchoChar('*');
		password_field.setHorizontalAlignment(SwingConstants.CENTER);
		password_field.setFont(new Font("Arial", Font.PLAIN, 12));
		password_field.setColumns(10);

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
										.addGroup(
												gl_panInput
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_panInput
																		.createSequentialGroup()
																		.addGap(278)
																		.addComponent(
																				panInputLabels,
																				GroupLayout.PREFERRED_SIZE,
																				203,
																				GroupLayout.PREFERRED_SIZE))
														.addGroup(
																gl_panInput
																		.createSequentialGroup()
																		.addContainerGap()
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
																								password_field,
																								GroupLayout.PREFERRED_SIZE,
																								177,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								sid_field,
																								GroupLayout.PREFERRED_SIZE,
																								177,
																								GroupLayout.PREFERRED_SIZE))))
										.addComponent(panInputFields,
												GroupLayout.PREFERRED_SIZE,
												203, GroupLayout.PREFERRED_SIZE)
										.addContainerGap(192, Short.MAX_VALUE)));
		gl_panInput.setVerticalGroup(gl_panInput.createParallelGroup(
				Alignment.TRAILING).addGroup(
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
						.addPreferredGap(ComponentPlacement.RELATED)
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
												GroupLayout.PREFERRED_SIZE, 42,
												GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(
								gl_panInput
										.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel,
												GroupLayout.DEFAULT_SIZE, 43,
												Short.MAX_VALUE)
										.addComponent(password_field,
												GroupLayout.DEFAULT_SIZE, 43,
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
		copy = new JButton();
		copy.setIcon(copyicon);
		copy.setPreferredSize(new Dimension(toolBar.getWidth(), 16));
		toolBar.add(copy);

		clearLogViewButton = new JButton("Clear Log Viewer");
		clearLogViewButton.setFont(new Font("Arial", Font.PLAIN, 12));
		clearLogViewButton.setPreferredSize(new Dimension(toolBar.getWidth(),
				16));
		toolBar.add(clearLogViewButton);

		ImageIcon playicon = new ImageIcon("icons/control_play_blue.png");
		playButton = new JButton("Play");
		playButton.setIcon(playicon);
		playButton.setFont(new Font("Arial", Font.PLAIN, 12));
		playButton.setPreferredSize(new Dimension(toolBar.getWidth(), 16));
		toolBar.add(playButton);

		ImageIcon stopicon = new ImageIcon("icons/stop.png");
		stopButton = new JButton("Stop");
		stopButton.setIcon(stopicon);
		stopButton.setFont(new Font("Arial", Font.PLAIN, 12));
		stopButton.setPreferredSize(new Dimension(toolBar.getWidth(), 16));
		toolBar.add(stopButton);

		Font myFont = new Font("Arial", Font.BOLD, 12);
		Color myColor = Color.BLACK;
		TitledBorder titledBorder = BorderFactory.createTitledBorder(null,
				" Log Viewer", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, myFont, myColor);

		// create the status bar panel and shove it down the bottom of the panel
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setPreferredSize(new Dimension(main.getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));

		jProgressBar = new JProgressBar();
		statusPanel.add(jProgressBar);

		main.setBorder(titledBorder);
		main.add(toolBar, BorderLayout.PAGE_START);
		jtextArea = new JTextPane();
		// jtextArea.setBorder(border);

		scrollPane = new JScrollPane(jtextArea);
		main.add(scrollPane, BorderLayout.CENTER);

		// ProgressBar
		JPanel barPanel = new JPanel();
		scrollPane.setRowHeaderView(barPanel);
		barPanel.setPreferredSize(new Dimension(toolBar.getWidth(), 16));
		main.add(statusPanel, BorderLayout.SOUTH);

		return main;

	}

	public String getSID() {
		String sid = new String(this.sid_field.getPassword()).trim();
		return sid;
	}

	public String getPassword() {
		String password = new String(this.password_field.getPassword()).trim();
		return password;
	}

	public void setclearLogViewButtonActionListener(ActionListener l) {
		this.clearLogViewButton.addActionListener(l);
	}

	public void setcopyActionListener(ActionListener l) {
		this.copy.addActionListener(l);
	}

	public void setplayButtonActionListener(ActionListener l) {
		this.playButton.addActionListener(l);
	}

	public void removePlayButtonActionListener(ActionListener l) {
		this.playButton.removeActionListener(l);
	}

	public void setstopButtonActionListener(ActionListener l) {
		this.stopButton.addActionListener(l);
	}

	public void setexitJMenuItemActionListener(ActionListener l) {
		this.exitJMenuItem.addActionListener(l);
	}

	public void setinforJMenuItemActionListener(ActionListener l) {
		this.inforJMenuItem.addActionListener(l);
	}

	public void setstop_SAP_CheckboxActionListener(ActionListener l) {
		this.stop_SAP_Checkbox.addActionListener(l);
	}

	public void setapplyKernelCheckboxActionListener(ActionListener l) {
		this.applyKernelCheckbox.addActionListener(l);
	}

	public void setstartSAPCheckBoxActionListener(ActionListener l) {
		this.startSAPCheckBox.addActionListener(l);
	}

	public JTextField getSid_field() {
		return sid_field;
	}

	public JTextField getPassword_field() {
		return password_field;
	}

	public JCheckBox getStop_SAP_Checkbox() {
		return stop_SAP_Checkbox;
	}

	public JCheckBox getApplyKernelCheckbox() {
		return applyKernelCheckbox;
	}

	public JCheckBox getStartSAPCheckBox() {
		return startSAPCheckBox;
	}

	public JMenu getFile() {
		return file;
	}

	public JMenuItem getExitJMenuItem() {
		return exitJMenuItem;
	}

	public JMenu getInfo() {
		return info;
	}

	public JMenuItem getInforJMenuItem() {
		return inforJMenuItem;
	}

	public JButton getCopy() {
		return copy;
	}

	public JButton getClearLogViewButton() {
		return clearLogViewButton;
	}

	public JButton getPlayButton() {
		return playButton;
	}

	public JButton getStopButton() {
		return stopButton;
	}

	public JTextPane getJtextArea() {
		return jtextArea;
	}

	public JProgressBar getjProgressBar() {
		return jProgressBar;
	}

	public JMenuItem getImportScriptJMenuItem() {
		return importScriptJMenuItem;
	}
	public void setImportScriptJMenuItemActionListener(ActionListener l) {
		this.importScriptJMenuItem.addActionListener(l);
	}
}
