package com.sap.on.ibm.i.controller;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;


public interface IController  {
 
	public void progress(PropertyChangeEvent e,String Taskname);

	public void sendDoneEvent(ActionEvent e);

}
