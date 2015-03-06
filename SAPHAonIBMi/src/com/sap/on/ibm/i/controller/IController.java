package com.sap.on.ibm.i.controller;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;


public interface IController  {
 
	public void progress(PropertyChangeEvent e,String Taskname);

	public void setProgressbarMax(int nSteps);
	
	public void updateProgressbar();
	
	public void sendDoneEvent(ActionEvent e);

}
