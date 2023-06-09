
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

package org.jtotus.config;

import java.io.File;
import java.util.ArrayList;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author Evgeni Kappinen
 */
public class ConfPortfolio {
    public String[] inputListOfStocks;
    public double inputAssumedBudjet;
    public DateTime inputStartingDate = null;
    public DateTime inputEndingDate = null;
    public DateTime inputStartIndicatorDate = null;
    public boolean useCurentDayAsEndingDate = true;
    public ArrayList<String> autoStartedMethods;

    public static final String portfolioName = "OMXHelsinki";
    private static final String pathToGroovyScripts = "modules" + File.separator + "groovy" + File.separator + "scripts";
    private static final String pathToResults = portfolioName + File.separator + "results";

    public ConfPortfolio() {
        inputListOfStocks = fetchGUIStockNames();
        inputAssumedBudjet = 6000;

        inputEndingDate = new DateTime();
        inputStartingDate = new DateTime().minusDays(350);

        inputStartIndicatorDate = inputStartingDate.toDateTime().minusDays(350);
        autoStartedMethods = new ArrayList<String>();
    }

    /**
     * @return the portofoliName
     */
    public String getPortfolioName() {
        return portfolioName;

    }

    public String[] fetchGUIStockNames() {
        GUIConfig config = null;
        ConfigLoader<GUIConfig> loader = new ConfigLoader<GUIConfig>("GUIConfig");

        //config = new GUIConfig();
        config = loader.getConfig();
        //if config does not exists create new one
        if (config == null) {
            config = new GUIConfig();
            loader.storeConfig(config);
        }

        return config.fetchStockNames();

    }

    public static synchronized ConfPortfolio getPortfolioConfig() {
        ConfPortfolio portfolioConfig;

        ConfigLoader<ConfPortfolio> configPortfolio =
                new ConfigLoader<ConfPortfolio>("OMXHelsinki");

        portfolioConfig = configPortfolio.getConfig();
        if (portfolioConfig == null) {
            //Load default values
            portfolioConfig = new ConfPortfolio();
            configPortfolio.storeConfig(portfolioConfig);
        }
        return portfolioConfig;
    }

    public boolean isAutoStarted(String methodName) {
        return autoStartedMethods.contains(methodName);
    }

    public void setAutoStarted(String methodName) {
        autoStartedMethods.add(methodName);
    }


    public static String getPathToGroovyScripts() {
        return pathToGroovyScripts;
    }

    public static String getPathToResults() {
        DateTime cal = new DateTime();
        DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd");

        String path = pathToResults + File.separator
                + formater.print(cal) + File.separator;

        File dirs = new File(path);
        if (dirs.exists()) {
            dirs.mkdirs();
        }
        return path + "LongTermResults";
    }
}