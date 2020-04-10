package com.example.appdiploma.roomedRepository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.appdiploma.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Query("SELECT * FROM note ORDER BY year DESC")
    List<Note> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);

    @Delete
    void delete(Note note);

    @Update
    void update(Note note);

}
