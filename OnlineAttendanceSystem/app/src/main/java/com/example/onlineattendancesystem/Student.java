package com.example.onlineattendancesystem;

public class Student {

    private String studentId;
    private String name;
    private String email;
    private String phone;
    private String imageUrl;
    private String presence = "False";

    public Student() {
        super();
    }

    public Student(String studentId, String name, String email, String phone, String imageUrl) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.imageUrl = imageUrl;
    }

    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
        return presence;
    }
    public void setPresence(String presence) {
        this.presence = presence;
    }

}