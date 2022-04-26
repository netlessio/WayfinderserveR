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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.jlucrum.realtime.BrokerWatcher;
import com.espertech.esper.client.*;
import org.jtotus.common.Helper;
import org.jtotus.common.MethodResults;
import org.jtotus.config.ConfPortfolio;
import org.jtotus.config.ConfigLoader;
import org.jtotus.config.GUIConfig;
import org.jtotus.config.MainMethodConfig;
import org.jtotus.engine.Engine;
import org.jtotus.gui.mail.JtotusGmailClient;
import org.jtotus.methods.MethodEntry;

/**
 *
 * @author Evgeni Kappinen
 */
public class JTotusMethodView extends JTabbedPane implements MethodResultsPrinter, UpdateListener {

    private JScrollPane jScrollPane1 = null;
    private JDesktopPane drawDesktopPane = null;
    private JTable methodTable = null;
    private Helper help = Helper.getInstance();

    @Override
    public void update(EventBean[] eventBeans, EventBean[] eventBeans1) {

        for (EventBean eventBean : eventBeans) {
            if (eventBean.getUnderlying() instanceof MethodResults) {
                MethodResults results = (MethodResults) eventBean.getUnderlying();
                this.drawResults(results);
            }
        }
    }

    class methodTableListener implements TableModelListener {

        public void tableChanged(TableModelEvent event) {
            DefaultTableModel source = (DefaultTableModel) event.getSource();

            if (event.getType() == TableModelEvent.UPDATE
                    || event.getType() == TableModelEvent.INSERT) {
                String type = (String) source.getValueAt(source.getRowCount() - 1, 0);

                //If sum column does not exists create one
                if (type.compareTo("Sum") != 0 && source.getRowCount() != 0) {
                    String[] data = new String[source.getColumnCount()];
                    data[0] = "Sum";
                    source.addRow(data);
                }

                //TODO: calculate sum, TableModelEvent.ALL_COLUMNS
                //TODO: summ only when Normilizer is used.
                int col = event.getColumn();
                if (col == TableModelEvent.ALL_COLUMNS) {
                    System.err.printf("TODO: all columns\n");
                    return;
                }


                Double sum = new Double(0.0f);
                int count = 0;
                for (int row = source.getRowCount() - 2; row > 0; row--) {
                    String rowValue = (String) source.getValueAt(row, col);
                    if (rowValue != null) {
                        sum += Double.valueOf(rowValue);
                        count++;
                    }
                }

                sum /= Double.valueOf(count);
                String sumValue = sum.toString();
                String value = (String) source.getValueAt(source.getRowCount() - 1, col);
                if (value == null || value.compareTo(sumValue) != 0) {
                    source.setValueAt(sumValue, source.getRowCount() - 1, col);
                }

            }

        }
    }

    private class PopupListener extends MouseAdapter {
        JTable table = null;
        JPopupMenu popup = null;
        JCheckBoxMenuItem item = null;
        JCheckBoxMenuItem auto = null;
        ConfigLoader<MainMethodConfig> configFile = null;
        MainMethodConfig config = null;
        
        public PopupListener(JTable table) {
            this.table =  table;
        }

        public JPopupMenu getPopupMenu() {
            if (popup != null) {
                return popup;
            }

            popup = new JPopupMenu();
            item = new JCheckBoxMenuItem("Draw");
            auto = new JCheckBoxMenuItem("Auto-start");
            popup.add(item);
            popup.add(auto);

            item.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    config.inputPrintResults = !config.inputPrintResults;
                    configFile.storeConfig(config);
                }
            });

            auto.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    int[] selectedRows = table.getSelectedRows();
                    ConfPortfolio portfolioConfig;

                    ConfigLoader<ConfPortfolio> configPortfolio =
                            new ConfigLoader<ConfPortfolio>("OMXHelsinki");

                    portfolioConfig = configPortfolio.getConfig();
                    if (portfolioConfig == null) {
                        //Load default values
                        portfolioConfig = new ConfPortfolio();
                    }

                    for (int selectedRow : selectedRows) {
                        String method = table.getModel().getValueAt(selectedRow, 0).toString();
                        portfolioConfig.setAutoStarted(method);
                    }
                    configPortfolio.storeConfig(portfolioConfig);
                }
            });

            return popup;
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                final JPopupMenu popupMenu = getPopupMenu();

                ConfPortfolio portfolioConfig;

                ConfigLoader<ConfPortfolio> configPortfolio =
                        new ConfigLoader<ConfPortfolio>("OMXHelsinki");

                portfolioConfig = configPortfolio.getConfig();
                if (portfolioConfig == null) {
                    //Load default values
                    portfolioConfig = new ConfPortfolio();
                }

                int[] selectedRows = table.getSelectedRows();

                for (int row = 0; row < selectedRows.length; row++) {
                    String method = table.getModel().getValueAt(selectedRows[row], 0).toString();
                    if (portfolioConfig.isAutoStarted(method)) {
                        auto.setSelected(true);
                    } else {
                        auto.setSelected(false);
                    }
                    
                    int[] selectedColumns = table.getSelectedColumns();
                    for (int selectedColumn : selectedColumns) {
                        if (!table.isCellSelected(selectedRows[row], selectedColumn)
                                || selectedColumn == 0) {
                            continue;
                        }
                        //
                        String name = table.getValueAt(selectedRows[row], 0).toString();

                        configFile = new ConfigLoader<MainMethodConfig>("OMXHelsinki"
                                    + File.separator
                                    + table.getColumnModel().getColumn(selectedColumn).getHeaderValue()
                                    + File.separator
                                    + name);

                        config = configFil