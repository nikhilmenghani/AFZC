/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.DiskOperations.Write;
import flashablezipcreator.UserInterface.Preference;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Nikhil
 */
public class Logs {

    public static String logFile = "log.log";
    public static String newLine = "\n";

    public static void write(String strToWrite) {
        if (Preference.pp.saveLogs) {
            Write w = new Write();
            w.appendStringToFile(strToWrite + newLine, logFile);
        }
    }

    public static String getTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("d_MMM_y_H_m_s");
        return sdf.format(cal.getTime());
    }
    
    public static String getShortTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMd");
        System.out.println(sdf.format(cal.getTime()));
        return sdf.format(cal.getTime());
    }

    public static String getExceptionTrace(Exception e) {
        String detail = e.getClass().getName() + ": " + e.getMessage();
        for (StackTraceElement s : e.getStackTrace()) {
            detail += "\n\t" + s.toString();
        }
        while ((e = (Exception) e.getCause()) != null) {
            detail += "\nCaused by: ";
            for (final StackTraceElement s : e.getStackTrace()) {
                detail += "\n\t" + s.toString();
            }
        }
        return detail;
    }
}
