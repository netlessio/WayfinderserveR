
/*
    This file is part of jTotus.

    jTotus is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jTotus is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jTotus.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * http://tutorials.jenkov.com/java-collections/navigableset.html
 */

package org.jtotus.gui;


import org.jlucrum.realtime.generators.TickInterface;
import org.jlucrum.realtime.training.TrainManager;
import org.jdesktop.application.*;
import org.jtotus.engine.Engine;
import org.jtotus.gui.passwords.JtotusSetPasswordsGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The application's main frame.
 */
public class JtotusView extends FrameView {

    private JFrame mainFrame = null;


    public void initialize() {
        ((JTotusMethodView) methodTabbedPane).initialize();
        JtotusPortfolioView portTabTable = new JtotusPortfolioView();
        portTabTable.initialize();
        portTabTable.setMainPane(((JTotusMethodView) methodTabbedPane).getMainPane());
        ((JTotusMethodView) methodTabbedPane).addComponentToInternalWindow(portTabTable, "PortfolioView");
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JtotusView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(JtotusView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(JtotusView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(JtotusView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LinkedList<String> getMethodList() {

        //Returns selected methods from methoTabPane
        return ((JTotusMethodView) methodTabbedPane).getSelectedMethods();
    }


    public JtotusView(SingleFrameApplication app) {
        super(app);
        mainFrame = app.getMainFrame();

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
