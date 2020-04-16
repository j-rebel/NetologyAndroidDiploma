package com.example.appdiploma.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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

public class AddNoteActivity extends ToolbarActivity {

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
            String dateToDisplay = myYear +
                    getString(R.string.date_divider) +
                    (myMonth + 1) +
                    getString(R.string.date_divider) +
                    myDay;
            mDate.setText(dateToDisplay);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);
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
    }

    public void initToolbar() {
        Toolbar myToolbar = findViewById(R.id.included_bar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        myToolbar.setTitle(R.string.note_add_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveTask();
                return true;
            case android.R.id.home:
                startActivity(new Intent(AddNoteActivity.this, NoteListActivity.class));
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

    private void saveTask() {
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
        Note note = new Note(title, text, myYear, myMonth, myDay);

        disposable.add(
                noteDAO
                .insert(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() {
                        startActivity(new Intent(context, NoteListActivity.class));
                        Toast.makeText(context, getString(R.string.new_note_added), Toast.LENGTH_LONG).show();
                    }
                })
        );
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