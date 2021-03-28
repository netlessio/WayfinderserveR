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
 * MERCHANTABILITY 