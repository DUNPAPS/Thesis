package com.sap.on.ibm.i.view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class HATestScriptEditor extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextArea textarea = new JTextArea(10, 40);
	private JToolBar toolbar;
	private JButton openButton;
	private JButton saveButton;
	private JButton runScriptButton;
	private JPanel gui;

	public HATestScriptEditor() {
		this.setTitle("Run Script");
		gui = initComponents();
		getContentPane().add(gui);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationByPlatform(true);
		pack();

	}

	public JPanel initComponents() {
		JPanel gui = new JPanel(new BorderLayout());
		gui.setBorder(new EmptyBorder(2, 3, 2, 3));

		toolbar = new JToolBar();
		gui.add(toolbar, BorderLayout.PAGE_START);
		openButton = new JButton("Open");
		openButton.setFont(new Font("Arial", Font.PLAIN, 12));
		saveButton = new JButton("Save");
		saveButton.setFont(new Font("Arial", Font.PLAIN, 12));
		runScriptButton = new JButton("Run Script");
		toolbar.add(openButton);
		toolbar.add(saveButton);
		toolbar.add(runScriptButton);

		runScriptButton.setFont(new Font("Arial", Font.PLAIN, 12));

		textarea.setWrapStyleWord(true);
		textarea.setLineWrap(true);

		gui.add(new JScrollPane(textarea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		return gui;
	}

	public JToolBar getToolbar() {
		return toolbar;
	}

	public JButton getOpenButton() {
		return openButton;
	}

	public JButton getSaveButton() {
		return saveButton;
	}

	public JButton getRunScriptButton() {
		return runScriptButton;
	}

	public JPanel getGui() {
		return gui;
	}

	public JTextArea getTextarea() {
		return textarea;
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