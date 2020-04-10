package com.example.appdiploma.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appdiploma.App;
import com.example.appdiploma.R;

public class PinEditActivity extends AppCompatActivity {

    private Button saveButton;
    private EditText pinInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_edit);
        initView();
    }

    public void initView() {
        saveButton = findViewById(R.id.button_save);
        pinInput = findViewById(R.id.pinInput);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }
}
