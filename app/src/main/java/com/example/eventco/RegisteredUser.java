package com.example.eventco;

public class RegisteredUser {
   private String name,imageUrl;

    public String getName() {
        return name;
    }

    public RegisteredUser(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public RegisteredUser() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
