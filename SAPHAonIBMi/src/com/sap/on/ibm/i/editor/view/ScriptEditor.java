package com.sap.on.ibm.i.editor.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

import com.sap.on.ibm.i.editor.controller.Controller;

import java.io.*;

@SuppressWarnings("serial")
public class ScriptEditor extends JFrame {

	private JFileChooser fileChooser;
	private JTextArea textarea = new JTextArea(10, 40);
	private Controller controller;
	private File file;
	private boolean waitCursorIsShowing;

	public ScriptEditor(Controller controller) {
		this.controller = controller;
		this.setTitle("Run Script");
		getContentPane().add(initComponents());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationByPlatform(true);
		pack();

	}

	public JPanel initComponents() {
		JPanel gui = new JPanel(new BorderLayout());
		gui.setBorder(new EmptyBorder(2, 3, 2, 3));
		String userDirLocation = System.getProperty("user.dir");
		File userDir = new File(userDirLocation);
		// default to user directory
		fileChooser = new JFileChooser(userDir);

		Action open = new AbstractAction("Open") {

			@Override
			public void actionPerformed(ActionEvent e) {
				int result = fileChooser.showOpenDialog(gui);
				if (result == JFileChooser.APPROVE_OPTION) {
					try {
						file = fileChooser.getSelectedFile();
						FileReader fr = new FileReader(file);
						textarea.read(fr, file);
						fr.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		};

		Action save = new AbstractAction("Save") {

			BufferedWriter writer;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (textarea.getText().equals("")) {
					Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
					setCursor(defaultCursor);

					JOptionPane.showMessageDialog(new JFrame(),
							"There is no script to save", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					saveAs();
				}
			}
		};

		Action runScript = new AbstractAction("Run Script") {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (textarea.getText().equals("")) {
					Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
					setCursor(defaultCursor);

					JOptionPane.showMessageDialog(new JFrame(),
							"There is no script to run", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {

					Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
					try {
						synchronized (waitCursor) {
							setCursor(Cursor
									.getPredefinedCursor(Cursor.WAIT_CURSOR));
							waitCursor.wait(900);
						}

						int totalLines = textarea.getLineCount();
						String text = textarea.getText();
						for (int i = 0; i < totalLines; i++) {
							int start = textarea.getLineStartOffset(i);
							int end = textarea.getLineEndOffset(i);
							String line = text.substring(start, end);
							if (!line.equals("") && line.contains(":")) {
								String[] split = line.split(":");
								String firstSubString = split[0];
								String secondSubString = split[1];

								if (firstSubString.trim().equals("Step0")
										&& secondSubString.trim().equals(
												"StopSAP")) {
									controller.get_outputTestEditor()
											.getStop_SAP_Checkbox()
											.setSelected(true);
								}
								if (firstSubString.trim().equals("Step1")
										&& secondSubString.trim().equals(
												"ApplyKernel")) {
									controller.get_outputTestEditor()
											.getApplyKernelCheckbox()
											.setSelected(true);
								}
								if (firstSubString.trim().equals("Step2")
										&& secondSubString.trim().equals(
												"StartSAP")) {
									controller.get_outputTestEditor()
											.getStartSAPCheckBox()
											.setSelected(true);
								}
								if (firstSubString.trim().equals("SAP_SID")
										&& secondSubString.trim().equals(
												"bigboss")) {
									controller.get_outputTestEditor()
											.getSap_SID_Field().setText("");
									controller.get_outputTestEditor()
											.getSap_SID_Field()
											.setText("bigboss");
								}
								if (firstSubString.trim()
										.equals("SAP_Password")
										&& secondSubString.trim().equals(
												"qsecofer")) {
									controller.get_outputTestEditor()
											.getSap_PASSWORD_Field()
											.setText(" ");
									controller.get_outputTestEditor()
											.getSap_PASSWORD_Field()
											.setText("qsecofer");
								}
								if (firstSubString.trim().equals("SAP_Userd")
										&& secondSubString.trim().equals(
												"dcnadm")) {
									// TODO
								}

							}

						}
						controller
								.getLogging()
								.getLogger()
								.info("Script" + " " + "[ " + file + " ]" + " "
										+ "Successfully loaded....");
					} catch (InterruptedException | BadLocationException e1) {
						e1.printStackTrace();
					}
					setCursor(waitCursor);

					Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
					setCursor(defaultCursor);
					setInVisible();
				}

			}

		};

		JToolBar tb = new JToolBar();
		gui.add(tb, BorderLayout.PAGE_START);
		JButton openButton = tb.add(open);
		openButton.setFont(new Font("Arial", Font.PLAIN, 12));
		JButton saveButton = tb.add(save);
		saveButton.setFont(new Font("Arial", Font.PLAIN, 12));
		JButton runScriptButton = tb.add(runScript);
		runScriptButton.setFont(new Font("Arial", Font.PLAIN, 12));

		textarea.setWrapStyleWord(true);
		textarea.setLineWrap(true);

		gui.add(new JScrollPane(textarea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		return gui;
	}

	public void saveAs() {
		FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(
				"Text File", "txt");
		final JFileChooser saveAsFileChooser = new JFileChooser();
		saveAsFileChooser.setApproveButtonText("Save");
		saveAsFileChooser.setFileFilter(extensionFilter);
		int actionDialog = saveAsFileChooser.showOpenDialog(this);
		if (actionDialog != JFileChooser.APPROVE_OPTION) {
			return;
		}
		// !! File fileName = new File(SaveAs.getSelectedFile() + ".txt");
		File file = saveAsFileChooser.getSelectedFile();
		if (!file.getName().endsWith(".txt")) {
			file = new File(file.getAbsolutePath() + ".txt");
		}

		BufferedWriter outFile = null;
		try {
			outFile = new BufferedWriter(new FileWriter(file));

			textarea.write(outFile);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (outFile != null) {
				try {
					outFile.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public void setVisible() {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				setVisible(true);
				setLocationRelativeTo(null);

			}
		};
		SwingUtilities.invokeLater(r);
	}

	public void setInVisible() {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				setVisible(false);
				setLocationRelativeTo(null);

			}
		};
		SwingUtilities.invokeLater(r);
	}

}