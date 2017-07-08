package com.example.saurabh.mess2owner;

/**
 * Created by saurabh on 26/6/17.
 */

public class User {


    private String batch;
    private String groupid;
    private String name;
   // private String paidtime;
    private String scanneddinner;
    private String scannedlunch;
    private String uid;
    private String endsub;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User() {
    }

    public User(String batch, String groupid, String name/*, String paidtime*/, String scanneddinner, String scannedLunch,String endsub) {
        this.batch = batch;
        this.groupid = groupid;
        this.name = name;
       // this.paidtime = paidtime;
        this.scanneddinner = scanneddinner;
        this.scannedlunch = scannedLunch;
        this.endsub=endsub;
    }

    public String getEndsub() {
        return endsub;
    }

    public void setEndsub(String endsub) {
        this.endsub = endsub;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   /* public String getPaidtime() {
        return paidtime;
    }*/

/*
    public void setPaidtime(String paidtime) {
        this.paidtime = paidtime;
    }
*/

    public String getScanneddinner() {
        return scanneddinner;
    }

    public void setScanneddinner(String scanneddinner) {
        this.scanneddinner = scanneddinner;
    }

    public String getScannedLunch() {
        return scannedlunch;
    }

    public void setScannedLunch(String scannedLunch) {
        this.scannedlunch = scannedLunch;
    }

    @Override
    public String toString() {
        return "User{" +
                "batch='" + batch + '\'' +
                ", groupid='" + groupid + '\'' +
                ", name='" + name + '\'' +
              //  ", paidtime='" + paidtime + '\'' +
                ", scanneddinner='" + scanneddinner + '\'' +
                ", scannedlunch='" + scannedlunch + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
