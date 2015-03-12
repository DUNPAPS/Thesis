package com.sap.on.ibm.i.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
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
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class HATestEditor extends JFrame {
	private JCheckBox stop_SAP_Checkbox;
	private JCheckBox applyKernelCheckbox;
	private JCheckBox startHATestSAPCheckBox;
	private JCheckBox start_SAP_CheckBox;
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
	private JProgressBar jProgressBar;
	private JMenuItem importScriptJMenuItem;
	private JPanel panel;
	private JLabel sap_global_KernelLabel;
	private JLabel sap_SID_label;
	private JTextPane jtextArea;
	private JTextField sap_SID_Field;
	private JTextField sap_USER_Field;
	private JTextField sap_Nigtly_MakeField;
	private JPasswordField sap_PASSWORD_Field;
	private JTextField sap_global_KernelField;
	private JLabel label;
	private JLabel statusBarJLabel;

	public HATestEditor() {
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
		setSize(1500, 850);
		setzeMenue();

		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JPanel panInputLabels = new JPanel(new BorderLayout(0, 5));
		JPanel panInputFields = new JPanel(new BorderLayout(0, 5));

		JPanel settingsPanel = new JPanel();
		settingsPanel.setForeground(new Color(0, 0, 0));
		settingsPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("SAP System information"),
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

		start_SAP_CheckBox = new JCheckBox("Start SAP");
		start_SAP_CheckBox.setFont(new Font("Tahoma", Font.PLAIN, 11));
		tasksPanel.add(start_SAP_CheckBox);

		startHATestSAPCheckBox = new JCheckBox("RunHATest");
		startHATestSAPCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
		tasksPanel.add(startHATestSAPCheckBox);

		// Settings
		JPanel panUpper = new JPanel(new BorderLayout());
		panUpper.add(settingsPanel, BorderLayout.NORTH);

		JLabel sapPasswordLabel = new JLabel("SAP_PASSWORD");
		sapPasswordLabel.setHorizontalAlignment(SwingConstants.LEFT);
		sapPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 12));

		sap_PASSWORD_Field = new JPasswordField();
		sap_PASSWORD_Field.setHorizontalAlignment(SwingConstants.LEFT);
		sap_PASSWORD_Field.setFont(new Font("Arial", Font.PLAIN, 12));
		sap_PASSWORD_Field.setEchoChar('*');
		sap_PASSWORD_Field.setColumns(10);

		JLabel Sap_SID_label = new JLabel("SAP_USER");
		Sap_SID_label.setHorizontalAlignment(SwingConstants.LEFT);
		Sap_SID_label.setFont(new Font("Arial", Font.PLAIN, 12));

		sap_USER_Field = new JTextField();
		sap_USER_Field.setHorizontalAlignment(SwingConstants.LEFT);
		sap_USER_Field.setFont(new Font("Arial", Font.PLAIN, 12));
		sap_USER_Field.setColumns(10);

		panel = new JPanel();
		panel.setBorder(BorderFactory.createCompoundBorder(

		BorderFactory.createTitledBorder("Kernel location"),

		BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		sap_SID_label = new JLabel("SAP_SID");
		sap_SID_label.setHorizontalAlignment(SwingConstants.LEFT);
		sap_SID_label.setFont(new Font("Arial", Font.PLAIN, 12));

		sap_SID_Field = new JTextField();
		sap_SID_Field.setHorizontalAlignment(SwingConstants.LEFT);
		sap_SID_Field.setFont(new Font("Arial", Font.PLAIN, 12));
		sap_SID_Field.setColumns(10);
		GroupLayout gl_panInput = new GroupLayout(settingsPanel);
		gl_panInput
				.setHorizontalGroup(gl_panInput
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panInput
										.createSequentialGroup()
										.addGroup(
												gl_panInput
														.createParallelGroup(
																Alignment.LEADING,
																false)
														.addGroup(
																gl_panInput
																		.createSequentialGroup()
																		.addContainerGap()
																		.addComponent(
																				sapPasswordLabel,
																				GroupLayout.PREFERRED_SIZE,
																				111,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				sap_PASSWORD_Field))
														.addGroup(
																gl_panInput
																		.createSequentialGroup()
																		.addGap(19)
																		.addGroup(
																				gl_panInput
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								sap_SID_label,
																								GroupLayout.PREFERRED_SIZE,
																								97,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								Sap_SID_label,
																								GroupLayout.PREFERRED_SIZE,
																								93,
																								GroupLayout.PREFERRED_SIZE))
																		.addPreferredGap(
																				ComponentPlacement.UNRELATED)
																		.addGroup(
																				gl_panInput
																						.createParallelGroup(
																								Alignment.TRAILING,
																								false)
																						.addComponent(
																								sap_USER_Field)
																						.addComponent(
																								sap_SID_Field,
																								GroupLayout.DEFAULT_SIZE,
																								201,
																								Short.MAX_VALUE))))
										.addPreferredGap(
												ComponentPlacement.RELATED,
												294, Short.MAX_VALUE)
										.addComponent(panel,
												GroupLayout.PREFERRED_SIZE,
												807, GroupLayout.PREFERRED_SIZE)
										.addGap(34))
						.addGroup(
								gl_panInput
										.createSequentialGroup()
										.addGap(278)
										.addComponent(panInputLabels,
												GroupLayout.PREFERRED_SIZE,
												203, GroupLayout.PREFERRED_SIZE)
										.addGap(640)
										.addComponent(panInputFields,
												GroupLayout.PREFERRED_SIZE,
												203, GroupLayout.PREFERRED_SIZE)
										.addContainerGap(138, Short.MAX_VALUE)));
		gl_panInput
				.setVerticalGroup(gl_panInput
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								gl_panInput
										.createSequentialGroup()
										.addGroup(
												gl_panInput
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																panInputLabels,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																panInputFields,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addGroup(
												gl_panInput
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_panInput
																		.createSequentialGroup()
																		.addGap(20)
																		.addGroup(
																				gl_panInput
																						.createParallelGroup(
																								Alignment.TRAILING)
																						.addComponent(
																								Sap_SID_label,
																								GroupLayout.PREFERRED_SIZE,
																								35,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								sap_USER_Field,
																								GroupLayout.PREFERRED_SIZE,
																								37,
																								GroupLayout.PREFERRED_SIZE))
																		.addPreferredGap(
																				ComponentPlacement.UNRELATED)
																		.addGroup(
																				gl_panInput
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								sap_SID_label,
																								GroupLayout.PREFERRED_SIZE,
																								38,
																								GroupLayout.PREFERRED_SIZE)
																						.addGroup(
																								gl_panInput
																										.createSequentialGroup()
																										.addGap(1)
																										.addComponent(
																												sap_SID_Field,
																												GroupLayout.PREFERRED_SIZE,
																												37,
																												GroupLayout.PREFERRED_SIZE)))
																		.addPreferredGap(
																				ComponentPlacement.UNRELATED)
																		.addGroup(
																				gl_panInput
																						.createParallelGroup(
																								Alignment.BASELINE)
																						.addComponent(
																								sapPasswordLabel,
																								GroupLayout.DEFAULT_SIZE,
																								33,
																								Short.MAX_VALUE)
																						.addComponent(
																								sap_PASSWORD_Field,
																								GroupLayout.PREFERRED_SIZE,
																								37,
																								GroupLayout.PREFERRED_SIZE)))
														.addComponent(
																panel,
																GroupLayout.DEFAULT_SIZE,
																154,
																Short.MAX_VALUE))
										.addContainerGap()));
		panel.setLayout(null);

		sap_Nigtly_MakeField = new JTextField();
		sap_Nigtly_MakeField.setHorizontalAlignment(SwingConstants.LEFT);
		sap_Nigtly_MakeField.setFont(new Font("Arial", Font.PLAIN, 12));
		sap_Nigtly_MakeField.setColumns(10);
		sap_Nigtly_MakeField.setBounds(127, 23, 643, 37);
		panel.add(sap_Nigtly_MakeField);

		sap_global_KernelLabel = new JLabel("SAP_Global_Kernel");
		sap_global_KernelLabel.setHorizontalAlignment(SwingConstants.LEFT);
		sap_global_KernelLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		sap_global_KernelLabel.setBounds(8, 86, 109, 38);
		panel.add(sap_global_KernelLabel);

		label = new JLabel("SAP_Nigtly Make");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		label.setBounds(8, 22, 109, 38);
		panel.add(label);

		sap_global_KernelField = new JTextField();
		sap_global_KernelField.setHorizontalAlignment(SwingConstants.LEFT);
		sap_global_KernelField.setFont(new Font("Arial", Font.PLAIN, 12));
		sap_global_KernelField.setColumns(10);
		sap_global_KernelField.setBounds(127, 87, 643, 37);
		panel.add(sap_global_KernelField);

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
		chckbxInf.setEnabled(false);
		toolBar.add(chckbxInf);

		JCheckBox chckbxError = new JCheckBox("Error");
		chckbxError.setFont(new Font("Arial", Font.PLAIN, 12));
		chckbxError.setEnabled(false);
		toolBar.add(chckbxError);

		JCheckBox chckbxDebug = new JCheckBox("Debug");
		chckbxDebug.setFont(new Font("Arial", Font.PLAIN, 12));
		chckbxDebug.setEnabled(false);
		toolBar.add(chckbxDebug);

		ImageIcon copyicon = new ImageIcon("icons/page_copy.png");
		copy = new JButton();
		copy.setIcon(copyicon);
		copy.setPreferredSize(new Dimension(toolBar.getWidth(), 16));
		copy.setEnabled(false);
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

		// create the status bar panel and shove it down the bottom of the panel
		JPanel statusPanel = new JPanel(new BorderLayout());

		jProgressBar = new JProgressBar();
		jProgressBar.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
		jProgressBar.setPreferredSize(new Dimension(50, 20));
		jProgressBar.setBackground(new Color(255, 255, 255));
		jProgressBar.setFont(new Font("Arial", Font.PLAIN, 18));
		jProgressBar.setForeground(new Color(070, 130, 180));
		statusPanel.add(jProgressBar, BorderLayout.NORTH);

		// status Bar JLabel
		statusBarJLabel = new JLabel();
		statusPanel.add(statusBarJLabel, BorderLayout.WEST);

		main.add(toolBar, BorderLayout.PAGE_START);
		jtextArea = new JTextPane();

		scrollPane = new JScrollPane(jtextArea);
		main.add(scrollPane, BorderLayout.CENTER);

		main.add(statusPanel, BorderLayout.PAGE_END);

		return main;

	}

	public String getUSER() {
		String user = sap_USER_Field.getText().trim();
		return user;
	}

	public String getSID() {
		String sid = sap_SID_Field.getText().trim();
		return sid;
	}

	public String getPassword() {
		String password = new String(this.sap_PASSWORD_Field.getPassword())
				.trim();
		return password;
	}

	public void removePlayButtonActionListener(ActionListener l) {
		this.playButton.removeActionListener(l);
	}

	public JCheckBox getStop_SAP_Checkbox() {
		return stop_SAP_Checkbox;
	}

	public JCheckBox getApplyKernelCheckbox() {
		return applyKernelCheckbox;
	}

	public JCheckBox getStartSAPCheckBox() {
		return startHATestSAPCheckBox;
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

	public JTextField getSap_SID_Field() {
		return sap_SID_Field;
	}

	public JTextField getSap_USER_Field() {
		return sap_USER_Field;
	}

	public JPasswordField getSap_PASSWORD_Field() {
		return sap_PASSWORD_Field;
	}

	public JLabel getStatusBarJLabel() {
		return statusBarJLabel;
	}

	public JTextField getSap_Nigtly_MakeField() {
		return sap_Nigtly_MakeField;
	}

	public JTextField getSap_global_KernelField() {
		return sap_global_KernelField;
	}

	public JCheckBox getStart_SAP_CheckBox() {
		return start_SAP_CheckBox;
	}

	public JCheckBox getStartHATestSAPCheckBox() {
		return startHATestSAPCheckBox;
	}
}
