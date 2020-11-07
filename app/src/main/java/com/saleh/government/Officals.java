package com.saleh.government;

public class Officals {

    private String position;
    private String name;

    public Officals() {
    }

    public Officals(String position, String name) {
        this.position = position;
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
