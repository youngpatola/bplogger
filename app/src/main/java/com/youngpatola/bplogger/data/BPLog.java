package com.youngpatola.bplogger.data;

/**
 * Created by youngpatola on 04/03/2018.
 */

public class BPLog {
    private int id;
    private String systolic;
    private String diastolic;
    private String heartRate;
    private long datetime;

    private String note;
    private String indicator;
    private String feel;
    private String position;
    private String arm;

    public BPLog() {
    }

    public BPLog(int id, String systolic, String diastolic, String heartRate, long datetime, String note, String feel, String position, String arm) {
        this.id = id;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.datetime = datetime;
        this.heartRate = heartRate;
        this.note = note;
        this.feel = feel;
        this.position = position;
        this.arm = arm;
    }

    public BPLog(String systolic, String diastolic, String heartRate, long datetime, String note, String feel, String position, String arm) {
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.heartRate = heartRate;
        this.datetime = datetime;
        this.note = note;
        this.feel = feel;
        this.position = position;
        this.arm = arm;
    }

    //    public BPLog(String systolic, String diastolic, long datetime, String heartRate) {
//        this.systolic = systolic;
//        this.diastolic = diastolic;
//        this.datetime = datetime;
//        this.heartRate = heartRate;
//    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSystolic() {
        return systolic;
    }

    public void setSystolic(String systolic) {
        this.systolic = systolic;
    }

    public String getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(String diastolic) {
        this.diastolic = diastolic;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public String getFeel() {
        return feel;
    }

    public void setFeel(String feel) {
        this.feel = feel;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getArm() {
        return arm;
    }

    public void setArm(String arm) {
        this.arm = arm;
    }

    @Override
    public String toString() {
        return String.valueOf(systolic) + " / " + String.valueOf(diastolic);
    }
}
