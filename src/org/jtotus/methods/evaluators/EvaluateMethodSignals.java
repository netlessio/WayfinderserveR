
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.jtotus.config.ConfPortfolio;
import org.jtotus.gui.graph.GraphSender;

/**
 * @author Evgeni Kappinen
 *         TODO:all data in cents!
 */
public class EvaluateMethodSignals {

    private BigDecimal currentCapital = null;
    private BigDecimal assumedCapital = null;
    private long stockCount = 0;
    private BigDecimal previousCapital = null;
    private GraphSender graphSender = null;
    private double numberOfWinningTrades = 0;
    private double numberOfLosingTrades = 0;
    private boolean newBestIsFound = false;
    //Best state
    private GraphSender bestResultsGraph = null;
    private BigDecimal currentBestCapital = null;
    private double bestNumberOfWinningTrades = 0;
    private double bestNumberOfLosingTrades = 0;
    private final ConfPortfolio portfolio = ConfPortfolio.getPortfolioConfig();


    public EvaluateMethodSignals() {
        assumedCapital = BigDecimal.valueOf(portfolio.inputAssumedBudjet);
        currentCapital = BigDecimal.valueOf(assumedCapital.doubleValue());
        if (this.getCurrentBestCapital() == null) {
            this.setCurrentBestCapital(BigDecimal.valueOf(0.0));
        }
    }


    /* Initialization function, should be run
    * for each state for StateIterator.
    *
    * @param reviewTarget Current stock name
    * @param seriesName   Method related series name
    * @param originalCapital Assumed Capital from portofolio
    * @param stockGraph   Stores values for each iteration of StateIterator.class
    *
    * @return boolean  Currently, Always returns true
    *
    */

    public boolean initialize(String reviewTarget,
                              String seriesName,
                              Double originalCapital) {

        setCurrentCapital(BigDecimal.valueOf(originalCapital));
        assumedCapital = BigDecimal.valueOf(originalCapital);

        if (this.getCurrentBestCapital() == null) {
            this.setCurrentBestCapital(BigDecimal.valueOf(0.0));
        }
        stockCount = 0;

        /*All points, which lead to signal are stored
          in Graph container.
        */

        graphSender = new GraphSender(reviewTarget);
        graphSender.setSeriesName(seriesName);
        this.numberOfLosingTrades = 0.0;
        this.numberOfWinningTrades = 0.0;
        newBestIsFound = false;

        return true;
    }

    public BigDecimal brockerExpensePerAction(BigDecimal tradingVolume) {
        //Nordnet State 3 -> 0.15% /Min. 7
        BigDecimal payment = tradingVolume.setScale(5, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(100.0))
                .multiply(BigDecimal.valueOf(0.15));

        if (payment.compareTo(BigDecimal.valueOf(7)) > 0) {
            return payment;
        }

        return BigDecimal.valueOf(7.00);
    }

    public  long getStockCount() {
        return  stockCount;
    }

    public EvaluateMethodSignals buy(double price, int amount) {

        if (getCurrentCapital().subtract(this.brockerExpensePerAction(getCurrentCapital()))
                .compareTo(BigDecimal.valueOf(0.0)) <= 0) {
            // System.err.printf("There is no money left\n");
            return null;
        }

        if (amount == -1) {//ALL-in
            stockCount = getCurrentCapital().subtract(this.brockerExpensePerAction(getCurrentCapital()))
                    .divide(BigDecimal.valueOf(price), 3, RoundingMode.HALF_DOWN)
                    .intValue();

            setPreviousCapital(getCurrentCapital());
            setCurrentCapital(BigDecimal.valueOf(0.0));

        } else {
            //TODO: implement
        }

        return this;
    }

    public EvaluateMethodSignals buy(double price, int amount, Date date) {
        this.buy(price, amount);

        String annotation = "Buy";
        graphSender.addForSending(date, price, annotation);

        return this;
    }

