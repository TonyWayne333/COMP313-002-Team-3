package com.example.onlineattendancesystem;

public class Student {

    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String imageUrl;
    private String presence = "False";

    public Student() {
        super();
    }

    public Student(String studentId, String firstName, String lastName, String email, String phone, String imageUrl) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.imageUrl = imageUrl;
    //    this.presence = presence;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPresence() {
        return this.presence;
    }

    public void setPresence(String presence) {
        this.presence = presence;
    }
}