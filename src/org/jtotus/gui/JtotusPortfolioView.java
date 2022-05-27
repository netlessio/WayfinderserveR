
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
 */
package org.jtotus.gui;

import org.jlucrum.realtime.BrokerWatcher;
import org.jlucrum.realtime.eventtypes.IndicatorData;
import org.jlucrum.realtime.eventtypes.StockTick;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.jtotus.config.ConfigLoader;
import org.jtotus.config.GUIConfig;
import org.jtotus.gui.graph.DynamicCharting;
import org.jtotus.gui.graph.GraphPacket;
import org.jtotus.gui.graph.GraphPrinter;

/**
 *
 * @author Evgeni Kappinen
 */
public class JtotusPortfolioView extends JTabbedPane implements UpdateListener {

    private JScrollPane jScrollPane4 = null;
    private JTable portfolioTable = null;
    private JTable standAloneTable = null;
    DefaultTableModel portfolioModel = null;
    private JDesktopPane desktopPane = null;
    private HashMap <String, String>titleMap = null;
    private GUIConfig uiConfig = null;
    private boolean debug = false;
    JInternalFrame standAloneFrame = new JInternalFrame();


    public void loadStoredView() {
        if (uiConfig == null) {
            ConfigLoader<GUIConfig> loader = new ConfigLoader<GUIConfig>("GUIConfig");
            uiConfig = loader.getConfig();
            if (uiConfig == null) {
                uiConfig = new GUIConfig();
            }
        }
    }
