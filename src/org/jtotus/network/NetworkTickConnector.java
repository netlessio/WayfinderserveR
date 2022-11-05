/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jtotus.network;

import org.jlucrum.realtime.eventtypes.StockTick;

/**
 *
 * @author house
 */
public interface NetworkTickConnector {
    public boolean connect();

    public StockTick getTick(String stockName);
}
