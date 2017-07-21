/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

/**
 *
 * @author Nikhil
 */
public class Control {

    public static boolean forceTestUpdate = false;
    public static boolean forceBetaUpdate = false;
    public static boolean forceStableUpdate = false;
    public static boolean forceCheckOnStartUp = false;
    public static boolean forceFTP = true;
    public static String RawControlUrl = "https://raw.githubusercontent.com/nikhilmenghani/AFZC/v4.0/src/flashablezipcreator/Update/Control";

    public static void check() {
        if (Web.netIsAvailable()) {
            try {
                String data = Web.getHtmlContent(RawControlUrl);
                String[] commands = data.split("\n");
                forceTestUpdate = (commands[0].split(":"))[1].contains("1");
                forceBetaUpdate = (commands[1].split(":"))[1].contains("1");
                forceStableUpdate = (commands[2].split(":"))[1].contains("1");
                forceCheckOnStartUp = (commands[3].split(":"))[1].contains("1");
                forceFTP = (commands[4].split(":"))[1].contains("1");
            } catch (Exception e) {
                Logs.write("While checking Contols: " + Logs.getExceptionTrace(e));
            }
        }
    }
}
