package edu.uncc.assignment11.models;

import java.io.Serializable;

public class ToDoList implements Serializable {
    private int todolistId;
    private String name;

    public ToDoList() {
    }

    public ToDoList(int todolistId, String name) {
        this.todolistId = todolistId;
        this.name = name;
    }

    public int getTodolistId() {
        return todolistId;
    }

    public void setTodolistId(int todolistId) {
        this.todolistId = todolistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
