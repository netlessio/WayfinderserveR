
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
 *

 */

package org.jtotus.methods;

import java.math.BigDecimal;
import org.jtotus.common.Helper;
import org.jtotus.common.MethodResults;
import org.jtotus.network.StockType;

/**
 * @author Evgeni Kappinen
 */
public class StatisticsFreqPeriod extends TaLibAbstract implements MethodEntry {
    private int maxPeriod = 20;
    private boolean debug = false;
    private static final int POSITIVE = 0;
    private static final int NEGATIVE = 1;
    private static final int STILL = 2;
    private StockType stockType = null;

    public String getMethName() {
        return "StatisticsFreqPeriod";
    }

    private int normilize(double tmp) {

        if (tmp > 0) {
            return 1;
        } else if (tmp < 0) {
            return -1;
        }

        return 0;
    }

    public int lastTrend(String stockName) {
        BigDecimal data = null;
        double table[] = null;
        int mainDirection = 0;
        int strikes = 0;

        if (stockType == null) {
            stockType = new StockType();
        }

        stockType.setStockName(stockName);

        for (int i = 0; i < this.getMaxPeriod(); i++) {

            data = stockType.fetchPastDayClosingPrice(i);
            if (data == null) {
                continue;
            }
