package net.einspunktnull.io.gui;

import java.io.PrintStream;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public final class OutputScrollPane extends JScrollPane
{
	private TextAreaOutputStream textAreaOutputStream;
	private PrintStream systemOutPrintStream;
	private PrintStream printStream;
	private JTextArea jTextArea;

	public OutputScrollPane(int columns, int rows)
	{
		systemOutPrintStream = System.out;
		jTextArea = new JTextArea();
		jTextArea.setEditable(false);
		jTextArea.setColumns(columns);
		jTextArea.setRows(rows);
		textAreaOutputStream = new TextAreaOutputStream(jTextArea);
		printStream = new PrintStream(textAreaOutputStream);
		this.setViewportView(jTextArea);
		this.setAutoscrolls(true);
	}

	public void setOutput(boolean terminal)
	{
		if (terminal) System.setOut(printStream);
		else System.setOut(systemOutPrintStream);
	}
	
}
