
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

/**
 *
 * @author Evgeni Kappinen
 */
public class GenerateJTotusType {



    private static void declare_type(String type) {
        System.out.printf("private %s %sType ;\n",type, type);
    }

    private static void constructor(String type) {
        System.out.printf("jTotusType(%s tmpType) { %sType = tmpType; }",
                type, type);
    }

     private static void setvalue(String type) {
        System.out.printf("setValue(%s tmpType) { %sType = tmpType; }",
                type, type);
    }

    static void main (String av[]) {


        constructor("int");
        setvalue("int");
    }


}