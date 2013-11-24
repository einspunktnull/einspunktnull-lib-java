/*
 * $Id: WindowCloser.java,v 1.1.1.1 2001/02/07 15:24:18 rtfm Exp $
 */

/**
 * Utility class to catch window close events on a target
 * window and actually dispose the window.
 */
package net.einspunktnull.gui;
import java.awt.*;
import java.awt.event.*;

public class WindowCloser implements WindowListener {

   /**
    * Create an adaptor to listen for window closing events
    * on the given window and actually perform the close.
    * @param w
    */

    public WindowCloser(Window w) {
	this(w, false);
    }

   /**
    * Create an adaptor to listen for window closing events
    * on the given window and actually perform the close.
    * If "exitOnClose" is true we do a System.exit on close.
    */

    public WindowCloser(Window w, boolean exitOnClose) {
	this.exitOnClose = exitOnClose;
	w.addWindowListener(this);
    }


    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
	if (exitOnClose) {
            System.exit(0);
	}
	e.getWindow().dispose();
    }

    public void windowClosed(WindowEvent e) {
	if (exitOnClose) {
            System.exit(0);
	}
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    private boolean exitOnClose;
}

/*
 * $Log: WindowCloser.java,v $
 * Revision 1.1.1.1  2001/02/07 15:24:18  rtfm
 * initial
 *
 * Revision 1.1  1998/10/06 08:06:17  mm
 * builder-package added.
 *
 */
