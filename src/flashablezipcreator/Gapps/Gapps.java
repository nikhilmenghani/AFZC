/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Gapps;

import flashablezipcreator.Adb.Package;
import flashablezipcreator.Protocols.Project;
import java.util.ArrayList;

/**
 *
 * @author NBCTNBM
 */
public class Gapps {

    public static Package FaceLock = new Package();
    public static Package GoogleContactsSyncAdapter = new Package();
    public static Package GoogleExtShared = new Package();
    public static Package GmsCoreSetupPrebuilt = new Package();
    public static Package GoogleBackupTransport = new Package();
    public static Package GoogleExtServices = new Package();
    public static Package GoogleServicesFramework = new Package();
    public static Package Phonesky = new Package();
    public static Package PrebuiltGmsCore = new Package();
    public static Package GoogleKeep = new Package();
    public static Package GoogleMaps = new Package();
    public static Package GooglePhotos = new Package();
    public static Package Gmail = new Package();
    public static Package GoogleDrive = new Package();
    public static Package GooglePlayBooks = new Package();
    public static Package GoogleCamera = new Package();
    public static Package Google = new Package();
    public static Package Calculator = new Package();
    public static Package Duo = new Package();
    public static Package DigitalWellbeing = new Package();
    public static Package files = new Package();

    public Gapps() {
        FaceLock.packageName = "com.android.facelock";
        FaceLock.associatedFileList.add("/system/app/FaceLock/lib/arm64/libfacenet.so");
        FaceLock.installedPath = "/system/app/FaceLock/FaceLock.apk";
        FaceLock.isOptional = true;
        GoogleContactsSyncAdapter.packageName = "com.google.android.syncadapters.contacts";
        GoogleContactsSyncAdapter.installedPath = "/system/app/GoogleContactsSyncAdapter/GoogleContactsSyncAdapter.apk";
        GoogleExtShared.packageName = "com.google.android.ext.shared";
        GoogleExtShared.installedPath = "/system/app/GoogleExtShared/GoogleExtShared.apk";
        GmsCoreSetupPrebuilt.packageName = "com.google.android.gms.setup";
        GmsCoreSetupPrebuilt.installedPath = "/system/priv-app/GmsCoreSetupPrebuilt/GmsCoreSetupPrebuilt.apk";
        GoogleBackupTransport.packageName = "com.google.android.backuptransport";
        GoogleBackupTransport.installedPath = "/system/priv-app/GoogleBackupTransport/GoogleBackupTransport.apk";
        GoogleExtServices.packageName = "com.google.android.ext.services";
        GoogleExtServices.installedPath = "/system/priv-app/GoogleExtServices/GoogleExtServices.apk";
        GoogleServicesFramework.packageName = "com.google.android.gsf";
        GoogleServicesFramework.installedPath = "/system/priv-app/GoogleServicesFramework/GoogleServicesFramework.apk";
        Phonesky.packageName = "com.android.vending";
        Phonesky.installedPath = "/system/priv-app/Phonesky/Phonesky.apk";
        PrebuiltGmsCore.packageName = "com.google.android.gms";
        PrebuiltGmsCore.installedPath = "/system/priv-app/PrebuiltGmsCore/PrebuiltGmsCore.apk";
        GoogleKeep.packageName = "com.google.android.keep";
        GoogleKeep.installedPath = "/system/priv-app/PrebuiltKeep/PrebuiltKeep.apk";
        GoogleMaps.packageName = "com.google.android.apps.maps";
        GoogleMaps.installedPath = "/system/priv-app/Maps/Maps.apk";
        //GoogleMaps.associatedFileList.add("/system/etc/permissions/com.google.android.maps.xml");
        GooglePhotos.packageName = "com.google.android.apps.photos";
        GooglePhotos.installedPath = "/system/app/Photos/Photos.apk";
        Gmail.packageName = "com.google.android.gm";
        Gmail.installedPath = "/system/app/PrebuiltGmail/PrebuiltGmail.apk";
        GoogleDrive.packageName = "com.google.android.apps.docs";
        GoogleDrive.installedPath = "/system/app/Drive/Drive.apk";
        GooglePlayBooks.packageName = "com.google.android.apps.books";
        GooglePlayBooks.installedPath = "/system/app/Books/Books.apk";
        GoogleCamera.packageName = "com.google.android.GoogleCamera";
        GoogleCamera.installedPath = "/system/app/GoogleCamera/GoogleCamera.apk";
        Google.packageName = "com.google.android.googlequicksearchbox";
        Google.installedPath = "/system/app/Google/Google";
        Calculator.packageName = "com.google.android.calculator";
        Calculator.installedPath = "/system/app/CalculatorGooglePrebuilt/CalculatorGooglePrebuilt.apk";
        Duo.packageName = "com.google.android.apps.tachyon";
        Duo.installedPath = "/system/app/Duo/Duo.apk";
        DigitalWellbeing.packageName = "com.google.android.apps.wellbeing";
        DigitalWellbeing.installedPath = "/system/priv-app/WellbeingPrebuilt/WellbeingPrebuilt.apk";
        DigitalWellbeing.isOptional = true;
        //following 5 not needed
//        files.associatedFileList.add("/system/etc/permissions/com.google.android.dialer.support.xml");
//        files.associatedFileList.add("/system/etc/permissions/com.google.android.media.effects.xml");
//        files.associatedFileList.add("/system/etc/sysconfig/dialer_experience.xml");
//        files.associatedFileList.add("/system/etc/sysconfig/google_build.xml");
//        files.associatedFileList.add("/system/etc/g.prop");

        files.associatedFileList.add("/system/framework/com.google.android.dialer.support.jar");
        //files.associatedFileList.add("/system/framework/com.google.android.maps.jar"); //corrupted for some reason
        files.associatedFileList.add("/system/framework/com.google.android.media.effects.jar");
        files.associatedFileList.add("/system/framework/com.google.widevine.software.drm.jar");
//        files.associatedFileList.add("/system/etc/sysconfig/whitelist_com.android.omadm.service.xml");
//        files.associatedFileList.add("/system/etc/permissions/com.google.widevine.software.xml");
//        files.associatedFileList.add("/system/lib/libfilterpack_facedetect.so");
//        files.associatedFileList.add("/system/lib/libfrsdk.so");
//        files.associatedFileList.add("/system/lib64/libfacenet.so");
//        files.associatedFileList.add("/system/lib64/libfilterpack_facedetect.so");
//        files.associatedFileList.add("/system/lib64/libfrsdk.so");
//        files.associatedFileList.add("/vendor/lib/libfrsdk.so");
//        files.associatedFileList.add("/vendor/lib64/libfrsdk.so");
        files.isOptional = true;
    }

