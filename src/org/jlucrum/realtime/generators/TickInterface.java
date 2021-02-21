/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlucrum.realtime.generators;

import com.espertech.esper.client.UpdateListener;

/**
 *
 * @author house
 */
public interface TickInterface extends UpdateListener {

    public boolean subscribeForTicks();
    p