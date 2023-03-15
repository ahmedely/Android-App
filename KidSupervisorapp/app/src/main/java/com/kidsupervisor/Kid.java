package com.kidsupervisor;

public class Kid {
    private String name;
    private int age;
    private double weight;
    private boolean saved;

    public Kid(){
        name = "";
        age = 0;
        weight = 0.0;
        saved = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getWeight() {
        return weight;
    }

    public boolean isSaved() {return saved; }

    public void setSaved(boolean saved) { this.saved = saved; }
}



