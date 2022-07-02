package com.example.pharmaaids;
public class Notes {
    String id;
    String task;
    String how_much;
    String times;
    String hour;
    String min;
    String year;
    String month;
    String day;

    public Notes() {
    }

    public Notes(String id, String task, String how_much, String times, String hour, String min, String year, String month, String day) {
        this.id = id;
        this.task = task;
        this.how_much = how_much;
        this.times = times;
        this.hour = hour;
        this.min = min;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getHow_much() {
        return how_much;
    }

    public void setHow_much(String how_much) {
        this.how_much = how_much;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
