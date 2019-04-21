/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Gapps;

import flashablezipcreator.Adb.Package;
import flashablezipcreator.Protocols.Device;
import flashablezipcreator.Protocols.Logs;
import flashablezipcreator.Protocols.Project;
import flashablezipcreator.Protocols.Types;
import flashablezipcreator.UserInterface.Preference;
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
    public static Package GoogleAssistant = new Package();
    public static Package Calculator = new Package();
    public static Package Duo = new Package();
    public static Package DigitalWellbeing = new Package();
    public static Package GoogleCalendarSync = new Package();
    public static Package YouTube = new Package();
    public static Package GoogleCalendar = new Package();
    public static Package GoogleContacts = new Package();
    public static Package GoogleClock = new Package();
    public static Package GoogleBoard = new Package();
    public static Package GoogleWallpaper = new Package();
    public static Package GoogleDialer = new Package();
    public static Package GoogleMessages = new Package();
    public static Package GoogleARCore = new Package();
    public static Package GooglePlayground = new Package();
    public static Package GoogleSetupWizard = new Package();
    public static Package GooglePartnerSetup = new Package();
    public static Package GoogleOneTimeInitializer = new Package();
    public static Package GoogleRestore = new Package();
    public static Package GoogleFeedback = new Package();
    public static Package AndroidMigratePrebuilt = new Package();
    //Google calendar, Google clock, Google dailer, 
    //Google contact, google message, google calculator, 
    //google gboard, youtube vanced, pixel launcher and wallpaper
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
        Google.installedPath = "/system/priv-app/Velvet/Velvet.apk";
        Calculator.packageName = "com.google.android.calculator";
        Calculator.installedPath = "/system/app/Calculator/Calculator.apk";
//        Calculator.installedPath = "/system/app/CalculatorGooglePrebuilt/CalculatorGooglePrebuilt.apk";
        Duo.packageName = "com.google.android.apps.tachyon";
        Duo.installedPath = "/system/app/Duo/Duo.apk";
        DigitalWellbeing.packageName = "com.google.android.apps.wellbeing";
        DigitalWellbeing.installedPath = "/system/priv-app/WellbeingPrebuilt/WellbeingPrebuilt.apk";
        DigitalWellbeing.isOptional = true;
        GoogleCalendarSync.packageName = "com.google.android.syncadapters.calendar";
        GoogleCalendarSync.installedPath = "/system/app/GoogleCalendarSyncAdapter/GoogleCalendarSyncAdapter.apk";
        GoogleAssistant.packageName = "com.google.android.apps.googleassistant";
        GoogleAssistant.installedPath = "/system/priv-app/Assistant/Assistant.apk";
        GoogleAssistant.isOptional = true;
        YouTube.packageName = "com.google.android.youtube";
        YouTube.packagePath = "/system/app/YouTube/YouTube.apk";
        GoogleCalendar.packageName = "com.google.android.calendar";
        GoogleCalendar.installedPath = "/system/priv-app/CalendarGooglePrebuilt/CalendarGooglePrebuilt.apk";
        GoogleContacts.packageName = "com.google.android.contacts";
        GoogleContacts.installedPath = "/system/priv-app/Contacts/Contacts.apk";
        GoogleClock.packageName = "com.google.android.deskclock";
        GoogleClock.installedPath = "/system/app/DeskClock/DeskClock.apk";
        GoogleBoard.packageName = "com.google.android.inputmethod.latin";
        GoogleBoard.installedPath = "/system/app/GBoard/GBoard.apk";
        GoogleWallpaper.packageName = "com.google.android.apps.wallpaper";
        GoogleWallpaper.installedPath = "/system/priv-app/WallpaperPickerGooglePrebuilt/WallpaperPickerGooglePrebuilt.apk";
        GoogleDialer.packageName = "com.google.android.dialer";
        GoogleDialer.installedPath = "/system/priv-app/Phone/Phone.apk";
        GoogleMessages.packageName = "com.google.android.apps.messaging";
        GoogleMessages.installedPath = "/system/app/Messaging/Messaging.apk";
        GoogleARCore.packageName = "com.google.ar.core";
        GoogleARCore.installedPath = "/system/app/ARCore/ARCore.apk";
        GooglePlayground.packageName = "com.google.vr.apps.ornament";
        GooglePlayground.installedPath = "/system/app/PlaygroundMod/PlaygroundMod.apk";
        GoogleSetupWizard.packageName = "com.google.android.setupwizard";
        GoogleSetupWizard.installedPath = "/system/priv-app/SetupWizard/SetupWizard.apk";
        GooglePartnerSetup.packageName = "com.google.android.partnersetup";
        GooglePartnerSetup.installedPath = "/system/priv-app/GooglePartnerSetup/GooglePartnerSetup.apk";
        GoogleOneTimeInitializer.packageName = "com.google.android.onetimeinitializer";
        GoogleOneTimeInitializer.installedPath = "/system/priv-app/GoogleOneTimeInitializer/GoogleOneTimeInitializer.apk";
        GoogleRestore.packageName = "com.google.android.apps.restore";
        GoogleRestore.installedPath = "/system/priv-app/GoogleRestore/GoogleRestore.apk";
        GoogleFeedback.packageName = "com.google.android.feedback";
        GoogleFeedback.installedPath = "/system/priv-app/GoogleFeedback/GoogleFeedback.apk";
        AndroidMigratePrebuilt.packageName = "com.google.android.apps.pixelmigrate";
        AndroidMigratePrebuilt.installedPath = "/system/priv-app/AndroidMigratePrebuilt/AndroidMigratePrebuilt.apk";
        files.packageName = "";
        files.associatedFileList = new ArrayList<>();
        files.associatedFileList.add("/system/etc/default-permissions/default-permissions.xml");
        files.associatedFileList.add("/systen/etc/permissions/privapp-permissions-google.xml");
        files.associatedFileList.add("/system/etc/sysconfig/google-hiddenapi-package-whitelist.xml");
        files.associatedFileList.add("/system/etc/sysconfig/google.xml");

        //following 5 not needed
