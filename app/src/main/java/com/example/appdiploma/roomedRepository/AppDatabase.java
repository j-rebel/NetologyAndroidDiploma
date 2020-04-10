package com.example.appdiploma.roomedRepository;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.appdiploma.Note;

@Database(entities = {Note.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDAO noteDAO();
}
