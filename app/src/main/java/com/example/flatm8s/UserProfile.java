package com.example.flatm8s;

public class UserProfile {
    public String userName;
    public String userEmail;
    public String userAge;
    public String userUniversity;

    public UserProfile(){

    }

    public UserProfile(String userName, String userEmail, String userAge, String userUniversity) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userAge = userAge;
        this.userUniversity = userUniversity;
    }

    /*Getters and setters used in order to retrieve and respectively
    set different values for the data stored in the variables above
    which are pushed onto the database. */

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserUniversity() {
        return userUniversity;
    }

    public void setUserUniversity(String userUniversity) {
        this.userUniversity = userUniversity;
    }
}
