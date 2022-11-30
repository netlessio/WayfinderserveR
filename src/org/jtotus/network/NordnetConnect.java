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
package org.jtotus.network;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jlucrum.realtime.eventtypes.StockTick;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jtotus.config.ConfigLoader;
import org.jtotus.config.GUIConfig;
import org.jtotus.engine.StartUpLoader;
import org.jtotus.network.BrokerConnector.ConnectorState;

/**
 *
 * @author Evgeni Kappinen
 */
public class NordnetConnect implements NetworkTickConnector {
    ConnectorState state = BrokerConnector.state.INITIAL;
    //private static final String _LOGIN_URL_ = "https://www.nordnet.fi/mux/login/login.html";
    //private static final String _LOGIN_URL_ = "https://www.nordnet.fi/mux/login/start.html";
    private static final String _LOGIN_URL_ = "https://www.nordnet.fi/mux/login/startFI.html";
    private static final String _LOGININPUT_URL_ = "https://www.nordnet.fi//mux/login/login.html";
    //private static final String _PORTFOLIO_URL_ = "https://www.nordnet.fi/mux/web/depa/mindepa/depaoversikt.html";
    private static final String _PORTFOLIO_URL_ = "https://www.nordnet.fi/mux/web/user/overview.html";
    private static final String _STOCK_INFO_URL_ = "https://www.nordnet.fi/mux/web/marknaden/aktiehemsidan/index.html";
    private static final String _ECRYPT_JS_ = "https://www.nordnet.fi/now/js/encrypt.js";

    private HashMap<String, Integer> stockNameToIndex = null;
    private BrokerConnector connector = null;
    private final static Log log = LogFactory.getLog( NordnetConnect.class );
    

    // Connects to login page, get seeds for user and password
    // POST data to server, by calling NordnetConnector
    private void fillStockNamesConverter() {

        if (stockNameToIndex != null) {
            return;
        }

        stockNameToIndex = new HashMap<String, Integer>();

        stockNameToIndex.put("Cargotec Oyj", 29983);
        //stockNameToIndex.put("Elisa Oyj","ELI1V.HSE");
        stockNameToIndex.put("Fortum Oyj", 24271);
        stockNameToIndex.put("Kemira Oyj", 24292);
        stockNameToIndex.put("KONE Oyj", 75061);
        stockNameToIndex.put("Konecranes Oyj", 24284);
        stockNameToIndex.put("Metso Oyj", 24302);
        stockNameToIndex.put("Neste Oil", 29375);
        stockNameToIndex.put("Nokia Oyj", 24311);
        //stockNameToIndex.put("Nokian Renkaat Oyj","NRE1V.HSE");
        stockNameToIndex.put("Nordea Bank AB", 24308);
        stockNameToIndex.put("Outokumpu Oyj", 24321);
        stockNameToIndex.put("Outotec Oyj", 36695);
        stockNameToIndex.put("Pohjola Bank A", 24316);
        stockNameToIndex.put("Rautaruukki Oyj", 24342);
        stockNameToIndex.put("Sampo Oyj A", 24346);
        stockNameToIndex.put("Sanoma Oyj", 24366);
        stockNameToIndex.put("Stora Enso Oyj A", 24359);
        stockNameToIndex.put("TeliaSonera AB", 24381);
        stockNameToIndex.put("Tieto Oyj", 24376);
        stockNameToIndex.put("UPM-Kymmene Oyj", 24386);
        stockNameToIndex.put("Wärtsilä Corporation", 24394);
        stockNameToIndex.put("YIT Oyj", 24397);

    }

    public boolean authenticated() {
        String loginPage = null;

        if (connector == null) {
            System.err.printf("Failure connector is empty\n");
            return false;
        }

        loginPage = connector.getPage(_POR