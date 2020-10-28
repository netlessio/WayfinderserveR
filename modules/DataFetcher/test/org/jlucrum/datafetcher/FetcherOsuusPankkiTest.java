/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlucrum.datafetcher;

import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author home
 */
public class FetcherOsuusPankkiTest {
    private String patternString = "yyyy-MM-dd";
    private DateTimeFormatter formatter = DateTimeFormat.forPattern(patternString);
    
    public FetcherOsuusPankkiTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testFetchData() {
        System.out.println("fetchData");
        String stockName = "KONE Oyj";
        int type = 0;
        
        FetcherOsuusPankki instance = new Fetcher