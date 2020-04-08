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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdiploma.Note;
import com.example.appdiploma.R;
import com.example.appdiploma.roomedRepository.DatabaseClient;

import java.util.GregorianCalendar;

public class UpdateNoteActivity extends AppCompatActivity {


    private EditText mTitle, mText;
    private TextView mDate;
    private CheckBox mHasDeadline;
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
            String dateToDisplay = myYear + getString(R.string.date_divider) + (myMonth + 1) + getString(R.string.date_divider) + myDay;
            mDate.setText(dateToDisplay);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_update);


        mTitle = findViewById(R.id.title);
        mText = findViewById(R.id.text);
        mDate = findViewById(R.id.date);
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });

        mHasDeadline = findViewById(R.id.hasDeadline);
        mHasDeadline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDate.setVisibility(View.VISIBLE);
                } else {
                    mDate.setVisibility(View.GONE);
                    mDate.setText("");
                }
            }
        });


        final Note note = (Note) getIntent().getSerializableExtra(getString(R.string.extra_label));

        loadTask(note);

        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), getString(R.string.note_updated), Toast.LENGTH_LONG).show();
                updateNote(note);
            }
        });

        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateNoteActivity.this);
                builder.setTitle(getString(R.string.dialog_sure));
                builder.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTask(note);
                    }
                });
                builder.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
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
            DatePickerDialog tpd = new DatePickerDialog(this,
                    myCallBack,
                    myYear,
                    myMonth,
                    myDay);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    private void loadTask(Note note) {
        mTitle.setText(note.getTitle());
        mText.setText(note.getText());
        String dateToDisplay = note.getYear() +
                getString(R.string.date_divider) +
                (note.getMonth() + 1) +
                getString(R.string.date_divider) +
                note.getDay();
        mDate.setText(dateToDisplay);
        myYear = note.getYear();
        myMonth = note.getMonth();
        myDay = note.getDay();

    }

    private void updateNote(final Note note) {
        final String title = mTitle.getText().toString().trim();
        final String text = mText.getText().toString().trim();
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
                Toast.makeText(getApplicationContext(), getString(R.string.note_updated), Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), getString(R.string.note_deleted), Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateNoteActivity.this, NoteListActivity.class));
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }

}
