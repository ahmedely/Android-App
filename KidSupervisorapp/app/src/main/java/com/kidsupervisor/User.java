package com.kidsupervisor;

import org.json.JSONArray;

import java.util.ArrayList;

public class User {
    private String id;
    private String fullName;
    private String email;
    private String selectedKid;
    private ArrayList<Kid> kids;

    public User(String id,String fullName,  String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.selectedKid = "";
        this.kids = new ArrayList();
    }

    public User(String id,String fullName,  String email, String selectedKid) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.selectedKid = selectedKid;
        this.kids = new ArrayList();
    }

    public User() {
        this.kids = new ArrayList();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSelectedKid() {
        return selectedKid;
    }

    public void setSelectedKid(String selectedKid) {
        this.selectedKid = selectedKid;
    }

    public ArrayList<Kid> getKids() {
        return kids;
    }

    public void setKids(ArrayList<Kid> kids) {
        this.kids = kids;
    }

    public void addKid(Kid kid){
        kids.add(kid);
    }
}