//        files.associatedFileList.add("/system/etc/permissions/com.google.android.dialer.support.xml");
//        files.associatedFileList.add("/system/etc/permissions/com.google.android.media.effects.xml");
//        files.associatedFileList.add("/system/etc/sysconfig/dialer_experience.xml");
//        files.associatedFileList.add("/system/etc/sysconfig/google_build.xml");
//        files.associatedFileList.add("/system/etc/g.prop");
//        files.associatedFileList.add("/system/framework/com.google.android.dialer.support.jar");
        //files.associatedFileList.add("/system/framework/com.google.android.maps.jar"); //corrupted for some reason
//        files.associatedFileList.add("/system/framework/com.google.android.media.effects.jar");
//        files.associatedFileList.add("/system/framework/com.google.widevine.software.drm.jar");
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
        list.add(Gapps.GoogleCalendarSync);
        return list;
    }

    public static ArrayList<Package> getBasicList() {
        ArrayList<Package> list = new ArrayList<>();
        for (Package p : getCoreList()) {
            list.add(p);
        }
        list.add(Gapps.GoogleBackupTransport);
        list.add(Gapps.FaceLock);
        list.add(Gapps.YouTube);
        list.add(Gapps.GoogleCamera);
        if ((Device.checkDeviceConnectivity(Preference.pp.connectIp) == 1) && Project.androidVersion.startsWith("9")) {
            list.add(DigitalWellbeing);
        }
        list.add(Gapps.GoogleExtServices);
        list.add(Gapps.GoogleExtShared);
        return list;
    }

    public static ArrayList<Package> getNanoList() {
        ArrayList<Package> list = new ArrayList<>();
        for (Package p : getBasicList()) {
            list.add(p);
        }
        list.add(Gapps.GoogleKeep);
        list.add(Gapps.GoogleMaps);
        return list;
    }

    public static ArrayList<Package> getMyList() {
        ArrayList<Package> list = new ArrayList<>();
        for (Package p : getBasicList()) {
            list.add(p);
        }
        list.add(Gapps.GoogleKeep);
        list.add(Gapps.GoogleMaps);
        list.add(Gapps.Gmail);
        list.add(Gapps.GoogleDrive);
        list.add(Gapps.GooglePhotos);
        list.add(Gapps.GooglePlayBooks);
        list.add(Gapps.Google);
        list.add(Gapps.Duo);
        list.add(Gapps.GoogleAssistant);
        return list;
    }

    public static ArrayList<Package> getStockList() {
        ArrayList<Package> list = new ArrayList<>();
        for (Package p : getBasicList()) {
            list.add(p);
        }
        list.add(Gapps.Calculator);
        list.add(Gapps.GoogleBoard);
        list.add(Gapps.GoogleContacts);
        list.add(Gapps.GoogleClock);
        list.add(Gapps.GoogleDialer);
        list.add(Gapps.GoogleClock);
        list.add(Gapps.GoogleWallpaper);
        list.add(Gapps.GoogleCalendar);
        list.add(Gapps.GoogleMessages);
        list.add(Gapps.GoogleARCore);
        list.add(Gapps.GooglePlayground);
        list.add(Gapps.GoogleAssistant);
        list.add(Gapps.Google);
        list.add(Gapps.GmsCoreSetupPrebuilt);
        list.add(Gapps.GoogleFeedback);
        list.add(Gapps.GooglePartnerSetup);
        list.add(Gapps.GoogleServicesFramework);
        list.add(Gapps.GoogleSetupWizard);
        list.add(Gapps.files);
        return list;
    }

    public static ArrayList<Package> getPackage(int type) {
        switch (type) {
            case Types.GAPPS_CORE:
                Logs.write("Importing Core List!");
                return Gapps.getCoreList();
            case Types.GAPPS_BASIC:
                Logs.write("Importing Basic List!");
                return Gapps.getBasicList();
            case Types.GAPPS_NANO:
                Logs.write("Importing Nano List!");
                return MicroG.getMicroGList();
//                return Gapps.getNanoList();
            case Types.GAPPS_MY:
                Logs.write("Importing My List!");
                return Gapps.getMyList();
            case Types.GAPPS_STOCK:
                Logs.write("Importing Stock List!");
                return Gapps.getStockList();
            case Types.GAPPS_MICROG:
                Logs.write("Importing MicroG List");
                return MicroG.getMicroGList();
        }
        return null;
    }
}
