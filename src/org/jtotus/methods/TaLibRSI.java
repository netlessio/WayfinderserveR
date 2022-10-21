
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
RSI

RSI on hintaa seuraava oskilaattori, joka saavuttaa 0-100 välisiä arvoja.
Se vertaa viimeisten ylöspäin tapahtuneiden hintamuutosten voimakkuutta
alaspäin suuntautuneisiin hintamuutoksiin. Suosituimmat tarkasteluvälit
ovat 9, 14 ja 25 päivän RSI.

Tulkinta:
- RSI huipussa: korkea arvo (yli 70/noususuhdanteessa yleensä 80) indikoi yliostotilannetta
- RSI pohjassa: matala arvo (alle 30/laskusuhdanteessa yleenäs 20) indikoi aliostotilannetta

Signaalit:
- Osta, kun RSI:n arvo leikkaa aliostorajan alapuolelta
- Myy, kun RSI:n arvo leikkaa yliostorajan yläpuolelta

Vaihtoehtoisesti:
- Osta, kun RSI leikkaa keskilinjan (50) alapuolelta
- Myy, kun RSI leikkaa keskilinjan (50) yläpuolelta



 */
package org.jtotus.methods;

import org.jtotus.common.MethodResults;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import java.io.File;
import java.util.Date;
import org.jtotus.common.DateIterator;
import org.jtotus.gui.graph.GraphSender;
import org.jtotus.config.ConfTaLibRSI;
import org.jtotus.common.StateIterator;
import org.jtotus.config.ConfigLoader;
import org.jtotus.methods.evaluators.EvaluateMethodSignals;

/**
 *
 * @author Evgeni Kappinen
 */
public class TaLibRSI extends TaLibAbstract implements MethodEntry {
    /*Stock list */
    private boolean debug = false;
    protected ConfTaLibRSI config = null;
    public ConfigLoader<ConfTaLibRSI> configFile = null;

    public void loadInputs(String configStock) {

        configFile = new ConfigLoader<ConfTaLibRSI>(super.portfolioConfig.portfolioName
                + File.separator
                + configStock
                + File.separator
                + this.getMethName());

        if (configFile.getConfig() == null) {
            //Load default values
            config = new ConfTaLibRSI();
            configFile.storeConfig(config);
        } else {
            config = configFile.getConfig();
        }
        super.child_config = config;
        configFile.applyInputsToObject(this);
    }

    

    /************* DECISION TEST *************
     * @param evaluator Evaluation object
     * @param stockName ReviewTarget of the method
     * @param input closing price for period
     * @return  void
     *
     */
    public void performDecisionTest(EvaluateMethodSignals evaluator,
                                    String stockName,
                                    double[] input,
                                    int decRSIPeriod,
                                    int lowestThreshold,
                                    int highestThreshold) {

        boolean change=false;
        MInteger outBegIdx = new MInteger();
        MInteger outNbElement = new MInteger();

        double[] output = this.actionRSI(input,
                                        outBegIdx,
                                        outNbElement,
                                        decRSIPeriod);

        if (config.inputPrintResults) {
            DateIterator dateIterator = new DateIterator(portfolioConfig.inputStartingDate,
                    portfolioConfig.inputEndingDate);

            dateIterator.move(outBegIdx.value);
            for (int elem = 0; elem < outNbElement.value && dateIterator.hasNext(); elem++) {
                Date date = dateIterator.next();
                if (output[elem] < lowestThreshold && change == false) {
                    evaluator.buy(input[elem + outBegIdx.value], -1, date);
                    change = true;
                } else if (output[elem] > highestThreshold && change == true) {
                    evaluator.sell(input[elem + outBegIdx.value], -1, date);
                    change = false;
                }
            }
        } else {
            for (int elem = 0; elem < outNbElement.value; elem++) {
                if (output[elem] < lowestThreshold && change == false) {
                    evaluator.buy(input[elem + outBegIdx.value], -1);
                    change = true;
                } else if (output[elem] > highestThreshold && change == true) {