package com.example.appdiploma.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdiploma.Note;
import com.example.appdiploma.R;
import com.example.appdiploma.roomedRepository.DatabaseClient;

import java.util.GregorianCalendar;

public class UpdateNoteActivity extends AppCompatActivity {


    private EditText editTextTitle, editTextDesc;
    private TextView mDate;
    private CheckBox checkBoxFinished;
    int DIALOG_DATE = 1;
    int myYear = 2020;
    int myMonth = 3;
    int myDay = 8;

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myYear = year;
            myMonth = monthOfYear;
            myDay = dayOfMonth;
            String dateToDisplay = myYear + "/" + (myMonth + 1) + "/" + myDay;
            mDate.setText(dateToDisplay);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_update);


        editTextTitle = findViewById(R.id.title);
        editTextDesc = findViewById(R.id.text);
        mDate = findViewById(R.id.date);
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });

        checkBoxFinished = findViewById(R.id.hasDeadline);


        final Note note = (Note) getIntent().getSerializableExtra("task");

        loadTask(note);

        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
                updateNote(note);
            }
        });

        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateNoteActivity.this);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTask(note);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, myYear, myMonth, myDay);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    private void loadTask(Note note) {
        editTextTitle.setText(note.getTitle());
        editTextDesc.setText(note.getText());
        mDate.setText(note.getYear() + "/" + (note.getMonth() + 1) + "/" + note.getDay());
        myYear = note.getYear();
        myMonth = note.getMonth();
        myDay = note.getDay();

    }

    private void updateNote(final Note note) {
        final String title = editTextTitle.getText().toString().trim();
        final String text = editTextDesc.getText().toString().trim();
        final String finishBy = mDate.getText().toString().trim();

        if (title.isEmpty() || text.isEmpty()) {
            return;
        }

        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                if (finishBy.isEmpty()) {
                    myYear = 0;
                    myMonth = 0;
                    myDay = 0;
                }
                note.setTitle(title);
                note.setText(text);
                note.setDate(new GregorianCalendar(myYear, myMonth, myDay));
                note.setYear(myYear);
                note.setMonth(myMonth);
                note.setDay(myDay);
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .noteDAO()
                        .update(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateNoteActivity.this, NoteListActivity.class));
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }


    private void deleteTask(final Note note) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .noteDAO()
                        .delete(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateNoteActivity.this, NoteListActivity.class));
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }

}
