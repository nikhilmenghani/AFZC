/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 *
 * @author Nikhil
 */
public class Update {

    public static String RawLatestBetaVersion = "https://raw.githubusercontent.com/nikhilmenghani/FlashableZipCreator/master/src/flashablezipcreator/Update/LatestBetaVersion";
    public static String RawLatestStableVersion = "https://raw.githubusercontent.com/nikhilmenghani/FlashableZipCreator/master/src/flashablezipcreator/Update/LatestStableVersion";
    public static String RawDownloadLatestBetaVersion = "https://raw.githubusercontent.com/nikhilmenghani/FlashableZipCreator/master/src/flashablezipcreator/Update/DownloadLatestBetaVersion";
    public static String RawDownloadLatestStableVersion = "https://raw.githubusercontent.com/nikhilmenghani/FlashableZipCreator/master/src/flashablezipcreator/Update/DownloadLatestStableVersion";
    public static String RawChangeLogBeta = "https://raw.githubusercontent.com/nikhilmenghani/FlashableZipCreator/master/src/flashablezipcreator/Update/ChangeLogBeta";
    public static String RawChangeLogStable = "https://raw.githubusercontent.com/nikhilmenghani/FlashableZipCreator/master/src/flashablezipcreator/Update/ChangeLogStable";
    public static String RawUpcomingFeatures = "https://raw.githubusercontent.com/nikhilmenghani/FlashableZipCreator/master/src/flashablezipcreator/Update/UpcomingFeatures";
    public static float CurrentStableVersion = 3.0F;
    public static float CurrentBetaVersion = 3.9F;
    public static boolean isBetaUpdateAvailable = false;
    public static boolean isStableUpdateAvailable = false;
    public static String betaDownloadLink = "";
    public static String stableDownloadLink = "";

    public static void runUpdateCheck(){
        if (Update.netIsAvailable()) {
            Logs.write("Beta update status: " + Update.isBetaUpdateAvailable());
            Logs.write("Stable update status: " + Update.isStableUpdateAvailable());
            if (Update.isStableUpdateAvailable()) {
                isBetaUpdateAvailable = true;
            }
            if (Update.isBetaUpdateAvailable()) {
                isStableUpdateAvailable = true;
            }
        }
    }
    
    public static void downloadBetaVersion() throws URISyntaxException{
        String downloadLink = Update.getBetaDownloadLink();
        Update.openWebpage(new URI(downloadLink));
    }
    
    public static void downloadStableVersion() throws URISyntaxException{
        String downloadLink = Update.getStableDownloadLink();
        Update.openWebpage(new URI(downloadLink));
    }
    
    public static String getHtmlContent(String url) {
        String content = "";
        URLConnection connection = null;
        try {
            connection = new URL(url).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content;
    }

    public static String getBetaVersion() {
        return getHtmlContent(RawLatestBetaVersion);
    }

    public static String getStableVersion() {
        return getHtmlContent(RawLatestStableVersion);
    }

    public static String getBetaDownloadLink() {
        return getHtmlContent(RawDownloadLatestBetaVersion);
    }

    public static String getStableDownloadLink() {
        return getHtmlContent(RawDownloadLatestStableVersion);
    }
    
    public static String getBetaChangelog(){
        return getHtmlContent(RawChangeLogBeta);
    }
    
    public static String getStableChangelog(){
        return getHtmlContent(RawChangeLogStable);
    }
    
    public static String getUpcomingFeatures(){
        return getHtmlContent(RawUpcomingFeatures);
    }

    public static boolean isBetaUpdateAvailable() {
        boolean updateStatus = false;
        float betaVersion = Float.valueOf(getBetaVersion());
        if (CurrentBetaVersion < betaVersion) {
            updateStatus = true;
        }
        return updateStatus;
    }

    public static boolean isStableUpdateAvailable() {
        boolean updateStatus = false;
        float stableVersion = Float.valueOf(getStableVersion());
        if (CurrentStableVersion < stableVersion) {
            updateStatus = true;
        }
        return updateStatus;
    }

    public static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openWebpage(URL url) {
        try {
            openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }
}
