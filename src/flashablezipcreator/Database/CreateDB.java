/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package flashablezipcreator.Database;

import java.sql.SQLException;
import flashablezipcreator.AFZC.Protocols;

/**
 *
 * @author Nikhil
 */
public class CreateDB extends Protocols{
    public final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    public String JDBC_URL;
    //this constructor creates a database
    CreateDB(String DB) throws ClassNotFoundException, SQLException{
        Class.forName(DRIVER);
        JDBC_URL = "jdbc:derby:" + DB + ";create=true";
    }
}
