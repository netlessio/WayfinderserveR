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
package org.jtotus.crypt;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Evgeni Kappinen
 */
public class JtotusKeyRingPassword {

    private String keyRingPassword = null;
    private static final JtotusKeyRingPassword instance = new JtotusKeyRingPassword();
    private boolean cancel = false;
    private Object passLock = new Object();

    private JtotusKeyRingPassword() {
    }

    //FIXME:re-check if it is safe to keep password in signleton ?
    public synchronized static JtotusKeyRingPassword getInstance() {
        return instance;
    }

    public synchronized String getKeyRingPassword() {
        boolean keyFound = false;
        int defaultSleepingTime = 60; //In Seconds

        while (keyFound == false && defaultSleepingTime > 0) {
            synchronized (passLock) {
                if (keyRingPassword != null) {
                    keyFound = true;
                }else if (cancel) {
                    return null;
                }
            }

            if (keyFound == false) {
                try {
                    System.out.println("Sleepings...");
                    Thread.sleep(1000);
                    defaultSleepingTime--;
                } catch (InterruptedException ex) {
                    Logger.getLogger(JtotusKeyRingPassword.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return keyRingPassword;
    }

    public synchronized void putKeyRingPassword(String password) {
        synchronized (passLock) {
            keyRingPassword = password;
        }
    }

    public synchronized void cancel() {
        synchronized (passLock) {
            cancel = true;
        }
    }
}
