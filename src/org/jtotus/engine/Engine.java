
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
package org.jtotus.engine;

import java.util.ArrayList;
import org.jlucrum.realtime.BrokerWatcher;
import org.jlucrum.realtime.broker.MarketBrokerSimulator;
import org.jlucrum.realtime.generators.AccdistGenerator;
import org.jlucrum.realtime.generators.IndicatorIndexGenerator;
import org.jlucrum.realtime.generators.RsiGenerator;
import org.jlucrum.realtime.generators.TickInterface;
import org.jlucrum.realtime.generators.VPTGenerator;
import org.jlucrum.realtime.generators.VrocGenerator;
import org.jlucrum.realtime.listeners.TicksToFile;
import java.util.HashMap;
import org.jtotus.methods.MethodEntry;
import org.jtotus.methods.DummyMethod;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jtotus.config.ConfPortfolio;
import org.jtotus.gui.JtotusView;
import org.jtotus.database.AutoUpdateStocks;
import org.jtotus.gui.MethodResultsPrinter;
import org.jtotus.methods.PotentialWithIn;
import org.jtotus.methods.SpearmanCorrelation;
import org.jtotus.methods.StatisticsFreqPeriod;
import org.jtotus.methods.TaLibEMA;
import org.jtotus.methods.TaLibMACD;
import org.jtotus.methods.TaLibMOM;
import org.jtotus.methods.TaLibRSI;
import org.jtotus.methods.TaLibSMA;
import org.jtotus.threads.*;

/**
 *
 * @author Evgeni Kappinen
 */
public class Engine {
    private static final Engine singleton = new Engine();
    private PortfolioDecision portfolioDecision = null;
    private JtotusView mainWindow = null;
    private ConfPortfolio portfolioConfig = null;
    //GenearatorName, StatementString, Object
    private HashMap<String, HashMap<String, TickInterface>> listOfGenerators = null;
    private final static Log log = LogFactory.getLog(Engine.class);
    private MethodResultsPrinter resultsPrinter = null;
    private BrokerWatcher watcher = null;

    private Engine() {

        watcher = new BrokerWatcher();
        this.prepareMethodsList();
    }


    public HashMap<String, HashMap<String, TickInterface>> getListOfGenerators() {
        return listOfGenerators;
    }

    private void prepareMethodsList() {
        // Available methods

        listOfGenerators = new HashMap<String, HashMap<String, TickInterface>>();
        LinkedList<MethodEntry> listOfLongTermIndicators = new LinkedList<MethodEntry>();
        listOfLongTermIndicators.add(new DummyMethod());
        listOfLongTermIndicators.add(new PotentialWithIn());
        listOfLongTermIndicators.add(new TaLibRSI());
        listOfLongTermIndicators.add(new TaLibSMA());
        listOfLongTermIndicators.add(new TaLibEMA());
        listOfLongTermIndicators.add(new TaLibMOM());
        listOfLongTermIndicators.add(new TaLibMACD());
        listOfLongTermIndicators.add(new SpearmanCorrelation());
        listOfLongTermIndicators.add(new StatisticsFreqPeriod());

//        FIXME: groovy interface
//        try {
//            Class groovyClass = Class.forName("org.jtotus.methods.DecisionScript");
//            GroovyScipts scripts = (GroovyScipts) groovyClass.newInstance();
//            scripts.loadScripts(listOfLongTermIndicators);
//
//        } catch (InstantiationException ex) {
//            log.info("GroovyScipt is disabled : InstantiationException");
//        } catch (IllegalAccessException ex) {
//            log.info("GroovyScipt is disabled : IllegalAccessException");
//        } catch (ClassNotFoundException ex) {
//            log.info("GroovyScipt is disabled");
//        }

        portfolioDecision = new PortfolioDecision(listOfLongTermIndicators);
    }

    public synchronized static Engine getInstance() {
        return singleton;
    }

    public void setGUI(JtotusView tempView) {
        mainWindow = tempView;