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

import java.util.Calendar;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

/**
 *
 * @author Evgeni Kappinen
 */
public class DayisHoliday {
    private static final int[]days= {1012010, 6012010, 2042010, 5042010,
                                     13052010, 6122010, 25062010, 10042009, 13042009,
                                     1052009, 21052009, 19062009, 24122009, 25122009,
                                     31122009, 24122010, 31122010, 6012011, 22042011,
                                     25042011, 26122008, 25122008, 1012009, 6012009,
                                     24122008, 31122008, 25062011, 2062011, 24062011};

    public static boolean isHoliday(Calendar date) {

        if (date == null) {
            return false;
        }

        if (date.get(Cal