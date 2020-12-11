package com.example.jeonse_alarm;


public class StationConstruct{


    private String name;
    private String line;
    private Double latitude;
    private Double longitude;
    private Integer stnId;

    public String getName() {
        return name;
    }
    public String getLine() {
        return line;
    }
    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Integer getStnId() {
        return stnId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setStnId(Integer stnId) {
        this.stnId = stnId;
    }

}