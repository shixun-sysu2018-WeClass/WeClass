package com.example.asus.weclass;

/**
 * Created by ASUS on 2018/6/13.
 */


/**
 * Created by weihuajian on 16/6/12.
 */
public class CourseModel {

    private int id; //课程ID
    private String courseName;
    private int section; //从第几节课开始
    private int oid; //跨几节课
    private int week; //周几
    private String classRoom; //教室
    private int courseFlag; //课程背景颜色

    public CourseModel() {
    }

    public CourseModel(int id, String courseName, int section, int oid,int week,String classRoom,int courseFlag) {
        this.id = id;
        this.courseName = courseName;
        this.section = section;
        this.oid = oid;
        this.week = week;
        this.classRoom = classRoom;
        this.courseFlag = courseFlag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getOid() {
        return this.oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getCourseFlag() {
        return courseFlag;
    }

    public void setCourseFlag(int courseFlag) {
        this.courseFlag = courseFlag;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }
}
