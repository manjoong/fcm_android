package com.example.jeonse_alarm;

public class SelectedStationConstructRenew {

    private String stn_name;
    private String stn_id;
    private Boolean opst_check;
    private Boolean apt_check;
    private Boolean or_check;


    public String getStnName() {
        return stn_name;
    }
    public String getStn_id() {
        return stn_id;
    }
    public Boolean getOpst_check() {
        return opst_check;
    }
    public Boolean getApt_check() {
        return apt_check;
    }
    public Boolean getOr_check() {
        return or_check;
    }


    public void setStnName(String stn_name) {
        this.stn_name = stn_name;
    }
    public void setStn_id(String stn_id) {
        this.stn_id = stn_id;
    }
    public void setOpst_check(Boolean opst_check) {
        this.opst_check = opst_check;
    }
    public void setApt_check(Boolean apt_check) {
        this.apt_check = apt_check;
    }
    public void setOr_check(Boolean or_check) {
        this.or_check = or_check;
    }
}
