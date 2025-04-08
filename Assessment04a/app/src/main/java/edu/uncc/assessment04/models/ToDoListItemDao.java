package edu.uncc.assessment04.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ToDoListItemDao {
    @Query("SELECT * FROM list_items WHERE list_id = :listId")
    List<ToDoListItem> getAllItemsForToDoList(int listId);

    @Insert
    void insert(ToDoListItem toDoListItem);

    @Delete
    void delete(ToDoListItem toDoListItem);

    @Update
    void update(ToDoListItem toDoListItem);
}
