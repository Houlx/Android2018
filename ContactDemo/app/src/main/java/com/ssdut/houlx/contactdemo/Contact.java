package com.ssdut.houlx.contactdemo;

import java.io.Serializable;

/**
 * @author houlx
 */
public class Contact implements Serializable {
    private String name;
    private String phone;
    private String email;
    private String addr;

    private int imageId;

    Contact(String name, String phone, String email, String addr, int imageId) {
        this.name = name;
        this.phone = phone;

        this.email = email;
        this.addr = addr;
        this.imageId = imageId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddr() {
        return addr;
    }

    public int getImageId() {
        return imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
