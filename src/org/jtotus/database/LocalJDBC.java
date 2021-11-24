
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

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.ArrayUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jtotus.common.DateIterator;

/**
 * @author Evgeni Kappinen
 */
public class LocalJDBC implements InterfaceDataBase {
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
    private boolean debug = false;
    private DataFetcher fetcher = null;

    @Override
    public double[] fetchDataPeriod(String stockName, DateTime fromDate, DateTime toDate, String type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static enum DataTypes {
        CLOSE,
        VOLUME
    };
    
    private Connection getConnection() throws SQLException {
        //FIXME: login & password from config
        return DriverManager.getConnection("jdbc:h2:~/.jtotus/local_database;LOG=0;CACHE_SIZE=65536;LOCK_MODE=0;UNDO_LOG=0",
                "sa", "sa");
    }

    //TODO;create procedures
    private void createTable(Connection con, String stockTable) {
        PreparedStatement createTableStatement = null;
        try {

            String statement = "CREATE TABLE IF NOT EXISTS " + stockTable + " ("
                    + "ID IDENTITY AUTO_INCREMENT,"
                    + "DATE          DATE,"
                    + "TIME          TIME,"
                    + "OPEN          DECIMAL(18,4),"
                    + "CLOSE         DECIMAL(18,4),"
                    + "HIGH          DECIMAL(18,4),"
                    + "LOW           DECIMAL(18,4),"
//                    + "AVRG           DECIMAL(18,4),"
//                    + "TRADES           DECIMAL(18,4),"
                    + "VOLUME        INT,"
                    + "PRIMARY KEY(ID));";

            createTableStatement = con.prepareStatement(statement);

            createTableStatement.execute();

        } catch (SQLException ex) {
            Logger.getLogger(LocalJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BigDecimal fetchClosingPrice(String stockName, DateTime date) {
        return this.fetchData(stockName, date, "CLOSE");
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public BigDecimal fetchData(String tableName, DateTime date, String column) {
        BigDecimal retValue = null;
        PreparedStatement pstm = null;
        Connection connection = null;
        ResultSet results = null;


        try {
            connection = getConnection();
            String statement = "SELECT " + column + " FROM " + this.normTableName(tableName) + " WHERE DATE=?";
            
            this.createTable(connection, this.normTableName(tableName));

            pstm = connection.prepareStatement(statement);

            java.sql.Date sqlDate = new java.sql.Date(date.getMillis());
            pstm.setDate(1, sqlDate);

            if (debug) {
                System.out.printf("Fetching:'%s' from'%s' Time" + date.toDate() + " Stm:%s\n", column, tableName, statement);
            }
            
            results = pstm.executeQuery();

//            System.out.printf("Results:%d :%d :%s (%d)\n",results.getType(), results.findColumn(column), results.getMetaData().getColumnLabel(1),java.sql.Types.DOUBLE);

            if (results.next()) {
                retValue = results.getBigDecimal(column);
            }

        } catch (SQLException ex) {
            System.err.printf("LocalJDBC Unable to find date for:'%s' from'%s' Time" + date.toDate() + "\n", column, tableName);
            //   Logger.getLogger(LocalJDBC.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (results != null) {
                    results.close(); results = null;
                }

                if (pstm != null) {
                    pstm.close(); pstm = null;
                }
                
                if (connection != null) {
                    connection.close(); connection = null;
                }
            } catch (SQLException ex) {
                Logger.getLogger(LocalJDBC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return retValue;
    }

    public void setFetcher(DataFetcher fetcher) {
        this.fetcher = fetcher;
    }

    public DataFetcher getFetcher() {
        if (this.fetcher == null) {
            this.fetcher = new DataFetcher();
        }

        return this.fetcher;
    }

    public double[] fetchPeriod(String tableName, DateTime startDate, DateTime endDate, String type) {
        BigDecimal retValue = null;
        PreparedStatement pstm = null;
        java.sql.Date retDate = null;
        ResultSet results = null;
        ArrayList<Double> closingPrices = new ArrayList<Double>(600);
        Connection connection = null;

        try {
            String query = "SELECT "+type+", DATE FROM " + this.normTableName(tableName) + " WHERE DATE>=? AND DATE<=? ORDER BY DATE ASC";
            // this.createTable(connection, this.normTableName(tableName));

            connection = this.getConnection();
            pstm = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            java.sql.Date startSqlDate = new java.sql.Date(startDate.getMillis());
            pstm.setDate(1, startSqlDate);

            java.sql.Date endSqlDate = new java.sql.Date(endDate.getMillis());
            pstm.setDate(2, endSqlDate);
            
            DateIterator dateIter = new DateIterator(startDate, endDate);
            
            results = pstm.executeQuery();
            DateTime dateCheck;
            
            if (debug) {    
                System.out.printf("start data %s end date: %s\n", startSqlDate.toString(), endSqlDate.toString());
            }

            
            
            while (dateIter.hasNext()) {
                dateCheck = dateIter.nextInCalendar();

                if (results.next()) {
                    retValue = results.getBigDecimal(1);
                    retDate = results.getDate(2);

                    DateTime compCal = new DateTime(retDate.getTime());
                    if (compCal.getDayOfMonth() == dateCheck.getDayOfMonth()
                            && compCal.getMonthOfYear() == dateCheck.getMonthOfYear()
                            && compCal.getYear() == dateCheck.getYear()) {
                        closingPrices.add(retValue.doubleValue());
                        continue;
                    } else {
                        results.previous();
                    }
                }
                
                BigDecimal failOverValue = getFetcher().fetchData(tableName, dateCheck, type);
                if (failOverValue != null) {
                    closingPrices.add(failOverValue.doubleValue());
                }
            }
            