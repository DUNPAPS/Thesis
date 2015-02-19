package com.sap.on.ibm.i.editor.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sap.on.ibm.i.editor.controller.Controller;

import java.io.*;

public class ScriptEditor extends JFrame {

	private JFileChooser fileChooser;
	private JTextArea output = new JTextArea(10, 40);
	private final JButton btnRunScript = new JButton("Run Script");
	private Controller controller;
	private File file;

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
						output.read(fr, file);
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
				saveAs();
//				JOptionPane.showMessageDialog(null,
//						"The Message was Saved Successfully! ");
			}
		};

		JToolBar tb = new JToolBar();
		gui.add(tb, BorderLayout.PAGE_START);
		JButton button_1 = tb.add(open);
		button_1.setFont(new Font("Arial", Font.PLAIN, 12));
		JButton button = tb.add(save);
		button.setFont(new Font("Arial", Font.PLAIN, 12));
		btnRunScript.setFont(new Font("Arial", Font.PLAIN, 12));

		tb.add(btnRunScript);

		output.setWrapStyleWord(true);
		output.setLineWrap(true);

		gui.add(new JScrollPane(output, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		return gui;
	}

	public void saveAs() {
	      FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Text File", "txt");
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

	         output.write(outFile);

	      } catch (IOException ex) {
	         ex.printStackTrace();
	      } finally {
	         if (outFile != null) {
	            try {
	               outFile.close();
	            } catch (IOException e) {}
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

}