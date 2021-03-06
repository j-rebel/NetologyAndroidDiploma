package com.example.appdiploma.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.appdiploma.Note;
import com.example.appdiploma.R;
import com.example.appdiploma.ToolbarActivity;
import com.example.appdiploma.roomedRepository.NoteDAO;

import java.util.GregorianCalendar;

public class UpdateNoteActivity extends ToolbarActivity {


    private EditText mTitle, mText;
    private TextView mDate;
    private CheckBox mHasDeadline;
    int DIALOG_DATE;
    int myYear;
    int myMonth;
    int myDay;
    private CompositeDisposable disposable = new CompositeDisposable();
    private NoteDAO noteDAO;
    private Context context;

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
        Toolbar myToolbar = findViewById(R.id.included_bar);
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
                Toast.makeText(context, getString(R.string.note_updated), Toast.LENGTH_LONG).show();
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
            case android.R.id.home:
                startActivity(new Intent(UpdateNoteActivity.this, NoteListActivity.class));
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
        if (note.getYear() == 0 || dateToDisplay.isEmpty()) {
            mHasDeadline.setChecked(false);
            mDate.setText("");
        } else {
            myYear = note.getYear();
            myMonth = note.getMonth();
            myDay = note.getDay();
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
        disposable.add(noteDAO.update(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() {
                        Toast.makeText(context, getString(R.string.note_updated), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UpdateNoteActivity.this, NoteListActivity.class));
                    }
                }));
    }


    private void deleteTask(final Note note) {
        disposable.add(noteDAO.delete(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() {
                        Toast.makeText(context, getString(R.string.note_deleted), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UpdateNoteActivity.this, NoteListActivity.class));
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    public void setDIALOG_DATE(int DIALOG_DATE) {
        this.DIALOG_DATE = DIALOG_DATE;
    }

    public void setMyYear(int myYear) {
        this.myYear = myYear;
    }

    public void setMyMonth(int myMonth) {
        this.myMonth = myMonth;
    }

    public void setMyDay(int myDay) {
        this.myDay = myDay;
    }

    public void setNoteDAO(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}