package com.example.asus.weclass;



public class jsonModel {
    private String OID;
    private String CID_id;
    private String Date;
    private String Time;
    private String Usage;


    public jsonModel() {
    }

    public jsonModel(String oid, String cid, String date, String time, String usage) {
        this.OID = oid;
        this.CID_id = cid;
        this.Date = date;
        this.Time = time;
        this.Usage = usage;
    }

    public String  getOID(){
        return OID;
    }

    public String getCID_id() {
        return this.CID_id;
    }

    public String getDate() {
        return this.Date;
    }

    public String getTime() {
        return this.Time;
    }

    public String getUsage() {
        return this.Usage;
    }

    public void setCID_id(String CID_id) {
        this.CID_id = CID_id;
    }

    public void setOID(String OID) {
        this.OID = OID;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public void setUsage(String usage) {
        this.Usage = usage;
    }

}



