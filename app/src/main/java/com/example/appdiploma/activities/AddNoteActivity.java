package com.example.appdiploma.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appdiploma.Note;
import com.example.appdiploma.R;
import com.example.appdiploma.roomedRepository.DatabaseClient;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddNoteActivity extends AppCompatActivity {

    private EditText mEditTextTitle, mEditTextDesc, mEditTextFinishBy;
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
            mEditTextFinishBy.setText(dateToDisplay);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);

        mEditTextTitle = findViewById(R.id.editTextTitle);
        mEditTextDesc = findViewById(R.id.editTextDesc);
        mEditTextFinishBy = findViewById(R.id.editTextFinishBy);
        mEditTextFinishBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });

        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
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

    private void saveTask() {
        final String title = mEditTextTitle.getText().toString().trim();
        final String text = mEditTextDesc.getText().toString().trim();
        final String finishBy = mEditTextFinishBy.getText().toString().trim();

        if (title.isEmpty() || text.isEmpty()) {
            return;
        }

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                if (finishBy.isEmpty()) {
                    myYear = 0;
                    myMonth = 0;
                    myDay = 0;
                }
                Note note = new Note(title, text, myYear, myMonth, myDay);

                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .noteDAO()
                        .insert(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), NoteListActivity.class));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }


}
