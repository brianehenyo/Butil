/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pheno.dao;

import pheno.db.DBConnector;
import pheno.util.Entry;
import pheno.util.Plant;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author admin
 */
public class RowManager {
    
    public ArrayList<Entry> GetPlantRow(int row_id)
    {
        ArrayList<Entry> entries = new ArrayList<Entry>();

        try{
            Connection con = new DBConnector().OpenConnection();

            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM entry where row_id=? order by entry_id ");

            pstmt.setInt(1, row_id);

            ResultSet rs= pstmt.executeQuery();

            while(rs.next()){
               Entry p = new Entry();
               p.setRow_id(row_id);
               p.setDate(rs.getString("date"));
               p.setDev_stage(rs.getString("dev_stage"));
               p.setD_greeness(rs.getString("d_greeness"));
               p.setD_height(rs.getString("d_height"));
               p.setD_volume(rs.getString("d_volume"));
               
               entries.add(p);

            }
            con.close();

      }
      catch (SQLException s){
            System.out.println("SQL statement is not executed!");
            s.printStackTrace();
      }

      return entries;
    }    
    
    public void addEntry(Entry e)
    {
       
        try{
            Connection con = new DBConnector().OpenConnection();

            PreparedStatement pstmt = con.prepareStatement("INSERT INTO entry(row_id, picture_id, date, dev_stage, d_greeness, d_volume, d_vphenotype) VALUES (?,?,?,?,?,?,?) ");

            pstmt.setInt(1, e.getPicture_id());
            pstmt.setInt(2, e.getRow_id());
            Calendar c = new GregorianCalendar();
            pstmt.setDate(3, new Date(c.getTimeInMillis()));
            pstmt.setString(4, "2");
            pstmt.setString(5, e.getD_greeness());
            pstmt.setString(6, e.getD_volume());
            pstmt.setString(7, e.getD_vphenotype());

            pstmt.executeUpdate();

            con.close();

    
      }
      catch (SQLException s){
            System.out.println("SQL statement is not executed!");
           s.printStackTrace();
      }
    }
}
