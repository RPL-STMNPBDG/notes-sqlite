package com.nandanarafiardika.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import model.DbHelper;
import model.Notes;

public class NotesViewActivity extends AppCompatActivity {
    private DbHelper dbHelper;
    private Notes notes, user_data;
    private EditText notes_title, notes_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view);

        notes_title = findViewById(R.id.notes_title);
        notes_content = findViewById(R.id.notes_content);

        notes = (Notes) getIntent().getSerializableExtra("notes_data");

        notes_title.setText(notes.getTitle());
        notes_content.setText(notes.getContent());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent list = new Intent(this, NotesListActivity.class);
        dbHelper = new DbHelper(this);
        dbHelper.updateNotes(notes.getId(), notes_title.getText().toString(), notes_content.getText().toString());
        if(!(notes_title.getText().toString().equals(notes.getTitle()) && notes_content.getText().toString().equals(notes.getContent()))){
            Toast.makeText(NotesViewActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        }
        user_data = (Notes) getIntent().getSerializableExtra("notes_data");
        list.putExtra("currentUser", user_data.getOwner());
        list.putExtra("password", user_data.getPass());
        startActivity(list);
        finish();
    }
}