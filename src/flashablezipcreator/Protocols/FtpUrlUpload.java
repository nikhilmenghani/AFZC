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
import flashablezipcreator.FlashableZipCreator;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class FtpUrlUpload {

    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) throws UnsupportedEncodingException, MalformedURLException {
        try {
//            executeFTP();

//            System.out.println(GetNetworkAddress.getVMWareMacAddress());
//            System.out.println(GetNetworkAddress.GetAddress("ip"));
            System.out.println(GetNetworkAddress.GetAddress("mac"));
        } catch (Exception ex) {

        }
    }

    public static void executeFTP() throws UnsupportedEncodingException, IOException {
        String uploadUserDataPath = "AFZC/UserData" + FlashableZipCreator.VERSION + ".txt";
        try {
            if (Control.forceFTP || FlashableZipCreator.useFTP) {
                writeUserData(uploadUserDataPath);
            }
        } catch (Exception e) {

        }
    }

    public static void writeCreateZipCount(String path) throws UnsupportedEncodingException, IOException {
        String string = "0";
        try {
            string = readStringFromFTP(path);
        } catch (IOException e) {
        }
        try {
            int i = 0;
            i = Integer.valueOf(string);
            i++;
            uploadStringToFTP(String.valueOf(i), path);
        } catch (IOException | NumberFormatException e) {
            uploadStringToFTP("1", path);
        }
    }

    public static void writeUserData(String path) throws UnsupportedEncodingException, IOException {
        String userData = "";
        String userName = System.getProperty("user.name");
        int totalZipCount = 0;
        String temp = "";
        String oldData = "";
        String newData = "";
        if (userName.equals("")) {
            userName = "LinuxUser";
        }
        try {
            userData = readStringFromFTP(path);
        } catch (Exception e) {
        }
        //userData = "TCZC::47\nHi:21\nNikhil1::1\nTest::2\nHello:3";
        try {
            if (userData.startsWith("TCZC::")) {
                temp = userData.substring(userData.indexOf("::") + 2, userData.indexOf("\n"));
                totalZipCount = Integer.valueOf(temp);
                oldData = "TCZC::" + totalZipCount;
                newData = "TCZC::" + (totalZipCount + 1);
                userData = userData.replaceAll(oldData, newData);
            } else {
                newData = "TCZC::1";
                userData = newData;
            }
            oldData = newData = "";
            if (userData.contains(userName + "::")) {
                temp = userData.substring(userData.indexOf(userName + "::"), userData.length());
                if (temp.contains("\n")) {
                    temp = temp.substring(0, temp.indexOf("\n"));
                }
                oldData = temp;
                int num = Integer.valueOf((temp.substring(temp.indexOf("::") + 2, temp.length())));
                temp = temp.substring(0, temp.length() - String.valueOf(num).length());
                num++;
                newData = temp + String.valueOf(num);
            } else {
                userData += "\n" + userName + "::1";
            }
            userData = userData.replaceAll(oldData, newData);
            uploadStringToFTP(userData, path);
        } catch (Exception e) {
        }
    }

    public static String readStringFromFTP(String filePath) throws MalformedURLException, UnsupportedEncodingException, IOException {
        URL ftpurl = getFTPUrl(filePath);
        URLConnection conn = ftpurl.openConnection();
        String str = "";
        try (InputStream inputStream = conn.getInputStream();) {
            Reader r = new InputStreamReader(inputStream);
            StringBuilder sb = new StringBuilder();
            char[] chars = new char[4 * 1024];
            int len;
            while ((len = r.read(chars)) >= 0) {
                sb.append(chars, 0, len);
            }
            str = sb.toString();
        }
        return str;
    }

    public static void uploadFileToFTP(String filePath, String uploadPath) throws MalformedURLException, UnsupportedEncodingException, IOException {
        URL ftpurl = getFTPUrl(uploadPath);
        URLConnection conn = ftpurl.openConnection();
        try (OutputStream outputStream = conn.getOutputStream(); FileInputStream inputStream = new FileInputStream(filePath);) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    public static void uploadStringToFTP(String str, String uploadPath) throws IOException {
        URL ftpurl = getFTPUrl(uploadPath);
        URLConnection conn = ftpurl.openConnection();
        try (OutputStream outputStream = conn.getOutputStream(); InputStream is = new ByteArrayInputStream(str.getBytes())) {
            byte[] buffer = new byte[BUFFER_SIZE];
            for (int len; (len = is.read(buffer)) != -1;) {
                outputStream.write(buffer, 0, len);
            }
        }
    }

    public static URL getFTPUrl(String uploadPath) throws MalformedURLException, UnsupportedEncodingException {
        String host = "menghani.com:21";
        String user = "nikhil@menghani.com";
        String pass = "Nikhil2017";
        URL ftpurl = new URL("ftp://"
                + URLEncoder.encode(user, "UTF-8") + ":"
                + URLEncoder.encode(pass, "UTF-8") + "@"
                + host + "/" + uploadPath + ";type=i");
//        System.out.println("Upload URL: " + ftpurl.toString());
        return ftpurl;
    }

}
