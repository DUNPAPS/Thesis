package com.sap.on.ibm.i.editor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.sap.on.ibm.i.editor.model.User;
import com.sap.on.ibm.i.editor.view.OutputTestEditor;
import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.i.tasks.ExecuteSAPControl;
import com.sap.on.ibm.i.tasks.main.RunTasks;

public class Listener implements ActionListener, PropertyChangeListener,
		ItemListener {
	private OutputTestEditor _outputTestEditor;
	private User _user;
	private Logging logging;

	public Listener(OutputTestEditor outputTestEditor, User user) {
		this._outputTestEditor = outputTestEditor;
		this._user = user;
		logging = Logging.getInstance();
		logging.setLogger(Listener.class.getName());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this._outputTestEditor.getPlayButton()) {
			String sid = this._outputTestEditor.getSID();
			String password = this._outputTestEditor.getPassword();
			boolean b = this._outputTestEditor.getStop_SAP_Checkbox()
					.isSelected();
			if (sid.equals("") || password.equals("")) {
				logging.getLogger().warn("Please Enter User and Password");
				return;
			}
			if (!b) {
				logging.getLogger().info("Stop SAP First");
				return;
			}
			else {
				
			logging.getLogger()
					.info("---------------------------------------------------------------------------");
			logging.getLogger()
					.info("                          ExecuteSAPControl                                             ");
			logging.getLogger()
					.info("---------------------------------------------------------------------------");

			ExecuteSAPControl sapControl = new ExecuteSAPControl();

			sapControl.setFunction("GetProcessList");
			sapControl.setInstance("00");
			sapControl.setHost("as0013");
			sapControl.execute();
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
