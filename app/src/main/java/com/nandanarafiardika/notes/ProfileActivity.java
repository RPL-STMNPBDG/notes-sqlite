package com.nandanarafiardika.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import model.DbHelper;

public class ProfileActivity extends AppCompatActivity {
    ImageView user_delete;
    EditText username, password_old, password_new;
    Button edit_button;
    TextView profile_edit_fail;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DbHelper(this);
        Bundle extras = getIntent().getExtras();
        String getUser = extras.getString("username");
        String getPass = extras.getString("password");

        user_delete = findViewById(R.id.user_delete);
        username = findViewById(R.id.username);
        password_old = findViewById(R.id.password_old);
        password_new = findViewById(R.id.password_new);
        edit_button = findViewById(R.id.edit_button);
        profile_edit_fail = findViewById(R.id.profile_edit_fail);

        username.setText(getUser);
        username.setEnabled(false);

        edit_button.setOnClickListener(view -> {
            if(password_old.getText().toString().isEmpty() || password_new.getText().toString().isEmpty()){
                Toast.makeText(this, "Please fill the password fields", Toast.LENGTH_SHORT).show();
            }
            else{
                if(password_old.getText().toString().equals(getPass)){
                    dbHelper.updateUser(getUser, password_new.getText().toString());

                    Intent logout = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(logout);
                    finish();
                }
                else{
                    profile_edit_fail.setVisibility(View.VISIBLE);
                }
            }
        });

        user_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_dialog();
            }
            public void delete_dialog(){
                AlertDialog.Builder delete = new AlertDialog.Builder(ProfileActivity.this);
                delete.setTitle("Delete Profile");
                delete.setMessage("This action can't be undone");
                delete.setPositiveButton("Yes", (dialogInterface1, i1) -> {
                    dbHelper.deleteUser(getUser);
                    Intent logout = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(logout);
                    finish();
                });
                delete.setNegativeButton("No", (dialogInterface12, i12) -> dialogInterface12.dismiss());
                AlertDialog alert = delete.create();
                alert.show();
            }
        });
    }
}