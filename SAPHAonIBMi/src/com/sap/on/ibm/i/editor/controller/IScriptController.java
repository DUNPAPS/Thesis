package com.sap.on.ibm.i.editor.controller;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;


public interface IScriptController  {
 
	public void progress(PropertyChangeEvent e);

	public void sendDoneEvent(ActionEvent e);

}
