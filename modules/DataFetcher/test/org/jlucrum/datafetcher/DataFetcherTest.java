/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlucrum.datafetcher;

import java.util.Map.Entry;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author home
 */
public class DataFetcherTest {
    
    public DataFetcherTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testDataSource() {
        DataFetcher instance = new DataFetcher();
        
        
        instance.setSource("NasdaqOmxNordic");
        Map <String,Double>result = instance.fetchPeriodData("Metso Oyj", "2011-09-13", "2011-09-15", "close");
        
        for (Entry<String, Double> entry : result.entrySet()) {
            System.out.printf("%s - %f\n", entry.getKey(), entry.getValue());
        }
        
        assertEquals(result.get("2011-09-15"), Double.v