package com.example.ktran.wannabetinder.models;

/**
 * Created by ktran on 11/26/17.
 */

public class User {
    private String name;
    private String password;
    private String department;
    private String phone;
    private int[] survey_data;
    private String[] friends;

    @Override
    public String toString() {
        return "Name: " + this.name + " | Phone: " + this.phone + " | Department: " + this.department;
    }

    public User(String name, String password, String department, String phone, int[] survey_results) {
        this.survey_data = survey_results;
        this.name = name;
        this.password = password;
        this.department = department;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public int[] getSurvey_results() {
        return survey_data;
    }

    public void setSurvey_results(int[] survey_results) {
        this.survey_data = survey_results;
    }

    public String[] getFriends() {
        return friends;
    }

    public void setFriends(String[] friends) {
        this.friends = friends;
    }
}
