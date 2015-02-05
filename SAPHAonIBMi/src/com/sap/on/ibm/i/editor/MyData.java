package com.sap.on.ibm.i.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

import javax.swing.Timer;

class MyData {
	public static final int TIMER_DELAY = 300;
	public static final String VALUE = "value";
	protected static final int MAX_DELTA_VALUE = 5;
	private String text;
	private int value = 0;
	private Random random = new Random();
	private PropertyChangeSupport pcSupport = new PropertyChangeSupport(this);

	public MyData(String text) {
		this.text = text;
		new Timer(TIMER_DELAY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int deltaValue = random.nextInt(MAX_DELTA_VALUE);
				int newValue = value + deltaValue;
				if (newValue >= 100) {
					newValue = 100;
					((Timer) e.getSource()).stop();
				}
				setValue(newValue);
			}
		}).start();
	}

	public String getText() {
		return text;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		int oldValue = this.value;
		this.value = value;

		PropertyChangeEvent pcEvent = new PropertyChangeEvent(this, VALUE,
				oldValue, value);
		pcSupport.firePropertyChange(pcEvent);
	}

	public void addPropertyChangeListener(PropertyChangeListener pcListener) {
		pcSupport.addPropertyChangeListener(pcListener);
	}
}
