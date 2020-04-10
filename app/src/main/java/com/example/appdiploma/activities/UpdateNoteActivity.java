package com.example.appdiploma.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdiploma.App;
import com.example.appdiploma.Note;
import com.example.appdiploma.R;
import com.example.appdiploma.ToolbarActivity;

import java.util.GregorianCalendar;

public class UpdateNoteActivity extends ToolbarActivity {


    private EditText mTitle, mText;
    private TextView mDate;
    private CheckBox mHasDeadline;
    int DIALOG_DATE = App.getInstance().getDIALOG_DATE();
    int myYear = App.getInstance().getYear();
    int myMonth = App.getInstance().getMonth();
    int myDay = App.getInstance().getDay();

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
        initToolbar();
        initViews();
    }

    public void initViews() {
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
    }

    public void initToolbar() {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        myToolbar.setTitle(R.string.note_update_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_updade_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_update:
                Toast.makeText(getApplicationContext(), getString(R.string.note_updated), Toast.LENGTH_LONG).show();
                updateNote((Note) getIntent().getSerializableExtra(getString(R.string.extra_label)));
                return true;
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateNoteActivity.this);
                builder.setTitle(getString(R.string.dialog_sure));
                builder.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTask((Note) getIntent().getSerializableExtra(getString(R.string.extra_label)));
                    }
                });
                builder.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        myYear = note.getYear();
        myMonth = note.getMonth();
        myDay = note.getDay();
        if(note.getYear() == 0 || dateToDisplay.isEmpty()) {
            mHasDeadline.setChecked(false);
            mDate.setText("");
            myYear = App.getInstance().getYear();
            myMonth = App.getInstance().getMonth();
            myDay = App.getInstance().getDay();
        } else {
            mHasDeadline.setChecked(true);
            mDate.setText(dateToDisplay);
        }


    }

    private void updateNote(final Note note) {
        final String title = mTitle.getText().toString().trim();
        final String text = mText.getText().toString().trim();
        final String finishBy = mDate.getText().toString().trim();

        if (title.isEmpty() && text.isEmpty()) {
            mTitle.setError(getString(R.string.no_title_or_text));
            mText.setError(getString(R.string.no_title_or_text));
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
                note.setState(note.compareToToday(note.getDate()));
                App.getInstance().getNoteList().update(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), getString(R.string.note_updated), Toast.LENGTH_LONG).show();
                startActivity(new Intent(UpdateNoteActivity.this, NoteListActivity.class));
                finish();
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }


    private void deleteTask(final Note note) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                App.getInstance().getNoteList().delete(note);
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
