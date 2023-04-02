package com.kidsupervisor;

public class Schedule {
    private String time;
    private Boolean isSleeping;

    public Schedule(String time, Boolean isSleeping) {
        this.time = time;
        this.isSleeping = isSleeping;
    }

    public Schedule() {
    }

    public String getTime() {
        return time;
    }

    public Boolean getSleeping() {
        return isSleeping;
    }


}
