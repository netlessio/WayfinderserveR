
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

https://www.nordnet.fi/mux/page/hjalp/ordHjalp.html?ord=diagram%20momentum

momentum

Momentum toimii hyvin markkinoilla, joilla on havaittavissa joko nousevia tai laskevia trendejä.

Momentum kertoo sen, kuinka paljon osakkeen kurssi on muuttunut
valitulla aikavälillä. Se antaa seuraavat signaalit:
- Osta, kun indikaattori käy pohjalla ja kääntyy ylös
- Myy, kun indikaattori käy huipulla ja kääntyy alas


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
import org.jtotus.common.NumberRangeIter;
import org.jtotus.config.ConfTaLibMOM;
import org.jtotus.config.ConfigLoader;
import org.jtotus.gui.graph.GraphSender;
import org.jtotus.methods.utils.Normalizer;

/**
 *
 * @author Evgeni Kappinen
 */
public class TaLibMOM extends TaLibAbstract implements MethodEntry {

    private double avgSuccessRate = 0.0f;
    private int totalStocksAnalyzed = 0;
    public ConfTaLibMOM config = null;
    public ConfigLoader<ConfTaLibMOM> configFile = null;
    //INPUTS TO METHOD:
    private int inputParam_Period = 10;

    public TaLibMOM() {
        super();
    }

    public void loadInputs(String configStock) {

        configFile = new ConfigLoader<ConfTaLibMOM>(super.portfolioConfig.portfolioName
                + File.separator
                + configStock
                + File.separator
                + this.getMethName());

        if (configFile.getConfig() == null) {
            //Load default values
            config = new ConfTaLibMOM();
            configFile.storeConfig(config);
        } else {
            config = (ConfTaLibMOM) configFile.getConfig();
        }

        configFile.applyInputsToObject(this);
    }

    //MOM
    public MethodResults performMOM(String stockName, double []input) {
        double[] output = null;
        MInteger outBegIdx = null;
        MInteger outNbElement = null;
        int period = 0;

        this.loadInputs(stockName);

        final Core core = new Core();

        period = input.length - 1;
        final int allocationSize = period - core.momLookback(config.inputMOMPeriod);

        if (allocationSize <= 0) {
            System.err.printf("No data for period (%d)\n", allocationSize);
            return null;
        }



        output = new double[allocationSize];
        outBegIdx = new MInteger();
        outNbElement = new MInteger();

        RetCode code = core.mom(0, period - 1, input,
                config.inputMOMPeriod,
                outBegIdx,
                outNbElement, output);

        if (code.compareTo(RetCode.Success) != 0) {
            //Error return empty method results
            System.err.printf("SMI failed!\n");