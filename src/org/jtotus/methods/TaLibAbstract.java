
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
package org.jtotus.methods;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlucrum.realtime.BrokerWatcher;
import org.jlucrum.realtime.eventtypes.MarketData;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import org.jtotus.common.MethodResults;
import org.joda.time.DateTime;
import org.jtotus.network.StockType;
import org.jtotus.config.ConfigLoader;
import org.jtotus.gui.graph.GraphSender;
import org.jtotus.config.ConfPortfolio;
import org.jtotus.config.MainMethodConfig;
import org.jtotus.methods.utils.Normalizer;

/**
 * @author Evgeni Kappinen
 */
public abstract class TaLibAbstract implements UpdateListener {

    /*Stock list */
    private int totalStocksAnalyzed = 0;
    private EPRuntime runtime = null;
    GraphSender sender = null;
    //INPUTS TO METHOD: