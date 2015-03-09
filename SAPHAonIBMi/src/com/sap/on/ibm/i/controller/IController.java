package com.sap.on.ibm.i.controller;

import java.awt.event.ActionEvent;


public interface IController  {
 
	public void setProgressbarMax(int nSteps);
	
	public void updateProgressbar();
	
	public void sendDoneEvent(ActionEvent e);

	public void setThreadName(String name);

	public void doneProgressBar();

}
