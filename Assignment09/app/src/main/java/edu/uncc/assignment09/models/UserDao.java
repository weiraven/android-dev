package edu.uncc.assignment09.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

    // @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
    //           "last_name LIKE :last LIMIT 1")
    //    User findByName(String first, String last);
    @Query("SELECT * FROM user where credit_score >= :threshold order by name ASC")
    List<User> getFilteredScoresSortByName(int threshold);

    @Query("SELECT * FROM user where credit_score >= :threshold order by age ASC")
    List<User> getFilteredScoresSortByAge(int threshold);

    @Query("SELECT * FROM user where credit_score >= :threshold order by credit_score ASC")
    List<User> getFilteredScoresSortByScore(int threshold);

    @Query("SELECT * FROM user where credit_score >= :threshold order by state ASC")
    List<User> getFilteredScoresSortByState(int threshold);
}

