
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JtotusSetPasswordsGUI.java
 *
 * Created on Jan 1, 2011, 1:34:06 PM
 */

package org.jtotus.gui.passwords;

import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import org.jtotus.config.ConfigLoader;
import org.jtotus.config.GUIConfig;

/**
 *
 * @author house
 */
public class JtotusSetPasswordsGUI extends javax.swing.JDialog {
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    

    /** Creates new form JtotusSetPasswordsGUI */
    public JtotusSetPasswordsGUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
       
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */