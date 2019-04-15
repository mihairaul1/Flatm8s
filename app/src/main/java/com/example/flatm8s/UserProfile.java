package com.example.flatm8s;

public class UserProfile {
    public String userName;
    public String userEmail;
    public String userDOB;
    public String userAge;
    public String userUniversity;
    public String userCourse;
    public String userYear;

    public UserProfile(){

    }

    public UserProfile(String userName, String userEmail,String userDOB, String userAge,
                       String userUniversity, String userCourse, String userYear) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userAge = userAge;
        this.userDOB = userDOB;
        this.userUniversity = userUniversity;
        this.userCourse = userCourse;
        this. userYear = userYear;
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

    public String getUserDOB() {
        return userDOB;
    }

    public void setUserDOB(String userDOB) {
        this.userDOB = userDOB;
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

    public String getUserCourse() {
        return userCourse;
    }

    public void setUserCourse(String userCourse) {
        this.userCourse = userCourse;
    }

    public String getUserYear() {
        return userYear;
    }

    public void setUserYear(String userYear) {
        this.userYear = userYear;
    }
}
