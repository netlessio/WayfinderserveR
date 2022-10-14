
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
 *
 * http://tutorials.jenkov.com/java-collections/navigableset.html
 */

package org.jtotus.methods;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import org.joda.time.DateTime;
import org.jtotus.common.DateIterator;
import org.jtotus.common.Helper;
import org.jtotus.network.StockType;

/**
 *
 * @author Evgeni Kappinen
 */
public class PeriodClosingPrice {
    private StockType stock = null;
    private HashMap<Calendar, BigDecimal> priceList = null;
    private ArrayList<BigDecimal> sortedList = null;
    private Date endingDate = null;
    private boolean initDone = false;
    private Helper help = Helper.getInstance();
    public int raises = 0,total_dates = 0; //TODO: implement
    //Helps to identify, which stock period
    private String stockName = null;
    public int period = 90;
    
    public PeriodClosingPrice(StockType stockTmp) {
        stock = stockTmp;
        stockName = stock.getStockName();
        priceList = new HashMap<Calendar, BigDecimal>();
    }

    public PeriodClosingPrice(String stockTemp) {
        stockName = stockTemp;
        stock = new StockType(stockName);
        priceList = new HashMap<Calendar, BigDecimal>();
    }

    public String getStockName() {
        if (stockName == null && stock != null) {
            stockName =  stock.getStockName();
        }
        return stockName;
    }

    public StockType getStockType() {
        return stock;
    }

    public int getPeriodLength() {
        initList();
        
//         System.out.printf("The size of period:%d\n",priceList.size());
        return priceList.size();
    }
    public BigDecimal getPotential() {
        initList();
        
//        BigDecimal current = stock.fetchClosingPrice(endingDate);
        //FIXME !! loop (ending date)--
        BigDecimal current = stock.fetchCurrentClosingPrice();
        BigDecimal max = this.getMaxValue();
        BigDecimal pot = max.subtract(current).abs();
        BigDecimal ret = pot.divide(current,MathContext.DECIMAL64).multiply(BigDecimal.valueOf(100.00));
        help.debug("PeriodClosingPrice",
                "Stock: %s ret:%f - %f =ret:%f\n",
                stock.getStockName(),max.floatValue(),current.floatValue(),ret.floatValue());
        return ret;
    }


    public BigDecimal getLowPotential(){
        initList();

        //FIXME; 
        BigDecimal current = stock.fetchCurrentClosingPrice();
        BigDecimal min = getMinValue();
        BigDecimal lowPot = min.subtract(current).abs();
        BigDecimal ret = lowPot.divide(current,MathContext.DECIMAL64).multiply(BigDecimal.valueOf(100.00));
        help.debug("PeriodClosingPrice",
                "Stock: %s ret:%f - %f =ret:%f\n",
                stock.getStockName(),min.floatValue(),current.floatValue(),ret.floatValue());
        return ret;
    }

    public void setStartDate(Date endDate) {
        endingDate = endDate;
        return;
    }
    
    private void initList(){
        //Get values
        if (initDone) {
            return;
        }
        
        if (endingDate == null) {
            endingDate = Calendar.getInstance().getTime();
        }

        DateTime endCal = new DateTime(endingDate);
        DateTime startCal = new DateTime(endingDate).minusDays(period);

        DateIterator iter = new DateIterator(startCal, endCal);

        BigDecimal closingPrice = null;
        BigDecimal previous = null;

        while(iter.hasNext()) {

            Date searchnigDate = iter.next();
            closingPrice = stock.fetchClosingPrice(searchnigDate);

            if (closingPrice != null) {

                Calendar calDate = Calendar.getInstance();
                calDate.setTime(searchnigDate);
                
                priceList.put(calDate, closingPrice);

                total_dates++;
                
                if (previous != null) {
                    if (previous.compareTo(closingPrice) == 1) {
                        raises++;
                    }
                }
                
            previous = closingPrice;
            }
        }

        sortedList = new ArrayList<BigDecimal>(priceList.values());
        Collections.sort(sortedList);
        initDone = true;
    }

    public BigDecimal getMaxValue(){
        initList();
        if (sortedList != null && !sortedList.isEmpty()) {
            return  sortedList.get(sortedList.size()-1);
        }

        return null;
    }

    public BigDecimal getMinValue() {
        initList();
        if (sortedList != null) {
            return sortedList.get(0);
        }
        return null;
    }

}