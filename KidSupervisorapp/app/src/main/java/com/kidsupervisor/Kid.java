package com.kidsupervisor;

public class Kid {

    private String id;
    private String fullName;
    private String age;
    private String weight;
    private String height;

    public Kid(String fullName, String age, String weight, String height) {
        this.fullName = fullName;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Kid() {
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

}
