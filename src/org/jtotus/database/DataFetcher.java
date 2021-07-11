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
package org.jtotus.database;

import java.util.LinkedList;
import java.math.BigDecimal;
import org.jlucrum.realtime.BrokerWatcher;
import org.jlucrum.realtime.eventtypes.MarketData;
import com.espertech.esper.client.EPRuntime;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jtotus.common.DayisHoliday;

/**
 *
 * @author Evgeni Kappinen
 */
public class DataFetcher {

    private LinkedList<InterfaceDataBase> listOfResources = null;
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
    private Cache cache = null;
    private LocalJDBC localJDBC = null;
    private boolean debug = false;

    public DataFetcher() {
        listOfResources = new LinkedList<InterfaceDataBase>();

        //Supported resource
        //listOfResources.add(new FileSystemFromHex());