
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
package org.jlucrum.realtime.indicators;

import org.jlucrum.realtime.eventtypes.StockTick;
import org.jlucrum.realtime.ranalyzer.Rexecutor;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Evgeni Kappinen
 */
public class SimpleTechnicalIndicators extends StockIndicator<StockTick> {

    public static DataTypes dataTypes;
    public enum DataTypes {
        VOLUME,
        LATESTPRICE,
        LATESTBUY,
        LATESTSELL,
        LATESTHIGH,
        LATESTLOW,
        REVENUE
    }

    public SimpleTechnicalIndicators() {
        super();
    }

    public double VOLUME(int i) {
        return super.getTick(i).getVolume();
    }

    public double LATESTPRICE(int i) {
        return super.getTick(i).getLatestPrice();
    }

    public double LATESTHIGH(int i) {
        return super.getTick(i).getLatestHighest();
    }

    public double LATESTLOW(int i) {
        return super.getTick(i).getLatestLowest();
    }

    // Volume Rate of Change (VROC)
    // Source: http://www.mysmp.com/technical-analysis/volume-rate-of-change.html
    public double vroc(int iIndex, int n) {
        if (iIndex - n <= 0) {