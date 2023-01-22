package com.example.entregaprototipo.UserModel;

import java.io.Serializable;

public class UserData implements Serializable{

    private String uid;
    private String name;
    private String mail;
    private boolean isGoogleAccount;
    private double cash;
    private int phoneNumber;
    private String address;
    private int countProduct;
    private boolean isRememberMe;
    private String profileURL;

    public UserData(String uid, String name, String mail, boolean isGoogleAccount, double cash, int phoneNumber, String address, int countProduct, boolean isRememberMe, String profileURL) {
        this.name = name;
        this.mail = mail;
        this.isGoogleAccount = isGoogleAccount;
        this.cash = cash;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.countProduct = countProduct;
        this.isRememberMe = isRememberMe;
        this.profileURL = profileURL;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isGoogleAccount() {
        return isGoogleAccount;
    }

    public void setGoogleAccount(boolean googleAccount) {
        isGoogleAccount = googleAccount;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCountProduct() {
        return countProduct;
    }

    public void setCountProduct(int countProduct) {
        this.countProduct = countProduct;
    }

    public boolean isRememberMe() {
        return isRememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        isRememberMe = rememberMe;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", isGoogleAccount=" + isGoogleAccount +
                ", cash=" + cash +
                ", phoneNumber=" + phoneNumber +
                ", address='" + address + '\'' +
                ", countProduct=" + countProduct +
                ", isRememberMe=" + isRememberMe +
                ", profileURL='" + profileURL + '\'' +
                '}';
    }
}
