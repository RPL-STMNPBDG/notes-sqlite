package com.nandanarafiardika.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import model.DbHelper;

public class SignupActivity extends AppCompatActivity {
    DbHelper dbHelper;
    private EditText username, password;
    private Button signup_button;
    private TextView signup_fail, back_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        dbHelper = new DbHelper(this);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signup_button = findViewById(R.id.signup_button);
        signup_fail = findViewById(R.id.signup_fail);
        back_button = findViewById(R.id.back_button);

        signup_button.setOnClickListener(this::signup);
        back_button.setOnClickListener(view -> {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            finish();
        });

    }
    private void signup(View view){
        if(username.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill the username", Toast.LENGTH_SHORT).show();
        }
        else if(password.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill the password", Toast.LENGTH_SHORT).show();
        }
        else{
            if(dbHelper.checkSignup(username.getText().toString())){
                dbHelper.addUser(username.getText().toString(), password.getText().toString());
                Intent main = new Intent(this, NotesListActivity.class);
                main.putExtra("currentUser", username.getText().toString());
                main.putExtra("password", password.getText().toString());
                startActivity(main);
                finish();
            }
            else{
                signup_fail.setVisibility(View.VISIBLE);
            }
        }
    }
}