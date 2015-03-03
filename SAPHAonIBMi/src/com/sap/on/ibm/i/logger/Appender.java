package com.sap.on.ibm.i.logger;

import java.awt.Color;
import java.util.Hashtable;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import com.sap.on.ibm.i.editor.controller.GUIScriptController;

public class Appender extends AppenderSkeleton {

	private JTextPane textArea;
	private String cache;
	private static StyledDocument styledDocument = null;
	private static Hashtable<Level, SimpleAttributeSet> attributes = null;
	protected Layout layout;

	public Appender(GUIScriptController controller) {
		this.textArea = controller.getOutputTestEditor().getJtextArea();
		getTextPaneInstance();
	}

	public synchronized void getTextPaneInstance() {
		textArea.setEditable(false);
		textArea.setDragEnabled(true);
		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		styledDocument = textArea.getStyledDocument();
		createTextAttributes();
	}

	private static void createTextAttributes() {
		attributes = new Hashtable<Level, SimpleAttributeSet>();
		attributes.put(Level.ERROR, new SimpleAttributeSet());
		attributes.put(Level.FATAL, new SimpleAttributeSet());
		attributes.put(Level.WARN, new SimpleAttributeSet());
		StyleConstants.setForeground(
				(MutableAttributeSet) attributes.get(Level.ERROR), Color.red);
		StyleConstants.setForeground(
				(MutableAttributeSet) attributes.get(Level.FATAL), Color.red);
		StyleConstants.setForeground(
				(MutableAttributeSet) attributes.get(Level.WARN), Color.blue);
	}

	protected void append(LoggingEvent e) {
		if (this.layout != null) {
			String message = layout.format(e);
			final String msg = e.getRenderedMessage();
			if (cache != null && cache.equals(msg)) {
				// Nothing
			} else {
				SwingUtilities.invokeLater(new Thread(
						"Agent Log4JJTextAreaAppender Thread") {
					public void run() {
						try {
							styledDocument.insertString(
									styledDocument.getLength(),
									message
											+ System.getProperty("line.separator"),
									(MutableAttributeSet) attributes.get(e
											.getLevel()));
						} catch (BadLocationException ex) {
							textArea.setText(textArea.getText()
									+ layout.format(e));
						}
						textArea.setCaretPosition(textArea.getDocument()
								.getLength());
					}
				});
				cache = msg;
			}
		}

	}

	public void close() {
		textArea = null;
	}

	public boolean requiresLayout() {
		return true;
	}

	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

}
