
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
package org.jlucrum.realtime;

import org.jlucrum.realtime.eventtypes.*;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.UpdateListener;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jlucrum.realtime.generators.EsperEventGenerator;
import org.jlucrum.realtime.generators.HistoryTicksFromFile;
import org.jlucrum.realtime.generators.TickGenerator;
import org.jtotus.common.MethodResults;
import org.jtotus.gui.graph.GraphPacket;
import org.jtotus.threads.MethodFuture;

/**
 *
 * @author Evgeni Kappinen
 */
public class BrokerWatcher implements Callable, Runnable{

    private ExecutorService threadExecutor = null;
    private MethodFuture<String> futureTask = null;
    private HashMap<String, EPStatement> statements = null;
    private EPServiceProvider cep = null;
    private static final String mainEngineName = "BrokerWatcher";

    public BrokerWatcher() {
        statements = new HashMap<String, EPStatement>();
        threadExecutor = Executors.newCachedThreadPool();

        cep = initializeEngine(mainEngineName);
    }

    public static EPServiceProvider initializeEngine(String mainEngine) {
        Configuration cepConfig = new Configuration();

        cepConfig.addEventType("StockTick", StockTick.class.getName());
        cepConfig.addEventType("IndicatorData", IndicatorData.class.getName());
        cepConfig.addEventType("EsperEventRsi", EsperEventRsi.class.getName());
        cepConfig.addEventType("GraphPacket", GraphPacket.class.getName());
        cepConfig.addEventType("MarketData", MarketData.class.getName());
        cepConfig.addEventType("MethodResults", MethodResults.class.getName());
        cepConfig.addEventType("MarketSignal", MarketSignal.class.getName());

        EPServiceProvider provider = EPServiceProviderManager.getProvider(mainEngine, cepConfig);

        return provider;
    }

    public void addStatement(String statement, UpdateListener listener) {

        EPStatement eps = statements.get(statement);
        if (eps == null) {
            EPAdministrator cepAdm = cep.getEPAdministrator();
            eps = cepAdm.createEPL(statement);
        }

        eps.addListener(listener);
    }

    public void addPattern(String statement, UpdateListener listener) {

        EPStatement eps = statements.get(statement);
        if (eps == null) {
            EPAdministrator cepAdm = cep.getEPAdministrator();
            eps = cepAdm.createPattern(statement);
        }

        eps.addListener(listener);
    }


    public void startHistoryGenerator() {
        startService(new HistoryTicksFromFile(cep.getEPRuntime()));
    }

    public void startTicker() {
       startService(new TickGenerator(cep.getEPRuntime()));
    }

    private void startService(EsperEventGenerator tickGenerator) {

        if (futureTask != null) {
            if (futureTask.cancel(true)) {
             futureTask =  null;
            }
        }

        if (futureTask == null) {
            futureTask = new MethodFuture<String>(tickGenerator);
            threadExecutor.submit(futureTask);
        }
        
    }
    

    public Object call() {
        startTicker();
        return null;
    }

    public static synchronized EPServiceProvider getMainEngine() {
        return initializeEngine(mainEngineName);
    }

    public static void addPattern(EPServiceProvider provider, String statement, UpdateListener listener) {
        EPAdministrator cepAdm = provider.getEPAdministrator();
        EPStatement stmt = cepAdm.createPattern(statement);
        stmt.addListener(listener);
    }

    public static void addStatement(EPServiceProvider provider, String statement, UpdateListener listener) {
        EPAdministrator cepAdm = provider.getEPAdministrator();
        EPStatement stmt = cepAdm.createEPL(statement);
        stmt.addListener(listener);
    }

    public void run() {
        this.call();
    }
}