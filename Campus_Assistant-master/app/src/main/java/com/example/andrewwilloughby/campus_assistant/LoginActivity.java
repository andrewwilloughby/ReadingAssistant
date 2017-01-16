package com.example.andrewwilloughby.campus_assistant;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button saveButton;
    private Context context;
    private String validationError;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Student Login");

        context = this;

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                passwordEditText.setText("");
                return false;
            }
        });

        saveButton = (Button) findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEditText.getText().toString().toLowerCase();
                password = passwordEditText.getText().toString();

                if ((username != null) && (password != null)){
                    validationError = validateUserNameEditText(username);

                    if (validationError == null){
                        Intent intent = new Intent(context, CalendarActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra("password", password);

                        startActivity(intent);

                    } else if (validationError == "invalid length"){
                        Toast.makeText(context, "Invalid username length.", Toast.LENGTH_SHORT).show();
                    } else if (validationError == "invalid format"){
                        Toast.makeText(context, "Invalid username format.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "An unknown error occured.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private String validateUserNameEditText(String username){
        if (username.length() != 8){
            return "invalid length";
        } else if (!username.matches("^[a-zA-Z0-9]*$")){
            return "invalid format";
        }

        return null;
    }
}
