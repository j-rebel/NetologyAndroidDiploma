package com.example.appdiploma.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdiploma.App;
import com.example.appdiploma.R;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, b_reset;
    private ImageView circle1, circle2, circle3, circle4;
    private TextView hidden;
    private ArrayList<ImageView> circles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initCircles();
    }

    public void initViews() {
        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        b4 = findViewById(R.id.button4);
        b5 = findViewById(R.id.button5);
        b6 = findViewById(R.id.button6);
        b7 = findViewById(R.id.button7);
        b8 = findViewById(R.id.button8);
        b9 = findViewById(R.id.button9);
        b0 = findViewById(R.id.button0);
        b_reset = findViewById(R.id.buttonReset);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);
        b9.setOnClickListener(this);
        b0.setOnClickListener(this);
        b_reset.setOnClickListener(this);

        hidden = findViewById(R.id.hidden);
    }

    public void initCircles() {
        circle1 = findViewById(R.id.circle1);
        circle2 = findViewById(R.id.circle2);
        circle3 = findViewById(R.id.circle3);
        circle4 = findViewById(R.id.circle4);

        circles.add(circle1);
        circles.add(circle2);
        circles.add(circle3);
        circles.add(circle4);

        for (ImageView circle : circles) {
            circle.setBackground(App.getInstance().getEmpty());
        }
    }

    public void paintCircles() {
        int numberOfFilled = hidden.getText().length();
        for (int i = 0; i < circles.size(); i++) {
            if (i < numberOfFilled) {
                circles.get(i).setBackground(App.getInstance().getFilled());
            } else {
                circles.get(i).setBackground(App.getInstance().getEmpty());
            }
        }
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        int resetId = b_reset.getId();

        Button input = (Button) v;
        if(vId != resetId) {
            if (getInputLength() < 4) {
                hidden.append(input.getText());
            }
            if (getInputLength() == 4) {
                checkInput();
            }
        } else {
            String result;
            if ((getHidden() != null) && (getInputLength() > 0)) {
                result = getHidden().substring(0, getInputLength() - 1);
                hidden.setText(result);
            }
        }
        paintCircles();
    }

    public void checkInput() {
        boolean pinIsOK = App.getInstance().getKeystore().checkPin(getHidden());
        if (pinIsOK) {
            Intent intent = new Intent(this, NoteListActivity.class);
            startActivity(intent);
        } else {
            resetFields();
            Toast.makeText(this, getString(R.string.pin_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetFields();
    }

    public void resetFields() {
        hidden.setText("");
        paintCircles();
    }

    public String getHidden() {
        return hidden.getText().toString();
    }

    public int getInputLength() {
        return hidden.getText().toString().length();
    }
}
