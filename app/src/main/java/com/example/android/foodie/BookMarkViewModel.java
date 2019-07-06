package com.example.android.foodie;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.ListIterator;

public class BookMarkViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<BookMark>>bookMarkList;

    public BookMarkViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
        bookMarkList = repository.getAllBookMarks();
    }

    //Get bookmark list stored by user from Room
    LiveData<List<BookMark>> getBookMarkList(){
        return bookMarkList;
    }

    //Insert bookmark to room
    void insertBookMark(BookMark bookMark){
        repository.insertBookMark(bookMark);
    }

    //deleteAll bookmarks from room
    void delteAll(){
        repository.deleteAllBookMarks();
    }

    //delete a single bookmark from room
    void deleteBookMark(BookMark bookMark){
        repository.deleteBookMark(bookMark);
    }
}
