package com.example.onlineattendancesystem;

public class Professor_Image {

    private String imageName;
    private String imageUrl;

    public Professor_Image() {
        super();
    }

    public Professor_Image(String imageName, String imageUrl) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}