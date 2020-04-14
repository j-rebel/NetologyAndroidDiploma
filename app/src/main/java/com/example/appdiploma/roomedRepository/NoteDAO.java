package com.example.appdiploma.roomedRepository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Completable;
import io.reactivex.Observable;

import com.example.appdiploma.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Query("SELECT * FROM note ORDER BY year DESC")
    Observable<List<Note>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Note note);

    @Delete
    Completable delete(Note note);

    @Update
    Completable update(Note note);
}