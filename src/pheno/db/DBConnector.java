/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pheno.db;

import java.sql.*;
import java.util.ResourceBundle;
/**
 *
 * @author CCS
 */
public class DBConnector {

    public static String driver, url, username, password;
    private Connection conn;

    public DBConnector(){
        ResourceBundle settings = ResourceBundle.getBundle("senty");
        driver = settings.getString("driver");
        url = settings.getString("url");
        username = settings.getString("username");
        password = settings.getString("password");

        conn = null;
    }

    public Connection OpenConnection()
    {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,username,password);

            //System.out.println("Connected to the database");

            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
            return null;
    }

    public void CloseConnection()
    {
        try {
            conn.close();
            System.out.println("Disconnected from database");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
