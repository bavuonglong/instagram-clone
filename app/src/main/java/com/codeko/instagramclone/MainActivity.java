package com.codeko.instagramclone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    TextView tvChangeSignUpMode;
    Button btnSignUp;
    EditText username;
    EditText password;
    RelativeLayout relativeLayout;
    ImageView imgLogo;

    boolean signUpModeActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignUp = findViewById(R.id.btnSignUp);
        username = findViewById(R.id.edtUsername);

        password = findViewById(R.id.edtPassword);
        password.setOnKeyListener(this);

        tvChangeSignUpMode = findViewById(R.id.tvChangeSignUpMode);
        tvChangeSignUpMode.setOnClickListener(this);

        relativeLayout = findViewById(R.id.backgroundRelativeLayout);
        relativeLayout.setOnClickListener(this);

        imgLogo = findViewById(R.id.imgLogo);
        imgLogo.setOnClickListener(this);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void signUp(View view) {
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
        switch (view.getId()) {
            case R.id.tvChangeSignUpMode:
                if (signUpModeActive) {
                    btnSignUp.setText("Log In");
                    signUpModeActive = false;
                    tvChangeSignUpMode.setText("or Sign Up");
                } else {
                    btnSignUp.setText("Sign Up");
                    signUpModeActive = true;
                    tvChangeSignUpMode.setText("or Log In");
                }
                break;
            case R.id.imgLogo:
            case R.id.backgroundRelativeLayout:
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                break;
            default:

        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            signUp(view);
        }
        return false;
    }
}
