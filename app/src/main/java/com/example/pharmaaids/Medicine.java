package com.example.pharmaaids;

public class Medicine {

    String name;
    String pid;
    String availability;
    String price;

    public Medicine() {
    }

    public Medicine(String name, String pid, String availability, String price) {
        this.name = name;
        this.pid = pid;
        this.availability = availability;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
