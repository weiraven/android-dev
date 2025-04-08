package edu.uncc.assessment04.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ToDoListDao {
    @Insert
    void insert(ToDoList toDoList);

    @Update
    void update(ToDoList toDoList);

    @Delete
    void delete(ToDoList toDoList);

    @Query("SELECT * FROM todo_lists WHERE owner_id = :userId")
    List<ToDoList> getAllToDoListsForUser(int userId);

    @Query("SELECT * FROM todo_lists WHERE id = :id")
    ToDoList getToDoListById(int id);

}
