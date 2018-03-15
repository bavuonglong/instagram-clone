package com.codeko.instagramclone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvChangeSignUpMode;

    Button btnSignUp;

    boolean signUpModeActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignUp = findViewById(R.id.btnSignUp);

        tvChangeSignUpMode = findViewById(R.id.tvChangeSignUpMode);
        tvChangeSignUpMode.setOnClickListener(this);

    }

    public void signUp(View view) {
        EditText username = findViewById(R.id.edtUsername);
        EditText password = findViewById(R.id.edtPassword);

        if (username.getText().toString().matches("") || password.getText().toString().matches("")) {
            Toast.makeText(this, "A username and password are required", Toast.LENGTH_LONG).show();
        } else {
            if (signUpModeActive) {
                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("sign up", "successful");
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Log.d("log in", "successful");
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvChangeSignUpMode) {
            if (signUpModeActive) {
                btnSignUp.setText("Log In");
                signUpModeActive = false;
                tvChangeSignUpMode.setText("or Sign Up");
            } else {
                btnSignUp.setText("Sign Up");
                signUpModeActive = true;
                tvChangeSignUpMode.setText("or Log In");
            }
        }
    }
}
