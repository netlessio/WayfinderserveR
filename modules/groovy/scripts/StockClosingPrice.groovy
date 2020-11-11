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
 * http://blogs.sun.com/geertjan/entry/netbeans_groovy_editor_in_a
 *
 */

package jtotus.rulebase

import org.jtotus.common.StockType
import org.jtotus.methods.SimpleMovingAvg
import org.jtotus.common.Helper;
import org.jtotus.engine.Engine;
import org.jtotus.gui.graph.GraphPacket;
import org.jtotus.common.MethodResults;
//
//
//results = new MethodResults("StockClosingPrice");
//
//def drawClosingPrice (String reviewTarget, int daysToSearch) {
//
//stockType = new org.jtotus.common.StockType(re