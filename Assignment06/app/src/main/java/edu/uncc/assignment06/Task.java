package edu.uncc.assignment06;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    String name;
    Date date;
    String priority;

    public Task(String name, Date date, String priority) {
        this.name = name;
        this.date = date;
        this.priority = priority;
    }

    public Task(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
