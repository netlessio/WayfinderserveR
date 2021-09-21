/*
 * JStock - Free Stock Market Software
 * Copyright (C) 2009 Yan Cheng CHEOK <yccheok@yahoo.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */


package org.jtotus.database;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigDecimal;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 *
 * @author Evgeni Kappinen
 */
public class FileSystemFromHex implements InterfaceDataBase {

    String pathToDataBaseDir = "OMXNordic/";
    String filePattern = "yyyy-MM-dd";
    private final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(filePattern);


    private int columnHighestPrice = 1;
    private int columnLowestPrice = 2;
    private int columnClosingPrice = 3;
    private int columnAvrPrice = 4;
    private int columnTotalVolume = 5;
    private int columnTurnOver = 6;
    private int columnTrades = 7;


    //TODO:find column* values by reading first line in file,
    // if contains string which indicates value type change value.

    private FileFilter filterForDir()
    {
       FileFilter fileFilter = new FileFilter() {
           public boolean accept(File file)
           {
                if(!file.isFile() || !file.canRead()) {
                    return false;
                }

                String name = file.getName();
                if (!name.endsWith(".xls"))
                {
                    return false;
                }
               return true;
           }
       };
       return fileFilter;
    }

    
    
public BigDecimal fetchHighestPrice(String stockName, DateTime calendar){
    return this.fetchValue(stockName, calendar, columnHighestPrice);
}

public BigDecimal fetchLowestPrice(String stockName, DateTime calendar){
    return this.fetchValue(stockName, calendar, columnLowestPrice);
}

public BigDecimal fetchClosingPrice(String stockName, DateTime calendar){
    return this.fetchValue(stockName, calendar, columnClosingPrice);
}

public BigDecimal fetchAveragePrice(String stockName, DateTime calendar){
    return this.fetchValue(stockName, calendar, columnAvrPrice);
}

public BigDecimal fetchTurnOver(String stockName, DateTime calendar){
    return this.fetchValue(stockName, calendar, columnTurnOver);
}

public BigDecimal fetchTrades(String stockName, DateTime calendar){
    return this.fetchValue(stockName, calendar, columnTrades);
}

    public BigDecimal fetchData(String stockName, DateTime date, String type) {

        if (type.compareTo("HIGH") == 0) {
            return this.fetchValue(stockName, date, columnHighestPrice);
        } else if (type.compareTo("LOW") == 0) {
            return this.fetchValue(stockNam