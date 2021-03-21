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
import org.jtotus.config.ConfTrainWi