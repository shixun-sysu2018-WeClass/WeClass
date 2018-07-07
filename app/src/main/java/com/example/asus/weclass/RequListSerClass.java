package com.example.asus.weclass;

/**
 * Created by ASUS on 2018/6/12.
 */

public class RequListSerClass {
    private String UID;
    private String AID;
    private String CID__RoomName;
    private String StartTime;
    private String EndTime;
    private String StartDate;
    private String Status;
    private String BookDate;
    private String Usage;
    private String Reason;
    private String StudentNumber;
    private String StudentName;
    private String PhoneNumber;



    public String getAID() {
        return AID;
    }
    public String getUID() {
        return UID;
    }
    public String getReason() {return Reason;}
    public String getStatus() {
        return Status;
    }

    public String getCID__RoomName() {
        return CID__RoomName;
    }
    public String getStudentName() {return StudentName;}
    public String getStartTime() {
        return StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }
    public String getStartDate() {return StartDate;}
    public String getBookDate() {
        return BookDate;
    }
    public String getUsage() {return Usage;}
    public String getPhoneNumber() {
        return PhoneNumber;
    }
    public String getStudentNumber() {
        return StudentNumber;
    }
}
