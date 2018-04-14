package com.ssdut.houlx.secondhandtransactionsystem;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @author houlx
 */
public class Commodity extends DataSupport implements Serializable {
    private String name;
    private String type;
    private double price;
    private int imageId;
    private String description;

    Commodity(String name, String type, double price, int imageId, String description) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.imageId = imageId;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
