/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pheno.dao;

import pheno.db.DBConnector;
import pheno.util.Plant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class PlantManager {
    
    public ArrayList<Plant> GetPlantsBasedOnROw(int row_id)
    {
        ArrayList<Plant> plants = new ArrayList<Plant>();

        try{
            Connection con = new DBConnector().OpenConnection();

            PreparedStatement pstmt = con.prepareStatement("SELECT * from plant WHERE row_id = ? ");

            pstmt.setInt(1, row_id);

            ResultSet rs= pstmt.executeQuery();

            while(rs.next()){
               Plant p = new Plant();
               p.setPlant_id(rs.getInt("plant_id"));
               p.setRow_id(row_id);
               p.setCurrent_stage(rs.getInt("current_stage"));
               p.setPosition(rs.getString("position"));
               
               plants.add(p);

            }
            con.close();

      }
      catch (SQLException s){
            System.out.println("SQL statement is not executed!");
            s.printStackTrace();
      }

      return plants;
    }
    
    
    public boolean CheckPlantPosition(String position, ArrayList<Plant> plants){
        
        boolean exist = false;
        for(int i=0; i<plants.size(); i++){
            
            Plant p = plants.get(i); 
            
            if(position.equalsIgnoreCase(p.getPosition())){
                exist=true;
                
            }
            
        }
         
        return exist;
    }
    
    
    public int GetPlantStage(String position, ArrayList<Plant> plants){
        
        int stage = 0;
        for(int i=0; i<plants.size(); i++){
            
            Plant p = plants.get(i); 
            
            if(position.equalsIgnoreCase(p.getPosition())){
                stage = p.getCurrent_stage();
                
            }
            
        }
         
        return stage;
    }
    
  
    public void AddNewPlant(Plant p)
    {
       
        try{
            Connection con = new DBConnector().OpenConnection();

            PreparedStatement pstmt = con.prepareStatement("INSERT INTO plant(current_stage, row_id, abbreviated_name, position) VALUES (?,?,?,?) ");

            pstmt.setInt(1, p.getCurrent_stage());
            pstmt.setInt(2, p.getRow_id());
            pstmt.setString(3, p.getAbbreviated_name());
            pstmt.setString(4, p.getPosition());

            pstmt.executeUpdate();

            con.close();

    
      }
      catch (SQLException s){
            System.out.println("SQL statement is not executed!");
           s.printStackTrace();
      }
    }
    
    
}
