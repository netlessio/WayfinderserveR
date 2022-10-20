
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

import org.jlucrum.realtime.eventtypes.MarketData;
import org.jtotus.common.MethodResults;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import java.util.Calendar;
import java.util.Date;
import org.jtotus.common.DateIterator;
import org.jtotus.config.ConfigLoader;
import org.jtotus.gui.graph.GraphSender;
import java.io.File;
import org.jtotus.common.NumberRangeIter;
import org.jtotus.config.ConfTaLibEMA;

/**
 *
 * @author Evgeni Kappinen
 */
public class TaLibEMA extends TaLibAbstract implements MethodEntry {

    /*Stock list */
    private double avgSuccessRate = 0.0f;
    private int totalStocksAnalyzed = 0;
    //INPUTS TO METHOD:
    public Calendar inputStartingDate = null;
    public ConfTaLibEMA config = null;
    ConfigLoader<ConfTaLibEMA> configFile = null;

    public void loadInputs(String configStock) {

        configFile = new ConfigLoader<ConfTaLibEMA>(super.portfolioConfig.portfolioName
                + File.separator
                + configStock
                + File.separator
                + this.getMethName());

        // new ConfigLoader<ConfTaLibEMA>(portfolio+File.separator+this.getMethName());

        if (configFile.getConfig() == null) {
            //Load default values
            config = new ConfTaLibEMA();
            configFile.storeConfig(config);
        } else {
            config = configFile.getConfig();
        }


        configFile.applyInputsToObject(this);
    }

    public MethodResults performEMA(String stockName, double[] input) {

        double[] output = null;
        MInteger outBegIdx = null;
        MInteger outNbElement = null;
        int period = 0;

        this.loadInputs(stockName);

        final Core core = new Core();

        period = input.length - 1;
        final int allocationSize = period - core.emaLookback(config.inputEMAPeriod);

        if (allocationSize <= 0) {
            System.err.printf("No data for period (%d)\n", allocationSize);
            return null;
        }



        output = new double[allocationSize];