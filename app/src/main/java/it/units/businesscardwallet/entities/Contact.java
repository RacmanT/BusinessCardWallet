package it.units.businesscardwallet.entities;


import java.io.Serializable;

public class Contact implements Serializable {

    private String name;
    private String lastName;
    private String profession;
    private String email;
    private int phoneNumber;
    private String address;


    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contact(String name, String lastName, String profession, String email, int phoneNumber, String address) {
        this.name = name;
        this.lastName = lastName;
        this.profession = profession;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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


}
