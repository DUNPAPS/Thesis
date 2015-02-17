package com.sap.on.ibm.i.editor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;

import com.sap.on.ibm.i.editor.model.User;
import com.sap.on.ibm.i.editor.view.OutputTestEditor;
import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.i.tasks.ExecuteSAPControl;
import com.sap.on.ibm.i.tasks.main.RunTasks;

public class Listener implements ActionListener, PropertyChangeListener,
		ItemListener {
	private OutputTestEditor _outputTestEditor;
	private Logging logging;
	private boolean stopSAPCheckBox;
	private Controller controller;
	private boolean applyKernelCheckBox;

	public Listener(Controller controller) {
		this.controller = controller;
		this._outputTestEditor = controller.get_outputTestEditor();
		logging = Logging.getInstance();
		logging.setLogger(Listener.class.getName());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this._outputTestEditor.getPlayButton()) {
			String sid = this._outputTestEditor.getSID();
			String password = this._outputTestEditor.getPassword();
			stopSAPCheckBox = this._outputTestEditor.getStop_SAP_Checkbox()
					.isSelected();
			applyKernelCheckBox = this._outputTestEditor
					.getApplyKernelCheckbox().isSelected();
			if (sid.equals("") || password.equals("") || !sid.equals("bigboss")
					|| !password.equals("qsecofer")) {

				JOptionPane.showMessageDialog(null,
						"Invalid username and password", "Try again",
						JOptionPane.ERROR_MESSAGE);
				this._outputTestEditor.getSid_field().setText("");
				this._outputTestEditor.getPassword_field().setText("");
			}
			 
			if (stopSAPCheckBox) {
				controller.executeSAPcontrol();
			}
			if (applyKernelCheckBox) {
				controller.executeSSHCommand();
			}
			else {
				JOptionPane.showMessageDialog(null, " Select a Task to run..",
						" Run Tasks ", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub

	}

}
