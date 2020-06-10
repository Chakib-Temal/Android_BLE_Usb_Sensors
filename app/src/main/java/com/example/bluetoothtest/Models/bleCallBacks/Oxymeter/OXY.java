package com.example.bluetoothtest.Models.bleCallBacks.Oxymeter;

public class OXY {

    private int sat;
    private int fq;

    public OXY(int sat, int fq) {
        this.sat = sat;
        this.fq = fq;
    }

    public int getSat() {
        return sat;
    }

    public void setSat(int sat) {
        this.sat = sat;
    }

    public int getFq() {
        return fq;
    }

    public void setFq(int fq) {
        this.fq = fq;
    }
}
