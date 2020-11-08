package com.saleh.government;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Officals implements Comparable<Officals>, Serializable {

    private String position;
    private String name;
    private String address;
    private String party;
    private String photoUrl="";
    private final HashMap<String,String> channels = new HashMap<>();
    private final ArrayList<String> phones = new ArrayList<>();

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public HashMap<String, String> getChannels() {
        return channels;
    }

    public ArrayList<String> getPhones() {
        return phones;
    }


    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public int compareTo(Officals o) {
        return this.getName().compareTo(o.getName());
    }


}
