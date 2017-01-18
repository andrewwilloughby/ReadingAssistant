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

        setTitle("Authentication required");

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

                    switch (validationError){
                        case "valid": {
                            Intent intent = new Intent(context, CalendarActivity.class);
                            intent.putExtra("username", username);
                            intent.putExtra("password", password);

                            startActivity(intent);
                        }
                        case "invalid length": Toast.makeText(context, "Invalid username length.", Toast.LENGTH_SHORT).show(); break;
                        case "special characters": Toast.makeText(context, "Special Characters in username are not permitted.", Toast.LENGTH_SHORT).show(); break;
                        default:
                            Toast.makeText(context, "An unknown error occured.", Toast.LENGTH_SHORT).show(); break;
                    }
                }
            }
        });
    }

    public String validateUserNameEditText(String username){
        if (username.length() != 8){
            return "invalid length";
        } else if (!username.matches("^[a-zA-Z0-9]*$")){
            return "special characters";
        } else if (!username.matches(("^[a-zA-Z]{2}\\d{6}"))){
            return "invalid format";
        }

        return "valid";
    }
}
