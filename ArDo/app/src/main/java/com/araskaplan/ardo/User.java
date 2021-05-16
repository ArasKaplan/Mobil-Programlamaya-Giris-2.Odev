    package com.araskaplan.ardo;


import android.graphics.Bitmap;

public class User {
    String name;
    String surname;
    String phonenumber;
    String mail;
    String password;
    String bDate;
    Bitmap image;

    public User(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }

    public User(String name, String surname, String phonenumber, String mail, String password, String bDate) {
        this.name = name;
        this.surname = surname;
        this.phonenumber = phonenumber;
        this.mail = mail;
        this.password = password;
        this.bDate = bDate;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getbDate() {
        return bDate;
    }

    public void setbDate(String bDate) {
        this.bDate = bDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    }

