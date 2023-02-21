package com.nandanarafiardika.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import adapter.NotesAdapter;
import model.DbHelper;
import model.Notes;

public class NotesListActivity extends AppCompatActivity {
    FloatingActionButton add_button;
    ImageView options_button;
    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private ArrayList<Notes> NotesArrayList;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        add_button = findViewById(R.id.add_button);
        options_button = findViewById(R.id.options_button);
        recyclerView = findViewById(R.id.recyclerview);

        adapter = new NotesAdapter(this);
        dbHelper = new DbHelper(this);
        Bundle extras = getIntent().getExtras();
        String getUser = extras.getString("currentUser");
        String getPass = extras.getString("password");
        if(dbHelper.checkUser(getUser, getPass)){
            NotesArrayList = dbHelper.getAllNotes(getUser);
            adapter.setListNotes(NotesArrayList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(NotesListActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
        else{
            Intent fail = new Intent(this, LoginActivity.class);
            fail.putExtra("login_fail", "wrong_username_or_password");
            startActivity(fail);
            finish();
        }

        options_button.setOnClickListener(new View.OnClickListener() {
            final String[] options_list = {"Edit Profile", "Logout"};
            @Override
            public void onClick(View view) {
                show_options();
            }
            private void show_options(){
                AlertDialog.Builder options_dialog = new AlertDialog.Builder(NotesListActivity.this);
                options_dialog.setTitle("Options");
                options_dialog.setItems(options_list, (dialogInterface, i) -> {
                    AlertDialog.Builder logout_dialog = new AlertDialog.Builder(NotesListActivity.this);
                    switch(i){
                        case 0:
                            Intent editProfile = new Intent(NotesListActivity.this, ProfileActivity.class);
                            editProfile.putExtra("username", getUser);
                            editProfile.putExtra("password", getPass);
                            startActivity(editProfile);
                            break;
                        case 1:
                            logout_dialog.setTitle("Logout");
                            logout_dialog.setMessage("Are you sure?");
                            logout_dialog.setPositiveButton("Yes", (dialogInterface1, i1) -> {
                                Intent logout = new Intent(NotesListActivity.this, LoginActivity.class);
                                startActivity(logout);
                                finish();
                            });
                            logout_dialog.setNegativeButton("No", (dialogInterface12, i12) -> dialogInterface12.dismiss());
                            AlertDialog alert = logout_dialog.create();
                            alert.show();
                            break;
                    }
                });
                options_dialog.show();
            }
        });

        add_button.setOnClickListener(view -> {
            dbHelper.addNotes(null, null, getUser, getPass);
            Intent add = new Intent(this, NotesListActivity.class);
            add.putExtra("currentUser", getUser);
            add.putExtra("password", getPass);
            startActivity(add);
            finish();
        });

        /*@Override
        protected void onResume(){
            super.onResume();
            NotesArrayList = dbHelper.getAllNotes(getUser);
            adapter.setListNotes(NotesArrayList);
            adapter.notifyDataSetChanged();
        }*/

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}