    public static ArrayList<Package> getCoreList() {
        Gapps gapps = new Gapps();
        ArrayList<Package> list = new ArrayList<>();
        list.add(Gapps.Phonesky);
        list.add(Gapps.PrebuiltGmsCore);
        list.add(Gapps.GoogleServicesFramework);
        list.add(Gapps.GoogleContactsSyncAdapter);
        return list;
    }

    public static ArrayList<Package> getPicoList() {
        ArrayList<Package> list = new ArrayList<>();
        for (Package p : getCoreList()) {
            list.add(p);
        }
        list.add(Gapps.GoogleBackupTransport);
        list.add(Gapps.GoogleExtServices);
        list.add(Gapps.GoogleExtShared);
        return list;
    }

    public static ArrayList<Package> getNanoList() {
        ArrayList<Package> list = new ArrayList<>();
        for (Package p : getPicoList()) {
            list.add(p);
        }
        list.add(Gapps.GoogleKeep);
        list.add(Gapps.GoogleMaps);
        return list;
    }

    public static ArrayList<Package> getMyList() {
        ArrayList<Package> list = new ArrayList<>();
        for (Package p : getPicoList()) {
            list.add(p);
        }
        list.add(Gapps.FaceLock);
        //following is not needed
        //list.add(Gapps.GmsCoreSetupPrebuilt);
        list.add(Gapps.GoogleKeep);
        list.add(Gapps.GoogleMaps);
        list.add(Gapps.Gmail);
        list.add(Gapps.GoogleDrive);
        list.add(Gapps.GooglePhotos);
        list.add(Gapps.GooglePlayBooks);
        list.add(Gapps.GoogleCamera);
        list.add(Gapps.Calculator);
        list.add(Gapps.Google);
        list.add(Gapps.Duo);
        if (Project.androidVersion.startsWith("9")) {
            list.add(DigitalWellbeing);
        }
        return list;
    }
}
