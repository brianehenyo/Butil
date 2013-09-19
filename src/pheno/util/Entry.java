/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pheno.util;

import java.util.Date;

/**
 *
 * @author admin
 */
public class Entry {
    
    private int entry_id;
    private int row_id;
    private int picture_id;
    private String date;
    private String dev_stage;
    private String d_greeness;
    private String d_height;
    private String d_volume;
    private String d_leaves;
    private String d_tillers;
    private String d_vphenotype;

    /**
     * @return the entry_id
     */
    public int getEntry_id() {
        return entry_id;
    }

    /**
     * @param entry_id the entry_id to set
     */
    public void setEntry_id(int entry_id) {
        this.entry_id = entry_id;
    }

    /**
     * @return the plant_id
     */
    public int getRow_id() {
        return row_id;
    }

    /**
     * @param plant_id the plant_id to set
     */
    public void setRow_id(int row_id) {
        this.row_id = row_id;
    }

    /**
     * @return the picture_id
     */
    public int getPicture_id() {
        return picture_id;
    }

    /**
     * @param picture_id the picture_id to set
     */
    public void setPicture_id(int picture_id) {
        this.picture_id = picture_id;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the dev_stage
     */
    public String getDev_stage() {
        return dev_stage;
    }

    /**
     * @param dev_stage the dev_stage to set
     */
    public void setDev_stage(String dev_stage) {
        this.dev_stage = dev_stage;
    }

    /**
     * @return the d_greeness
     */
    public String getD_greeness() {
        return d_greeness;
    }

    /**
     * @param d_greeness the d_greeness to set
     */
    public void setD_greeness(String d_greeness) {
        this.d_greeness = d_greeness;
    }

    /**
     * @return the d_height
     */
    public String getD_height() {
        return d_height;
    }

    /**
     * @param d_height the d_height to set
     */
    public void setD_height(String d_height) {
        this.d_height = d_height;
    }

    /**
     * @return the d_volume
     */
    public String getD_volume() {
        return d_volume;
    }

    /**
     * @param d_volume the d_volume to set
     */
    public void setD_volume(String d_volume) {
        this.d_volume = d_volume;
    }

    /**
     * @return the d_leaves
     */
    public String getD_leaves() {
        return d_leaves;
    }

    /**
     * @param d_leaves the d_leaves to set
     */
    public void setD_leaves(String d_leaves) {
        this.d_leaves = d_leaves;
    }

    /**
     * @return the d_tillers
     */
    public String getD_tillers() {
        return d_tillers;
    }

    /**
     * @param d_tillers the d_tillers to set
     */
    public void setD_tillers(String d_tillers) {
        this.d_tillers = d_tillers;
    }

    /**
     * @return the d_vphenotype
     */
    public String getD_vphenotype() {
        return d_vphenotype;
    }

    /**
     * @param d_vphenotype the d_vphenotype to set
     */
    public void setD_vphenotype(String d_vphenotype) {
        this.d_vphenotype = d_vphenotype;
    }
          
    
}
