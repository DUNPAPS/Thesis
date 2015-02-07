/**
 * 
 */
package com.sap.on.ibm.i.editor.controller;

import com.sap.on.ibm.i.editor.model.User;
import com.sap.on.ibm.i.editor.view.OutputTestEditor;

/**
 * @author Duncan
 *
 */
public class Controller{
	private OutputTestEditor _outputTestEditor;
	private User _user;
    private Listener _listener;
    
    public Controller() {
		this._outputTestEditor = new OutputTestEditor();
		this._user = new User();
		this._listener= new Listener(_outputTestEditor,this._user);
		this.addListener(this._listener);
	}

	public void showView(){
		this._outputTestEditor.setVisible(true);
		 	    } 
		 	 
		
	public void addListener(Listener listener) {
		_outputTestEditor.setclearLogViewButtonActionListener(listener);
		_outputTestEditor.setcopyActionListener(listener);
		_outputTestEditor.setstop_SAP_CheckboxActionListener(listener);
		_outputTestEditor.setplayButtonActionListener(listener);
		_outputTestEditor.setstopButtonActionListener(listener);
		_outputTestEditor.setexitJMenuItemActionListener(listener);
		_outputTestEditor.setinforJMenuItemActionListener(listener);
		
	}
	public void removeListener() {
		//TODO
	}
}