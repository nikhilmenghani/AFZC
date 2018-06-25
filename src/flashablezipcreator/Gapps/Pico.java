/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Gapps;

import java.util.ArrayList;
import flashablezipcreator.Adb.Package;

/**
 *
 * @author NBCTNBM
 */
public class Pico {

    public static ArrayList<Package> getList() {
        Gapps gapps = new Gapps();
        ArrayList<Package> list = new ArrayList<>();
        list.add(Gapps.FaceLock);
        list.add(Gapps.GmsCoreSetupPrebuilt);
        list.add(Gapps.GoogleBackupTransport);
        list.add(Gapps.GoogleContactsSyncAdapter);
        list.add(Gapps.GoogleExtServices);
        list.add(Gapps.GoogleExtShared);
        list.add(Gapps.GoogleServicesFramework);
        list.add(Gapps.Phonesky);
        list.add(Gapps.PrebuiltGmsCore);
        list.add(Gapps.files);
        return list;
    }
}
