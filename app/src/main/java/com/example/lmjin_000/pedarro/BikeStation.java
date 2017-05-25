package com.example.lmjin_000.pedarro;

import com.orm.SugarRecord;

/**
 * Created by lmjin_000 on 2015-12-02.
 */
public class BikeStation extends SugarRecord<BikeStation> {
    String name;
    double lat;
    double hard;
    int allbike;

    public BikeStation() {

    }

    public BikeStation(String name, double lat, double hard, int allbike) {

        this.name = name;
        this.lat = lat;
        this.hard = hard;
        this.allbike = allbike;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getHard() {
        return hard;
    }

    public void setHard(double hard) {
        this.hard = hard;
    }

    public int getAllbike() {
        return allbike;
    }

    public void setAllbike(int allbike) {
        this.allbike = allbike;
    }
}
