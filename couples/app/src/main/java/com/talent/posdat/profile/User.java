package com.talent.posdat.profile;

public class User {
    private  String country;
    private  String username;
    private  String profileImage;
    private  String fullname;
    private  String status;
    private String uid;




    public User() {
    }

    public User(String country, String username, String profileImage,String fullname, String status,String uid) {
        this.country = country;
        this.username = username;
        this.fullname = fullname;
        this.status = status;
        this.uid=uid;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
