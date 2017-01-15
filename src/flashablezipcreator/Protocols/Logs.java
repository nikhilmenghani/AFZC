/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.DiskOperations.Write;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Nikhil
 */
public class Logs {

    public static String logFile = "log.log";
    public static boolean show = true;
    public static String newLine = System.getProperty("line.separator");

    public static void write(String strToWrite) {
        if (show) {
            Write w = new Write();
            w.appendStringToFile(strToWrite + newLine, logFile);
        }
    }

    public static String getTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("DD_MMM_YYYY_HH_mm_ss");
        System.out.println();
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
