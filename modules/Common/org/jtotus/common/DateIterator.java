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
package org.jtotus.common;

import java.util.Date;
import java.util.Iterator;
import org.joda.time.DateTime;

/**
 *
 * @author Evgeni Kappinen
 */
public class DateIterator implements Iterator<Date>, Iterable<Date> {

    private int step = 1;
    private DateTime toDate;
    private DateTime fromDate;
    private DateTime current;
    private boolean debug = false;
    private boolean first = true;

    // Starts with past date(fromDate) and going towards ending date
    public DateIterator(Date tmpStart, Date tmpEnd) {
        fromDate = new DateTime(tmpEnd);
        toDate = new DateTime(tmpStart);

        if (toDate.compareTo(fromDate) < 0) {
            System.err.printf("Warning startin date is afte ending date! Reversing dates("
                    + fromDate.toDate() + ":" + toDate.toDate() + "\n");
            DateTime tmp = fromDate.toDateTime();
            fromDate = toDate;
            toDate = tmp;
            System.err.printf("New time startin date is afte ending date! Reversing dates("
                    + fromDate.toDate() + ":" + toDate.toDate() + "\n");
        }

//        DateTimeFormatter formater = DateTimeFormat.forPattern("dd-MM-yyyy");
//        System.err.printf("Assigned start2:%s, toDate:%s\n", formater.print(fromDate), formater.print(toDate));

        current = fromDate.toDateTime();
    }

    // Starts with past date(fromDate) and going towards ending date
    public DateIterator(DateTime startDate, DateTime endDate) {
        fromDate = startDate.toDateTime();
        toDate = endDate.toDateTime();

        if (!toDate.isAfter(fromDate)) {
            System.err.printf("Warning startin date is afte ending date! Reversing dates("
                    + fromDate.toDate() + ":" + toDate.toDate() + "\n");
            DateTime tmp = fromDate.toDateTime();
            fromDate = toDate;
     