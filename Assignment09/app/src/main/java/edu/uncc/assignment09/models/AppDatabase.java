package edu.uncc.assignment09.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}