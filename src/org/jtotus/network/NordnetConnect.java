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

/