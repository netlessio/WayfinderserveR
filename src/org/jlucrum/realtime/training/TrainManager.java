package org.jlucrum.realtime.training;

import org.jlucrum.realtime.BrokerWatcher;
import org.jlucrum.realtime.eventtypes.MarketData;
import org.jlucrum.realtime.eventtypes.MarketSignal;
import org.jlucrum.realtime.strategy.DecisionStrategy;
import org.jlucrum.realtime.strategy.SimpleIndicatorsOnlyStrategy;
import com.espertech.esper.client.EPRuntime;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jtotus.common.MethodResults;
import org.jtotus.config.ConfPortfolio;
import org.jtotus.config.ConfTrainWithLongTermIndicators;
import org.jtotus.config.ConfigLoader;
import org.jtotus.database.DataFetcher;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jtotus.methods.MethodEntry;
import org.jtotus.methods.StatisticsFreqPeriod;
import org.jtotus.methods.TaLibRSI;
import org.jtotus.threads.MethodFuture;

/**
 * This file is part of JTotus.
 * <p/>
 * jTotus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * jTotus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with jTotus.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
* Created by IntelliJ IDEA.
* Author: Evgeni Kappinen
* Date: 5/2/11
* Time: 6:38 PM
*/
public class TrainManager {
    private final ConfPortfolio portfolio = ConfPortfolio.getPortfolioConfig();
    private final DecisionStrategy strategy = new SimpleIndicatorsOnlyStrategy();
    private final EPRuntime epRuntime = BrokerWatcher.getMainEngine().getEPRuntime();
    private final DataFetcher fetcher = new DataFetcher();
    private LinkedList<MethodEntry> methods = new LinkedList<MethodEntry>();
    private LinkedList<MethodFuture> indResults = new LinkedList<MethodFuture>();
    private ConfTrainWithLongTermIndicators config = null;
    private ExecutorService threadExecutor = Executors.newCachedThreadPool();
    

    public TrainManager() {
        ConfigLoader<ConfTrainWithLongTermIndicators> loader
                = new ConfigLoader<ConfTrainWithLongTermIndicators>("ConfTrainWithLongTermIndicators");
        config = loader.getConfig();
        if (config == null) {
            config = new ConfTrainWithLongTermIndicators();
            loader.storeConfig(config);
        }
        
        methods.add(new TaLibRSI());
        methods.add(new StatisticsFreqPeriod());
    }

    public void train() {
        HashMap<String, MethodResults> inputs = new HashMap<String, MethodResults>();
        DateTime currentDate = portfolio.inputStartingDate;
        
        while (currentDate.isBefore(portfolio.inputEndingDate.minusDays(1))) {
            final MarketData data = fetcher.prepareMarketData(portfolio.inputListOfStocks,
                                                        portfolio.inputStartIndicatorDate,
                                                        currentDate);

            MethodFuture<MethodResults> futureTask = null;
            for (MethodEntry task : methods) {
                if (task.isCallable()) {
                    task.setMarketData(data);
                    futureTask = new MethodFuture<MethodResults>(task);
                    threadExecutor.execute(futureTask);
                    indResults.push(futureTask);
                } else {
                    //Lets support Runnable for now.
                    System.err.printf("TrainManager support only callable indicators ! : %s\n", task.getMethName());
      