package com.example.android.foodie;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertBookMark(BookMark bookMark);

    @Query("DELETE FROM bookmark_table")
    void deleteAll();

    @Query("SELECT * from bookmark_table ORDER BY itemId Asc")
    LiveData<List<BookMark>>getBookMarkList();

    @Delete
    void deleteBookMark(BookMark bookMark);
}
