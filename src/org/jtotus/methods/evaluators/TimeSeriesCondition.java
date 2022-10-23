
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

package org.jtotus.methods.evaluators;

import java.util.ArrayList;

/**
 *
 * @author Evgeni Kappinen
 */
public class TimeSeriesCondition {
    public int CROSSING = 0;
    public int BIGGER = 1;
    public int SMALLER = 2;


    private ArrayList<TimeSeriesFunction> funcList = null;

    private int a = 0;
    private int op = 0;
    private int b = 0;


    private boolean previous = false;
    private boolean and = false;

    
    public TimeSeriesCondition() {
        funcList = new ArrayList<TimeSeriesFunction> ();
        
    }

    public TimeSeriesFunction declareFunc(String name, double data[]) {
        TimeSeriesFunction series = new TimeSeriesFunction();
        series.setFuncName(name);
        series.setData(data);
        funcList.add(series);

        return series;
    }

     public boolean abiggerb(){
        TimeSeriesFunction aFunc = funcList.get(0);
        TimeSeriesFunction bFunc = funcList.get(1);

         return aFunc.get(getA()) > bFunc.get(getB());
     }

     public boolean asmallerb(){