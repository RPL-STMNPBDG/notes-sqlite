package com.nandanarafiardika.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import model.DbHelper;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    TextView login_fail, signup_button;
    Button login_button;

    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login_button = findViewById(R.id.login_button);
        signup_button = findViewById(R.id.signup_button);
        login_fail = findViewById(R.id.login_fail);

        dbHelper = new DbHelper(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null){
            String fail = extras.getString("login_fail");
            if(fail.equals("wrong_username_or_password")){
                login_fail.setVisibility(View.VISIBLE);
            }
        }

        login_button.setOnClickListener(this::login);

        signup_button.setOnClickListener(view -> {
            Intent signup = new Intent(this, SignupActivity.class);
            startActivity(signup);
        });

    }
    private void login(View view){
        if(username.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill the username", Toast.LENGTH_SHORT).show();
        }
        else if(password.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill the password", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent main = new Intent(this, NotesListActivity.class);
            main.putExtra("currentUser", username.getText().toString());
            main.putExtra("password", password.getText().toString());
            startActivity(main);
            finish();
        }
    }
}