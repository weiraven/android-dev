package edu.uncc.assessment04.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "todo_lists")
public class ToDoList implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "owner_id")
    int ownerId;

    public ToDoList() {
    }

    public ToDoList(String name, int userId) {
        this.name = name;
        this.ownerId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int userId) {
        this.ownerId = userId;
    }

    public int getId() {
        return this.id;
    }
}
