/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import flashablezipcreator.UserInterface.Preference;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JOptionPane;

/**
 *
 * @author Nikhil
 */
public class Update {

    public static String RawLatestBetaVersion = "https://raw.githubusercontent.com/nikhilmenghani/AFZC/v4.0/src/flashablezipcreator/Update/LatestBetaVersion";
    public static String RawLatestStableVersion = "https://raw.githubusercontent.com/nikhilmenghani/AFZC/v4.0/src/flashablezipcreator/Update/LatestStableVersion";
    public static String RawLatestTestVersion = "https://raw.githubusercontent.com/nikhilmenghani/AFZC/v4.0/src/flashablezipcreator/Update/LatestTestVersion";
    public static String RawDownloadLatestBetaVersion = "https://raw.githubusercontent.com/nikhilmenghani/AFZC/v4.0/src/flashablezipcreator/Update/DownloadLatestBetaVersion";
    public static String RawDownloadLatestStableVersion = "https://raw.githubusercontent.com/nikhilmenghani/AFZC/v4.0/src/flashablezipcreator/Update/DownloadLatestStableVersion";
    public static String RawDownloadLatestTestVersion = "https://raw.githubusercontent.com/nikhilmenghani/AFZC/v4.0/src/flashablezipcreator/Update/DownloadLatestTestVersion";
    public static String RawChangeLogBeta = "https://raw.githubusercontent.com/nikhilmenghani/AFZC/v4.0/src/flashablezipcreator/Update/ChangeLogBeta";
    public static String RawChangeLogStable = "https://raw.githubusercontent.com/nikhilmenghani/AFZC/v4.0/src/flashablezipcreator/Update/ChangeLogStable";
    public static String RawChangeLogTest = "https://raw.githubusercontent.com/nikhilmenghani/AFZC/v4.0/src/flashablezipcreator/Update/ChangeLogTest";
    public static String RawUpcomingFeatures = "https://raw.githubusercontent.com/nikhilmenghani/AFZC/v4.0/src/flashablezipcreator/Update/UpcomingFeatures";
    public static float CurrentMainVersion = 4.1f;
    public static float CurrentBetaVersion = 1;
    public static float CurrentTestVersion = 3;
    public static String CurrentVersionType = "Stable";
    public static boolean isBetaUpdateAvailable = false;
    public static boolean isStableUpdateAvailable = false;
    public static boolean isTestUpdateAvailable = false;
    public static String betaDownloadLink = "";
    public static String stableDownloadLink = "";
    public static String testDownloadLink = "";

    public static String runUpdateCheck() throws URISyntaxException {
        if (Web.netIsAvailable()) {
            Logs.write("Running Update Check");
            switch (CurrentVersionType) {
                case "Stable":
                    if ((Control.forceStableUpdate || Preference.pp.checkUpdatesOnStartUp) && Update.isStableUpdateAvailable()) {
                        isStableUpdateAvailable = true;
                        return "Stable";
                    }
                    break;
                case "Beta":
                    if (Control.forceBetaUpdate) {
                        if (Update.isStableUpdateAvailable()) {
                            isStableUpdateAvailable = true;
                            return "Stable";
                        }
                        if (Update.isBetaUpdateAvailable()) {
                            isBetaUpdateAvailable = true;
                            return "Beta";
                        }
                    }
                    break;
                case "Test":
                    if (Control.forceTestUpdate) {
                        if (Update.isStableUpdateAvailable()) {
                            isStableUpdateAvailable = true;
                            return "Stable";
                        }
                        if (Update.isBetaUpdateAvailable()) {
                            isBetaUpdateAvailable = true;
                            return "Beta";
                        }
                        if (Update.isTestUpdateAvailable()) {
                            isTestUpdateAvailable = true;
                            return "Test";
                        }
                    }
                    break;
            }
        }
        return "";
    }

    public static void executeDownload() throws URISyntaxException {
        if (isStableUpdateAvailable) {
            String changelog = Update.getStableChangelog();
            Logs.write("Stable Changelog: " + changelog);
            String message = "A new update is available with following changelog\n" + changelog;
            int dialogResult = JOptionPane.showConfirmDialog(null, message, "Stable Update Changelog", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                Update.downloadStableVersion();
                System.exit(0);
            }
        } else if (isBetaUpdateAvailable) {
            String changelog = Update.getBetaChangelog();
            Logs.write("Beta Changelog: " + changelog);
            String message = "A new update is available with following changelog\n" + changelog;
            int dialogResult = JOptionPane.showConfirmDialog(null, message, "Beta Update Changelog", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                Update.downloadBetaVersion();
                System.exit(0);
            }
        } else if (isTestUpdateAvailable) {
            String changelog = Update.getTestChangelog();
            Logs.write("Test Changelog: " + changelog);
            String message = "A new update is available with following changelog\n" + changelog;
            int dialogResult = JOptionPane.showConfirmDialog(null, message, "Test Update Changelog", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                Update.downloadTestVersion();
                System.exit(0);
            }
        }
        Logs.write("Beta update status: " + Update.isBetaUpdateAvailable);
        Logs.write("Stable update status: " + Update.isStableUpdateAvailable);
        Logs.write("Test update status: " + Update.isTestUpdateAvailable);
    }

