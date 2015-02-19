package com.sap.on.ibm.i.editor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;

import com.sap.on.ibm.i.logger.Logging;

public class Listener implements ActionListener, PropertyChangeListener,
		ItemListener {

	private Logging logging;

	private boolean stopSAPCheckBox;

	private Controller controller;

	private boolean applyKernelCheckBox;

	private boolean allChecked;

	public Listener(Controller controller) {
		this.controller = controller;
		logging = Logging.getInstance();
		logging.setLogger(Listener.class.getName());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.controller.get_outputTestEditor()
				.getPlayButton()) {
			String sid = this.controller.get_outputTestEditor().getSID();
			String password = this.controller.get_outputTestEditor()
					.getPassword();
			stopSAPCheckBox = this.controller.get_outputTestEditor()
					.getStop_SAP_Checkbox().isSelected();
			applyKernelCheckBox = this.controller.get_outputTestEditor()
					.getApplyKernelCheckbox().isSelected();
			if (sid.equals("") || password.equals("") || !sid.equals("bigboss")
					|| !password.equals("qsecofer")) {
				allChecked = true;
				JOptionPane.showMessageDialog(null,
						"Invalid username and password", "Try again",
						JOptionPane.ERROR_MESSAGE);
				this.controller.get_outputTestEditor().getSid_field()
						.setText("");
				this.controller.get_outputTestEditor().getPassword_field()
						.setText("");
			}
			if (stopSAPCheckBox) {
				allChecked = true;
				controller.executeSAPcontrol();
			}
			if (applyKernelCheckBox) {
				allChecked = true;
				controller.executeSSHCommand();
			}
			if (!allChecked) {
				JOptionPane.showMessageDialog(null, " Select a Task to run..",
						" Run Tasks ", JOptionPane.ERROR_MESSAGE);
			}
		}
		if (e.getSource() == this.controller.get_outputTestEditor()
				.getImportScriptJMenuItem()) {
			this.controller.showScriptView();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {

	}

}
