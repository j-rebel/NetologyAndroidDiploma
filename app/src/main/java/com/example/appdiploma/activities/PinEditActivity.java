package com.example.appdiploma.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appdiploma.App;
import com.example.appdiploma.R;
import com.example.appdiploma.ToolbarActivity;

public class PinEditActivity extends ToolbarActivity {

    private Button saveButton;
    private EditText pinInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_edit);
        initToolbar();
        initView();
    }

    public void initView() {
        saveButton = findViewById(R.id.button_save);
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
        String newPin = pinInput.getText().toString();

        if (newPin.length() == 4) {
            App.getInstance().getKeystore().saveNew(newPin);
            App.getInstance().setNotFirstTime();
            Toast.makeText(App.getInstance().getContext(), getString(R.string.pin_edit_ok), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(PinEditActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            pinInput.setText("");
            Toast.makeText(App.getInstance().getContext(), getString(R.string.pin_edit_error), Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
