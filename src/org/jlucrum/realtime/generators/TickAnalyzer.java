
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
package org.jlucrum.realtime.generators;

import org.jlucrum.realtime.BrokerWatcher;
import org.jlucrum.realtime.eventtypes.IndicatorData;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import org.jtotus.config.ConfSimpleTechnicalIndicators;
import org.jtotus.config.ConfigLoader;


/**
 *
 * @author Evgeni Kappinen
 */
public abstract class TickAnalyzer implements TickInterface{
    protected EPRuntime esperRuntime = null;
    protected EPServiceProvider provider = null;
    protected EPStatement eps = null;
    protected ConfSimpleTechnicalIndicators config = null;
    
    public TickAnalyzer() {
        //subscribeForTicks();
        ConfigLoader<ConfSimpleTechnicalIndicators> loader =
                new ConfigLoader<ConfSimpleTechnicalIndicators>("ConfSimpleTechnicalIndicators");
        config = loader.getConfig();
        if (config == null) {
            config = new ConfSimpleTechnicalIndicators();
            loader.storeConfig(config);
        }
    }

    private EPServiceProvider getProvider() {
        if (provider == null) {
            provider = BrokerWatcher.getMainEngine();
        }
        return provider;
    }

    protected EPStatement patternForEvents(String pattern) {
        eps = getProvider().getEPAdministrator().createPattern(pattern);
        return eps;
    }

    public void statementForEvents(String stmt) {
        eps = getProvider().getEPAdministrator().createEPL(stmt);
        eps.addListener(this);
    }
    
    public boolean subscribeForTicks() {
        eps = getProvider().getEPAdministrator().createEPL("select * from StockTick");
        eps.addListener(this);
        
        return eps.isStarted();
    }

    public boolean unsubscribeForTicks() {
        eps.removeAllListeners();
        eps.destroy();

        return eps.isDestroyed();
    }

    public EPRuntime getEngine() {

        if (esperRuntime == null) {
            esperRuntime = BrokerWatcher.getMainEngine().getEPRuntime();
        }

        return esperRuntime;
    }
