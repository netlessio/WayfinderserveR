
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
 *
https://www.nordnet.fi/mux/page/hjalp/ordHjalp.html?ord=diagram%20rsi
 *
MACD




 */
package org.jtotus.methods;

import org.jlucrum.realtime.eventtypes.MarketData;
import org.jtotus.common.MethodResults;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import java.io.File;
import java.util.Date;
import org.jtotus.common.DateIterator;
import org.jtotus.gui.graph.GraphSender;
import org.jtotus.config.ConfTaLibMACD;
import org.jtotus.common.StateIterator;
import org.jtotus.config.ConfigLoader;
import org.jtotus.methods.evaluators.EvaluateMethodSignals;

/**
 *
 * @author Evgeni Kappinen
 */
public class TaLibMACD extends TaLibAbstract implements MethodEntry {
    /*Stock list */

    protected ConfTaLibMACD config = null;
    public ConfigLoader<ConfTaLibMACD> configFile = null;

    public void loadInputs(String configStock) {

        configFile = new ConfigLoader<ConfTaLibMACD>(super.portfolioConfig.portfolioName
                + File.separator
                + configStock
                + File.separator
                + this.getMethName());

        if (configFile.getConfig() == null) {
            //Load default values
            config = new ConfTaLibMACD();
            configFile.storeConfig(config);
        } else {
            config = (ConfTaLibMACD) configFile.getConfig();
        }
        super.child_config = config;
        configFile.applyInputsToObject(this);
    }



    /************* DECISION TEST *************
     * @param evaluator Evaluation object
     * @param stockName ReviewTarget of the method
     * @param input closing price for period
     *
     * @return  void
     */
    public void performDecisionTest(EvaluateMethodSignals evaluator,
                                    String stockName,
                                    double[] input,
                                    int fastPeriod,
                                    int slowPeriod,
                                    int signalPeriod) {

        boolean change=false;
        MInteger outBegIdx = new MInteger();
        MInteger outNbElement = new MInteger();

        double []macd = new double [input.length - 1];
        double []macdSignal = new double [input.length - 1];
        double []macdHis = new double [input.length - 1];

        double[] output = this.actionMACD(input,
                                        outBegIdx, outNbElement,
                                        macd, macdSignal,macdHis,
                                        fastPeriod, slowPeriod, signalPeriod);

    }


    public double[] actionMACD(double[] input,
                                MInteger outBegIdxDec,
                                MInteger outNbElementDec,
                                double []macd,
                                double []macdSignal,