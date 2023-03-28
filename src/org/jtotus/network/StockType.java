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


package org.jtotus.network;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Date;
import org.joda.time.DateTime;
import org.jtotus.common.Helper;
import org.jtotus.common.StockNames;
import org.jtotus.database.DataFetcher;




/**
 *
 * @author Evgeni Kappinen
 */
public class StockType implements Iterator{
    private String stockName=null;
    private StockNames stocks = new StockNames();
    private Iterator mapIter = null;
    private final DataFetcher fetcher = new DataFetcher();
    private Helper help=Helper.getInstance();


    public StockType() {
        mapIter = stocks.iterator();
    }
    
    public StockType(String name) {
        stockName = name;
        mapIter = stocks.iterator();
    }

    public boolean hasNext() {
        return mapIter.hasNext();
    }

    public Object next() {
    