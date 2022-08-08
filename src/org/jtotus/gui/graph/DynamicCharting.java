
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






 Source:
 * http://jchart2d.sourceforge.net/usage.shtml
 * http://kickjava.com/src/info/monitorenter/gui/chart/io/ADataCollector.java.htm


 */
package org.jtotus.gui.graph;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.TracePoint2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import java.awt.Color;

/**
 *
 * @author Evgeni Kappinen
 */
public class DynamicCharting extends Chart2D implements UpdateListener{
    private int windowSize = 200;
    private ITrace2D trace = new Trace2DLtd(windowSize);
    private long m_starttime = System.currentTimeMillis();
    private EPServiceProvider provider = null;

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    
    public ITrace2D getTrace() {
        return trace;
    }