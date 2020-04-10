package com.example.appdiploma.activities;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.appdiploma.App;
import com.example.appdiploma.Note;
import com.example.appdiploma.NoteAdapter;
import com.example.appdiploma.R;
import com.example.appdiploma.ToolbarActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NoteListActivity extends ToolbarActivity {

    private FloatingActionButton buttonAddTask;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notelist);
        initToolbar();
        initViews();
        getNotes();
    }

    public void initViews() {
        recyclerView = findViewById(R.id.recyclerview_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonAddTask = findViewById(R.id.floating_button_add);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteListActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initToolbar() {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle(R.string.note_list_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_go_to_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, PinEditActivity.class);
        startActivity(intent);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        getNotes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getNotes();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getNotes();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getNotes();

    }

    private void getNotes() {
        class GetNotes extends AsyncTask<Void, Void, List<Note>> {

            @Override
            protected List<Note> doInBackground(Void... voids) {
                List<Note> noteList = App.getInstance().getNoteList().getAll();
                return noteList;
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                NoteAdapter adapter = new NoteAdapter(NoteListActivity.this, notes);
                recyclerView.setAdapter(adapter);
            }
        }

        GetNotes gt = new GetNotes();
        gt.execute();
    }

}