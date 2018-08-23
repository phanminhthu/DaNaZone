package com.example.danazone04.danazone.bean;

public class Run {
    private int id;
    private String time;
    private String date;
    private String speed;
    private String distance;
    private String calo;
    private String timeStart;

    public Run() {

    }

    public Run(int id, String time, String date, String speed, String distance, String calo, String timeStart) {
        this.id = id;
        this.time = time;
        this.date = date;
        this.speed = speed;
        this.distance = distance;
        this.calo = calo;
        this.timeStart = timeStart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCalo() {
        return calo;
    }

    public void setCalo(String calo) {
        this.calo = calo;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }
}
