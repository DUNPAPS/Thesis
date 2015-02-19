/**
 * 
 */
package com.sap.on.ibm.i.editor.main;

import javax.swing.SwingUtilities;

import com.sap.on.ibm.i.editor.controller.Controller;

/**
 * @author Duncan
 *
 */
public class Main {
 
	private  static Controller controller;
	public static void main(String[] args) {
		controller = new Controller();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				controller.showMainView();
			}
		});
        

	}

}
