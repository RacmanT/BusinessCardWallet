package it.units.businesscardwallet.entities;


import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

// TODO rename in user
public class Contact implements Serializable {

    private String name;
    private String lastName;
    private String profession;
    private String email;
    private String phoneNumber;
    private String address;
    private String institution;

    public Contact() {
        this.name = "";
        this.lastName = "";
        this.profession = "";
        this.email = "";
        this.phoneNumber = "";
        this.address = "";
        this.institution = "";
    }

    public Contact(String name, String lastName, String profession, String email, String phoneNumber, String address, String institution) {
        this.name = name;
        this.lastName = lastName;
        this.profession = profession;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.institution = institution;
    }


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

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInstitution() {
        return institution;
    }


    @NonNull
    @Override
    public String toString() {
        return name + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return name.equals(contact.name) && lastName.equals(contact.lastName) &&
                profession.equals(contact.profession) && email.equals(contact.email) &&
                Objects.equals(phoneNumber, contact.phoneNumber) && Objects.equals(address, contact.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastName, profession, email, phoneNumber, address);
    }


}