    public static void runTestUpdateCheck() {
        if (Web.netIsAvailable()) {
            if (Update.isTestUpdateAvailable()) {
                isTestUpdateAvailable = true;
            }
            Logs.write("Test update status: " + Update.isTestUpdateAvailable);
        }
    }

    public static void runBetaUpdateCheck() {
        if (Web.netIsAvailable()) {
            if (Update.isBetaUpdateAvailable()) {
                isBetaUpdateAvailable = true;
            }
            Logs.write("Beta update status: " + Update.isBetaUpdateAvailable);
        }
    }

    public static void runStableUpdateCheck() {
        if (Web.netIsAvailable()) {
            if (Update.isStableUpdateAvailable()) {
                isStableUpdateAvailable = true;
            }
            Logs.write("Stable update status: " + Update.isStableUpdateAvailable);
        }
    }

    public static void downloadBetaVersion() throws URISyntaxException {
        String downloadLink = Update.getBetaDownloadLink();
        Web.openWebpage(new URI(downloadLink));
    }

    public static void downloadStableVersion() throws URISyntaxException {
        String downloadLink = Update.getStableDownloadLink();
        Web.openWebpage(new URI(downloadLink));
    }

    public static void downloadTestVersion() throws URISyntaxException {
        String downloadLink = Update.getStableDownloadLink();
        Web.openWebpage(new URI(downloadLink));
    }

    public static String getBetaVersion() {
        return Web.getHtmlContent(RawLatestBetaVersion);
    }

    public static String getStableVersion() {
        return Web.getHtmlContent(RawLatestStableVersion);
    }

    public static String getTestVersion() {
        return Web.getHtmlContent(RawLatestTestVersion);
    }

    public static String getBetaDownloadLink() {
        return Web.getHtmlContent(RawDownloadLatestBetaVersion);
    }

    public static String getStableDownloadLink() {
        return Web.getHtmlContent(RawDownloadLatestStableVersion);
    }

    public static String getTestDownloadLink() {
        return Web.getHtmlContent(RawDownloadLatestTestVersion);
    }

    public static String getBetaChangelog() {
        return Web.getHtmlContent(RawChangeLogBeta);
    }

    public static String getStableChangelog() {
        return Web.getHtmlContent(RawChangeLogStable);
    }

    public static String getTestChangelog() {
        return Web.getHtmlContent(RawChangeLogTest);
    }

    public static String getUpcomingFeatures() {
        return Web.getHtmlContent(RawUpcomingFeatures);
    }

    public static boolean isBetaUpdateAvailable() {
        boolean updateStatus = false;
        try {
            String data = getBetaVersion();
            String[] version = data.split(" ");
            float mainVersion = Float.valueOf(version[0]);
            float betaVersion = Float.valueOf(version[1].substring(4, version[1].length()));
            if (CurrentBetaVersion == betaVersion && CurrentMainVersion == mainVersion) {
                return false;
            }
            if (CurrentMainVersion <= mainVersion) {
                if (CurrentBetaVersion < betaVersion) {
                    updateStatus = true;
                } else if (CurrentBetaVersion == betaVersion && CurrentMainVersion < mainVersion) {
                    updateStatus = true;
                } else if (CurrentMainVersion < mainVersion) {
                    updateStatus = true;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Take logs and Contact Developer! Couldn't check Beta update");
            Logs.write("While Checking Beta Update: " + Logs.getExceptionTrace(e));
        }
        return updateStatus;
    }

    public static boolean isStableUpdateAvailable() {
        boolean updateStatus = false;
        try {
            float stableVersion = Float.valueOf(getStableVersion());
            if (CurrentMainVersion < stableVersion) {
                updateStatus = true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Take logs and Contact Developer! Couldn't check Stable update");
            Logs.write("While Checking Stable Update: " + Logs.getExceptionTrace(e));
        }
        return updateStatus;
    }

    public static boolean isTestUpdateAvailable() {
        boolean updateStatus = false;
        try {
            String data = getTestVersion();
            String[] version = data.split(" ");
            float mainVersion = Float.valueOf(version[0]);
            float betaVersion = Float.valueOf(version[1].substring(4, version[1].length()));
            float testVersion = Float.valueOf(version[2].substring(4, version[2].length()));

            if (CurrentTestVersion == testVersion && CurrentMainVersion == mainVersion && CurrentBetaVersion == betaVersion) {
                return false;
            }
            if (CurrentMainVersion <= mainVersion && CurrentBetaVersion <= betaVersion) {
                if (CurrentMainVersion == mainVersion && CurrentBetaVersion < betaVersion) {
                    return true;
                }
                if (CurrentMainVersion < mainVersion && CurrentBetaVersion == betaVersion) {
                    return true;
                }
                if (CurrentMainVersion < mainVersion && CurrentBetaVersion < betaVersion) {
                    return true;
                }
                if (CurrentTestVersion < testVersion) {
                    return true;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Take logs and Contact Developer! Couldn't check Test update");
            Logs.write("While Checking Test Update: " + Logs.getExceptionTrace(e));
        }
        return updateStatus;
    }
}
