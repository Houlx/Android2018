package com.ssdut.houlx.addrbook.db;

import java.io.Serializable;

/**
 * @author houlx
 */
public class Contact implements Serializable {
    private String name;
    private String nameLetters;
    private String office;
    private String phoneOffice;
    private String phonePersonal;
    private String email;

    public Contact() {
    }

    public String getNameLetters() {
        return nameLetters;
    }

    public void setNameLetters(String nameLetters) {
        this.nameLetters = nameLetters;
    }

    public Contact(String name, String office, String phoneOffice, String phonePersonal, String email, String nameLetters) {
        this.name = name;
        this.office = office;
        this.phoneOffice = phoneOffice;
        this.phonePersonal = phonePersonal;
        this.email = email;
        this.nameLetters = nameLetters;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getPhoneOffice() {
        return phoneOffice;
    }

    public void setPhoneOffice(String phoneOffice) {
        this.phoneOffice = phoneOffice;
    }

    public String getPhonePersonal() {
        return phonePersonal;
    }

    public void setPhonePersonal(String phonePersonal) {
        this.phonePersonal = phonePersonal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
