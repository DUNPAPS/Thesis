package com.sap.on.ibm.i.editor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;

import com.sap.on.ibm.i.logger.Levels;
import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.i.tasks.TaskDoneEvent;

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
		try {

			if (e.getSource() == this.controller.get_outputTestEditor()
					.getImportScriptJMenuItem()) {
				this.controller.showScriptView();
			}
			if (e.getSource() == this.controller.get_outputTestEditor()
					.getPlayButton() || e instanceof TaskDoneEvent) {

				String EventName = e.getActionCommand();
				controller.logMessages(Levels.INFO, EventName, null);

				this.controller.get_outputTestEditor().getPlayButton()
						.setEnabled(false);
				String user = this.controller.get_outputTestEditor().getUSER();
				String password = this.controller.get_outputTestEditor()
						.getPassword();
				stopSAPCheckBox = this.controller.get_outputTestEditor()
						.getStop_SAP_Checkbox().isSelected();
				applyKernelCheckBox = this.controller.get_outputTestEditor()
						.getApplyKernelCheckbox().isSelected();

				if (user.equals("") || password.equals("")
						|| !user.equals("bigboss")
						|| !password.equals("qsecofer")) {
					allChecked = true;
					stopSAPCheckBox = false;
					applyKernelCheckBox = false;
					JOptionPane.showMessageDialog(null,
							"Invalid username and password", "Try again",
							JOptionPane.ERROR_MESSAGE);
					controller.get_outputTestEditor().getSap_SID_Field()
							.setText("");
					controller.get_outputTestEditor().getSap_USER_Field()
							.setText("");
					controller.get_outputTestEditor().getSap_USER_Field()
							.setText("");
				}

				if (stopSAPCheckBox) {
					allChecked = true;
					controller.stopSAP();
					controller.get_outputTestEditor().getStop_SAP_Checkbox()
							.setSelected(false);

				} else if (applyKernelCheckBox) {
					allChecked = true;
					controller.applyKernel();
					controller.get_outputTestEditor().getApplyKernelCheckbox()
							.setSelected(false);

				}
				else if (!allChecked) {
					JOptionPane.showMessageDialog(null,
							" Select a Task to run..", " Run Tasks ",
							JOptionPane.ERROR_MESSAGE);
					this.controller.get_outputTestEditor().getPlayButton().setEnabled(true);
				}
				else {
					allChecked = false;
					
				}
				this.controller.get_outputTestEditor().getjProgressBar()
						.setString(" ");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress".equals(evt.getPropertyName())) {
			int progress = (Integer) evt.getNewValue();
			this.controller.get_outputTestEditor().getjProgressBar()
					.setStringPainted(true);
			this.controller.get_outputTestEditor().getjProgressBar()
					.setValue(progress);
			this.controller
					.get_outputTestEditor()
					.getStatusBarJLabel()
					.setText("In " + evt.getPropertyName().toString() + " ....");
			this.controller.get_outputTestEditor().getPlayButton()
			.setEnabled(false);
		} else {
			this.controller.get_outputTestEditor().getStatusBarJLabel()
					.setText("Task(s) " + evt.getNewValue().toString());
			this.controller.get_outputTestEditor().getPlayButton()
			.setEnabled(true);

		}
	}

}
