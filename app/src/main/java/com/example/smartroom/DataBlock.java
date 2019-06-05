package com.example.smartroom;


import java.io.Serializable;

public class DataBlock implements Serializable {

    private String id;
    private String timestamp;
    private String temp_in;
    private String temp_out;
    private String light_in;
    private String isClimeOn;
    private String isWindowOpen;
    private String air_qua_in;
    private String air_qua_out;
    private String sound_detected;
    private String people_inside;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTempIn() {
        return temp_in;
    }

    public String getTempOut() {
        return temp_out;
    }

    public String getLightIn() {
        return light_in;
    }

    public String getClimeOn()
    {
        return isClimeOn.equals("0") ? "OFF" : "ON";
    }

    public String getWindowOpen() {
        return isWindowOpen;
    }

    public String getAirQuaIn() {
        return air_qua_in;
    }

    public String getAirQuaOut() {
        return air_qua_out;
    }

    public String isSoundDetected() {
        return sound_detected;
    }

    public String getPeopleInside() {
        return people_inside;
    }
}