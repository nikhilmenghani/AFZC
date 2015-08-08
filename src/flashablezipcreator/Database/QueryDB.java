/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package flashablezipcreator.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Nikhil
 */
public class QueryDB extends CreateDB {
    /*this constructor is used to create afzc database everytime tool is started
    it will be reduced to one time creation of database if managable.*/
    public QueryDB(String DbName) throws ClassNotFoundException, SQLException{
        super(DbName);
    }
    //this function is used to create only table. Different tables for handling Rom, Gapps might be required later.
    public void createTable(String tableQry) throws SQLException{
        Connection con = DriverManager.getConnection(JDBC_URL);
        con.createStatement().execute(tableQry);
        con.close();
    }
    //this function takes in comma seperated values and inserts them in given table.
    public void insert(String table, String values) throws SQLException{
        Connection con = DriverManager.getConnection(JDBC_URL);
        String qry = "insert into " + table + 
                " values" + "(" + values + ")";
        con.createStatement().executeUpdate(qry);
        sopln("inserted");
        con.close();
    }
    //this function updates given table with given set of values (csv) based on given condition.
    public void update(String table, String values, String condition) throws SQLException{
        Connection con = DriverManager.getConnection(JDBC_URL);
        String qry = "update " + table + 
                " set " + values +
                " where " + condition + "";
        con.createStatement().executeUpdate(qry);
        sopln("updated");
        con.close();
    }
    //this function deletes entry(s) from a given table satifying given condition.
    public void delete(String table, String condition) throws SQLException{
        Connection con = DriverManager.getConnection(JDBC_URL);
        String qry = "delete from " + table + 
                " where " + condition + "";
        con.createStatement().executeUpdate(qry);
        sopln("deleted");
        con.close();
    }
    //this function returns set of record satistying given condition.
    public ResultSet select(String columns, String table, String condition) throws SQLException{
        Connection con = DriverManager.getConnection(JDBC_URL);
        String qry = "select " + columns + 
                " from " + table +
                " where " + condition + "";
        return con.createStatement().executeQuery(qry);
    }
    //this function can be used when no condition filtering is required.
    public ResultSet select(String columns, String table) throws SQLException{
        Connection con = DriverManager.getConnection(JDBC_URL);
        String qry = "select " + columns + 
                " from " + table + "";
        return con.createStatement().executeQuery(qry);
    }
}
