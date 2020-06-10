package com.example.bluetoothtest.Models.bleCallBacks.sleep_band;



public class OutOfBedModel {

    private String firstDate= "null";
    private String duration1= "null";
    private String endFirstDate= "null";

    private String secondDate = "null";
    private String duration2= "null";
    private String endSecondDate= "null";


    public String getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    public String getDuration1() {
        return duration1;
    }

    public void setDuration1(String duration1) {
        this.duration1 = duration1;
    }

    public String getSecondDate() {
        return secondDate;
    }

    public void setSecondDate(String secondDate) {
        this.secondDate = secondDate;
    }

    public String getDuration2() {
        return duration2;
    }

    public void setDuration2(String duration2) {
        this.duration2 = duration2;
    }

    public String getEndFirstDate() {
        return endFirstDate;
    }

    public void setEndFirstDate(String endFirstDate) {
        this.endFirstDate = endFirstDate;
    }

    public String getEndSecondDate() {
        return endSecondDate;
    }

    public void setEndSecondDate(String endSecondDate) {
        this.endSecondDate = endSecondDate;
    }

    @Override
    public String toString() {
        return "OutOfBedModel{" +
                "firstDate='" + firstDate + '\'' +
                ", duration1='" + duration1 + '\'' +
                ", endFirstDate='" + endFirstDate + '\'' +
                ", secondDate='" + secondDate + '\'' +
                ", duration2='" + duration2 + '\'' +
                ", endSecondDate='" + endSecondDate + '\'' +
                '}';
    }
}
