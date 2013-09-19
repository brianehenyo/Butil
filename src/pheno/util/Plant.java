/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pheno.util;

/**
 *
 * @author admin
 */
public class Plant {
    
    private int plant_id;
    private int row_id;
    private int irri_id;
    private String abbreviated_name;
    private int current_stage; //1:4-5, 2:midtillering, 3:
    private String position;

    /**
     * @return the plant_id
     */
    public int getPlant_id() {
        return plant_id;
    }

    /**
     * @param plant_id the plant_id to set
     */
    public void setPlant_id(int plant_id) {
        this.plant_id = plant_id;
    }

    /**
     * @return the row_id
     */
    public int getRow_id() {
        return row_id;
    }

    /**
     * @param row_id the row_id to set
     */
    public void setRow_id(int row_id) {
        this.row_id = row_id;
    }

    /**
     * @return the irri_id
     */
    public int getIrri_id() {
        return irri_id;
    }

    /**
     * @param irri_id the irri_id to set
     */
    public void setIrri_id(int irri_id) {
        this.irri_id = irri_id;
    }

    /**
     * @return the abbreviated_name
     */
    public String getAbbreviated_name() {
        return abbreviated_name;
    }

    /**
     * @param abbreviated_name the abbreviated_name to set
     */
    public void setAbbreviated_name(String abbreviated_name) {
        this.abbreviated_name = abbreviated_name;
    }

    /**
     * @return the current_status
     */
    public int getCurrent_stage() {
        return current_stage;
    }

    /**
     * @param current_status the current_status to set
     */
    public void setCurrent_stage(int current_stage) {
        this.current_stage = current_stage;
    }

    /**
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }
    
}
