/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.AFZC;

import javax.swing.JOptionPane;

/**
 *
 * @author Nikhil
 */
public class Protocols {

    //this function will save the time and effort in writing long System.out.println function.
    
    public static <T> void show(T content){
        JOptionPane.showMessageDialog(null, content.toString());
    }
    
    public static void p(){
        System.out.println();
    }
    
    public static void p(int a){
        System.out.println(a);
    }
    
    public static void p(String print) {
        System.out.println(print);
    }
    
    public static void p(Object o){
        System.out.println(o);
    }

    public static void sopln(String print) {
        sop(print + "\n");
    }

    //this replaces simple print statement

    public static void sop(String print) {
        System.out.print(print);
    }
}
