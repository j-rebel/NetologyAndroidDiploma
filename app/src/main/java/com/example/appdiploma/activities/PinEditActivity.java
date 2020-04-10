package com.example.appdiploma.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appdiploma.App;
import com.example.appdiploma.R;
import com.example.appdiploma.ToolbarActivity;

public class PinEditActivity extends ToolbarActivity {

    private EditText pinInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_edit);
        initToolbar();
        initView();
    }

    public void initView() {
        pinInput = findViewById(R.id.pinInput);
    }

    public void initToolbar() {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        myToolbar.setTitle(R.string.pin_edit_title);
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
                String newPin = pinInput.getText().toString();

                if (newPin.length() == 4) {
                    App.getInstance().getKeystore().saveNew(newPin);
                    App.getInstance().setNotFirstTime();
                    Toast.makeText(App.getInstance().getContext(), getString(R.string.pin_edit_ok), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(PinEditActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    pinInput.setText("");
                    Toast.makeText(App.getInstance().getContext(), getString(R.string.pin_edit_error), Toast.LENGTH_LONG).show();
                }
                return true;
            case android.R.id.home:
                startActivity(new Intent(PinEditActivity.this, NoteListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
