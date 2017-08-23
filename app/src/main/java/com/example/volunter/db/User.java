package com.example.volunter.db;

import android.app.*;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by dell on 2017/8/22.
 */

public class User extends BmobUser {

    private BmobFile head;

    private boolean sex;

    private String school;

    private String grade;

    private int volunteerTime;

    private ArrayList<Activity> myActivity;

    private ArrayList<Lost> myLost;

    private ArrayList<Found> myFound;

    public BmobFile getHead() {
        return head;
    }

    public void setHead(BmobFile head) {
        this.head = head;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getVolunteerTime() {
        return volunteerTime;
    }

    public void setVolunteerTime(int volunteerTime) {
        this.volunteerTime = volunteerTime;
    }

    public ArrayList<Activity> getMyActivity() {
        return myActivity;
    }

    public void setMyActivity(ArrayList<Activity> myActivity) {
        this.myActivity = myActivity;
    }

    public ArrayList<Lost> getMyLost() {
        return myLost;
    }

    public void setMyLost(ArrayList<Lost> myLost) {
        this.myLost = myLost;
    }

    public ArrayList<Found> getMyFound() {
        return myFound;
    }

    public void setMyFound(ArrayList<Found> myFound) {
        this.myFound = myFound;
    }
}
