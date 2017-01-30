package com.example.andrewwilloughby.campus_assistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private String validationError;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button loginButton;

        setContentView(R.layout.activity_login);

        setTitle("Authentication required");

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                passwordEditText.setText("");
                return false;
            }
        });

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEditText.getText().toString().toLowerCase();
                password = passwordEditText.getText().toString();

                if ((!username.isEmpty()) && (!password.isEmpty())){
                    validationError = validateUserNameEditText(username);

                    switch (validationError){
                        case "valid": {
                            Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                            intent.putExtra("username", username);
                            intent.putExtra("password", password);

                            startActivity(intent);

                            break;
                        }
                        case "invalid length": displayToast("Invalid username length."); break;
                        case "special characters": displayToast("Special Characters in username are not permitted."); break;
                        default:
                            displayToast("An unknown error occured."); break;
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

    protected void displayToast(String toastContent){
        if (!toastContent.isEmpty()){
            Toast.makeText(getApplicationContext(), toastContent, Toast.LENGTH_SHORT).show();
        }
    }
}
