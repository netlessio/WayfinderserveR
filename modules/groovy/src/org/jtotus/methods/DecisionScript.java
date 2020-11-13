
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
 */
package org.jtotus.methods;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.codehaus.groovy.control.CompilationFailedException;
import org.jtotus.common.MethodResults;
import org.jtotus.config.ConfPortfolio;
import org.jtotus.threads.PortfolioDecision;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Evgeni Kappinen
 */
public class DecisionScript extends TaLibAbstract implements MethodEntry, GroovyScipts {

    private String path_to_script = null;

    public DecisionScript() {
    }

    protected DecisionScript(String tmp) {
        super();
        path_to_script = tmp;
    }

    public MethodResults runGroovyScripts(File path_to_script) {
        ClassLoader parent = getClass().getClassLoader();
        GroovyClassLoader loader = new GroovyClassLoader(parent);
        GroovyObject groovyObject = null;
        Class groovyClass = null;
        MethodResults results = null;

        try {
            System.out.printf("PATHTOSCIPTs:%s\n", path_to_script);
            groovyClass = loader.parseClass(path_to_script);
            // let's call some method on an instance
            groovyObject = (GroovyObject) groovyClass.newInstance();


        } catch (InstantiationException ex) {
            Logger.getLogger(DecisionScript.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DecisionScript.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CompilationFailedException ex) {
            Logger.getLogger(DecisionScript.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DecisionScript.class.getName()).log(Level.SEVERE, null, ex);
        }


        Object[] args = {};
        results = (MethodResults) groovyObject.invokeMethod("run", args);

        if (results == null) {
            System.out.printf("Returned NULL:\n");
            results = new MethodResults(this.getMethName());
        }

        System.out.printf("Hope\n");
        results.printToConsole();
        return results;
    }

    @Override
    public String getMethName() {
        // return this.getClass().getName();
        int dot = path_to_script.lastIndexOf(".");
        int sep = path_to_script.lastIndexOf(File.separator);
        return path_to_script.substring(sep + 1, dot);
    }

    public MethodResults performMethod(String stockName, double[] input) {
        File file = new File(path_to_script);

        if (!file.isFile() || !file.canRead()) {
            return new MethodResults(this.getMethName());
        }

        return this.runGroovyScripts(file);
    }

    private static FileFilter fileIsGroovyScript() {
        FileFilter fileFilter = new FileFilter() {

            public boolean accept(File file) {
                if (!file.isFile() || !file.canRead()) {
                    return false;
                }

                String name = file.getName();
                if (!name.endsWith(".groovy")) {
                    return false;
                }
                return true;
            }
        };
        return fileFilter;
    }

    public void loadScripts(LinkedList<MethodEntry> list) {

        File scriptDir = new File(ConfPortfolio.getPathToGroovyScripts());
        if (!scriptDir.isDirectory()) {
            return;
        }

        FileFilter filter = fileIsGroovyScript();
        File[] listOfFiles = scriptDir.listFiles(filter);

        for (File tmp : listOfFiles) {
            try {
                list.add(new DecisionScript(tmp.getCanonicalPath()));
            } catch (IOException ex) {
                Logger.getLogger(DecisionScript.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void loadScripts(PortfolioDecision portfolio) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}