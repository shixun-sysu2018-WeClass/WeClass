package com.example.asus.weclass;

/**
 * Created by ASUS on 2018/6/10.
 */

import org.json.JSONObject;
import org.json.JSONException;

public class AppointmentUsrData {

    //    private int applicationId;
    private int UID;
    private int AID;
    private String RoomName;
    private int StartTime;
    private int EndTime;
    private String StartDate; //申请使用的开始时间
    private int status;
    private String BookDate; //发出申请的时间
    private String Statement;
    //    private classTime endString;


    public AppointmentUsrData(int UID, int AID, String roomName, int startTime, int endTime, String startDate, int status, String bookDate, String statement) {
        this.UID = UID;
        this.AID = AID;
        RoomName = roomName;
        StartTime = startTime;
        EndTime = endTime;
        StartDate = startDate;
        this.status = status;
        BookDate = bookDate;
        Statement = statement;
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public int getAID() {
        return AID;
    }

    public void setAID(int AID) {
        this.AID = AID;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public int getStartTime() {
        return StartTime;
    }

    public void setStartTime(int startTime) {
        StartTime = startTime;
    }

    public int getEndTime() {
        return EndTime;
    }

    public void setEndTime(int endTime) {
        EndTime = endTime;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public int getStatus() {
        return status;
    }

    public String getBookDate() {
        return BookDate;
    }

    public String getStatement() {
        return Statement;
    }

    public void setStatement(String statement) {
        Statement = statement;
    }

    public int getStatusImg() {
        switch (status) {
            case 0:
                return R.mipmap.waiting;
            case 1:
                return R.mipmap.checked;
            case 2:
                return R.mipmap.wrong;
            default:
                return 0;
        }
    }

    public static AppointmentUsrData appointmentData(JSONObject json) {
        try {
            return new AppointmentUsrData(
                    json.getInt("UID"),
                    json.getInt("AID"),
                    json.getString("RoomName"),
                    json.getInt("StartTime"),
                    json.getInt("EndTime"),
                    json.getString("StartDate"),
                    json.getInt("Status"),
                    json.getString("BookDate"),
                    json.getString("Statement")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

