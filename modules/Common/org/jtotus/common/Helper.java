
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

package org.jtotus.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;



/**
 *
 * @author Evgeni Kappinen
 */
public class Helper {
    private static Helper help = null;
    private int debugLvl = 1;
    private String []debugFilter= { "SimpleMovinAvg",
                                    //"jtotus.database.FileSystemFromHex",
                                  //  "jtotus.database.NetworkNordnet",
                                  //  "jtotus.common.Helper",
                                    "jtotus.threads.Dispatcher",
                                    //"jtotus.database.NetworkOP",
                                    //"jtotus.graph.JtotusGraph",
                                    //"jtotus.database.LocalJavaDB"
                                    };


    protected Helper() {

    }

    public synchronized static Helper getInstance() {

        if (help == null) {
            help = new Helper();
        }
        return help;
    }

    public synchronized void setDebugLvl(int lvl){
        debugLvl = lvl;
    }



    public synchronized void debug(int lvl, String pattern, Object... arguments) {

        if(lvl >= debugLvl) {
            System.out.printf(pattern, arguments);
        }
    }

   public synchronized void debug(String filter, String pattern, Object... arguments) {

       if (debugFilter==null){
           return;
       }

       for(int i=0; i < debugFilter.length;i++)
       {
            if (debugFilter[i].compareTo(filter) == 0)
            {
                System.out.printf("[%s] ",filter);
                System.out.printf(pattern, arguments)   ;
                break;
            }
       }
    }


    public synchronized void printCrtDir() {

     File dir1 = new File (".");
     File dir2 = new File ("..");
     try {
       System.out.println ("Current dir : " + dir1.getCanonicalPath());
       System.out.println ("Parent  dir : " + dir2.getCanonicalPath());
       }
     catch(Exception e) {
       e.printStackTrace();
       }
     }

     public synchronized Float stringToFloat(String tmp)
    {
        return Float.valueOf(tmp);
     }

    private synchronized void dumpArray(double []array) {
        for (int i = 0; i < array.length;i++){
            System.out.printf("%.3f,", array[i]);
            if ((i % 10) == 0) {
               System.out.printf("\n");
            }
        }
        System.out.printf("\n");
    }

    public static double[] putAsFirstToArray(double[]oldArray, double newValue) {
        double newArray [] = null;

        if (oldArray==null) {
            newArray = new double[1];
            newArray[0] = newValue;
            return newArray;
        }
        
        newArray = new double[oldArray.length+1];
        System.arraycopy(oldArray, 0, newArray, 1, oldArray.length);
        newArray[0] = newValue;
        return newArray;
    }

    public static double[] putAsLastToArray(double[]oldArray, double newValue) {
        double newArray [] = null;

        if (oldArray==null) {
            newArray = new double[1];
            newArray[0] = newValue;
            return newArray;
        }

        newArray = new double[oldArray.length+1];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        newArray[newArray.length-1] = newValue;
        return newArray;
    }

    
}