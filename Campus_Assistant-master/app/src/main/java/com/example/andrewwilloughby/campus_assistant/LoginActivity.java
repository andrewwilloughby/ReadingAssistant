package com.example.andrewwilloughby.campus_assistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity for login screen to obtain university credentials.
 *
 * @author Andrew Willoughby
 */
public class LoginActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener{

    private EditText passwordEditText, usernameEditText;

    /**
     * Method to set up the Activity upon creation.
     *
     * @param savedInstanceState parameter which indicates the previous state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setTitle("Authentication required");

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);

        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.setOnTouchListener(this);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
    }

    /**
     * Method to handle onTouch events on EditText boxes.
     * @param v the view that has triggered the onTouch event.
     * @param event the event that has been triggered.
     * @return boolean value to indicate whether the listener has consumed the event.
     */
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.passwordEditText:passwordEditText.setText("");
        }
        return false;
    }

    /**
     * Method to handle onClick events from buttons displayed in the activity.
     *
     * @param viewInput the view initiating the onClick method.
     */
    public void onClick(View viewInput) {
        switch (viewInput.getId()){
            case R.id.loginButton:{
                String username = usernameEditText.getText().toString().toLowerCase();
                String password = passwordEditText.getText().toString();

                if ((!username.isEmpty()) && (!password.isEmpty())){
                    String validationError = validateUsernameEditText(username);

                    switch (validationError){
                        case "valid": {
                            Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                            intent.putExtra("username", username);
                            intent.putExtra("password", password);

                            startActivity(intent);

                            break;
                        }
                        case "invalid length":{
                            Toast.makeText(getApplicationContext(), "Invalid username length.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case "special characters":{
                            Toast.makeText(getApplicationContext(), "Special Characters in username are not permitted.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        default:
                            Toast.makeText(getApplicationContext(), "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                break;
            }
        }
    }

    /**
     * Method to validate user input to the username EditText box.
     *
     * @param username the user-defined string.
     * @return a string relevant to the validation of the username string.
     */
    public String validateUsernameEditText(String username){
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
