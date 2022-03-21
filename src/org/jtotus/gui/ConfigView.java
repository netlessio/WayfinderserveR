
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

/*
 * ConfigView.java
 *
 * Created on Oct 9, 2010, 5:33:45 PM
 */

package org.jtotus.gui;

import net.sf.nachocalendar.table.JTableCustomizer;
import org.dom4j.Document;
import org.jtotus.config.ConfigLoader;

import javax.swing.*;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Evgeni Kappinen
 */
public class ConfigView extends javax.swing.JDialog {
    private Document currentDocument = null;
    private String currentDocumentName = null;

//http://www.javaworld.com/javaworld/javatips/jw-javatip102.html
//http://forums.devshed.com/java-help-9/datepicker-inside-a-jtable-536152.html
 class DateCellEditor extends AbstractCellEditor implements TableCellEditor {
    private Date currentDate;
    private JSpinner spinner;

    protected static final String EDIT = "edit";

    
    public DateCellEditor() {

        Calendar calendar = Calendar.getInstance();
        Date initDate = calendar.getTime();
        calendar.add(Calendar.YEAR, -100);
        Date earliestDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 200);
        Date latestDate = calendar.getTime();
        SpinnerModel dateModel = new SpinnerDateModel(initDate,
                                                    earliestDate,
                                                    latestDate,
                                                    Calendar.YEAR);//ignored for user input
        spinner = new JSpinner(dateModel);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd.MM.yyyy"));
    }


    // Implement the one CellEditor method that AbstractCellEditor doesn't.
    public Object getCellEditorValue() {
        currentDate = ((SpinnerDateModel)spinner.getModel()).getDate();
        return ((SpinnerDateModel)spinner.getModel()).getDate();

    }

     // Implement the one method defined by TableCellEditor.
     public Component getTableCellEditorComponent(javax.swing.JTable table,
                                                  Object value,
                                                  boolean isSelected,
                                                  int row, int column) {
//         DateFieldTableEditor editor = new DateFieldTableEditor();
//         if (value.getClass() == Date.class);

         currentDate = (Date) value;
         spinner.setValue(value);
         return spinner;
     }

}


   // http://docstore.mik.ua/orelly/java-ent/jfc/ch03_19.htm
    class FileTreeModel implements TreeModel {
      // We specify the root directory when we create the model.
      protected File root;

      public FileTreeModel(File root) { this.root = root; }

      // The model knows how to return the root object of the tree
      public Object getRoot() { return root; }

      // Tell JTree whether an object in the tree is a leaf
      public boolean isLeaf(Object node) {  return ((File)node).isFile(); }

      // Tell JTree how many children a node has
      public int getChildCount(Object parent) {
        String[] children = ((File)parent).list();
        if (children == null) return 0;
        return children.length;
      }

      // Fetch any numbered child of a node for the JTree.
      // Our model returns File objects for all nodes in the tree.  The
      // JTree displays these by calling the File.toString() method.
      public Object getChild(Object parent, int index) {
        String[] children = ((File)parent).list();
        if ((children == null) || (index >= children.length)) return null;
        return new File((File) parent, children[index]);
      }

      // Figure out a child's position in its parent node.
      public int getIndexOfChild(Object parent, Object child) {
        String[] children = ((File)parent).list();
        if (children == null) return -1;
        String childname = ((File)child).getName();
        for(int i = 0; i < children.length; i++) {
          if (childname.equals(children[i])) return i;
        }
        return -1;
     }
      // This method is invoked by the JTree only for editable trees.
      // This TreeModel does not allow editing, so we do not implement
      // this method.  The JTree editable property is false by default.
      public void valueForPathChanged(TreePath path, Object newvalue) {}

      // Since this is not an editable tree model, we never fire any events,
      // so we don't actually have to keep track of interested listeners
      public void addTreeModelListener(TreeModelListener l) {}