package com.example.appdiploma.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.Dialog;
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

public class AddNoteActivity extends ToolbarActivity {

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
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
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

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                if (finishBy.isEmpty()) {
                    myYear = 0;
                    myMonth = 0;
                    myDay = 0;
                }
                Note note = new Note(title, text, myYear, myMonth, myDay);

                App.getInstance().getNoteList().insert(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                startActivity(new Intent(getApplicationContext(), NoteListActivity.class));
                Toast.makeText(getApplicationContext(), getString(R.string.new_note_added), Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }


}
