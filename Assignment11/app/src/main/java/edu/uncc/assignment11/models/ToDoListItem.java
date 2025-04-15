package edu.uncc.assignment11.models;

public class ToDoListItem {
    int itemId;
    String name;
    String priority;

    public ToDoListItem() {
    }

    public ToDoListItem(int id, String name, String priority) {
        this.itemId = id;
        this.name = name;
        this.priority = priority;
    }

    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
