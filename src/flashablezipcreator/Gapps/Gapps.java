/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Gapps;

import flashablezipcreator.Adb.Package;

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
    public static Package files = new Package();

    public Gapps() {
        FaceLock.packageName = "com.android.facelock";
        FaceLock.associatedFileList.add("/system/app/FaceLock/lib/arm64/libfacenet.so");
        FaceLock.installedPath = "/system/app/FaceLock/FaceLock.apk";
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
        files.associatedFileList.add("/system/etc/permissions/com.google.android.dialer.support.xml");
        files.associatedFileList.add("/system/etc/permissions/com.google.android.maps.xml");
        files.associatedFileList.add("/system/etc/permissions/com.google.android.media.effects.xml");
        files.associatedFileList.add("/system/etc/permissions/com.google.widevine.software.xml");
        files.associatedFileList.add("/system/etc/sysconfig/dialer_experience.xml");
        files.associatedFileList.add("/system/etc/sysconfig/google_build.xml");
        files.associatedFileList.add("/system/etc/sysconfig/whitelist_com.android.omadm.service.xml");
        files.associatedFileList.add("/system/etc/g.prop");
        files.associatedFileList.add("/system/framework/com.google.android.dialer.support.jar");
        files.associatedFileList.add("/system/framework/com.google.android.maps.jar");
        files.associatedFileList.add("/system/framework/com.google.android.media.effects.jar");
        files.associatedFileList.add("/system/framework/com.google.widevine.software.drm.jar");
        files.associatedFileList.add("/system/lib/libfilterpack_facedetect.so");
        files.associatedFileList.add("/system/lib/libfrsdk.so");
        files.associatedFileList.add("/system/lib64/libfacenet.so");
        files.associatedFileList.add("/system/lib64/libfilterpack_facedetect.so");
        files.associatedFileList.add("/system/lib64/libfrsdk.so");
        files.associatedFileList.add("/vendor/lib/libfrsdk.so");
        files.associatedFileList.add("/vendor/lib64/libfrsdk.so");
    }
}
