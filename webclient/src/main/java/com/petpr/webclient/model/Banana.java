package com.petpr.webclient.model;

public class Banana {
    String size = "";

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Banana{" +
                "size='" + size + '\'' +
                '}';
    }
}
