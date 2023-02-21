package model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.nandanarafiardika.notes.MainActivity;
import com.nandanarafiardika.notes.Users;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "notes_app_3";
    private static final int DATABASE_VERSION = 1;
    private static final String NOTES_TABLE_STD = "notes";
    private static final String USERS_TABLE_STD = "users";

    private static final String NOTES_ID = "id";
    private static final String NOTES_TITLE = "title";
    private static final String NOTES_CONTENT = "content";
    private static final String NOTES_OWNER = "owner";
    private static final String NOTES_PASSWORD = "password";
    private static final String CREATE_TABLE_NOTES = "CREATE TABLE " + NOTES_TABLE_STD + "(" + NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTES_TITLE + " TEXT, " + NOTES_CONTENT + " TEXT, " + NOTES_OWNER + " TEXT, " + NOTES_PASSWORD + " TEXT);";

    private static final String USERS_ID = "id";
    private static final String USERS_NAME = "username";
    private static final String USERS_PASSWORD = "password";
    private static final String USERS_REMEMBER = "rememberme";
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + USERS_TABLE_STD + "(" + USERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERS_NAME + " TEXT, " + USERS_PASSWORD + " TEXT, " + USERS_REMEMBER + " TEXT);";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTES);
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + NOTES_TABLE_STD + "'");
        db.execSQL("DROP TABLE IF EXISTS'" + USERS_TABLE_STD + "'");
        onCreate(db);
    }

    public long addNotes(String title, String content, String owner, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTES_TITLE, title);
        values.put(NOTES_CONTENT, content);
        values.put(NOTES_OWNER, owner);
        values.put(NOTES_PASSWORD, password);

        long insert = db.insert(NOTES_TABLE_STD, null, values);
        return insert;
    }
    public long addUser(String name, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USERS_NAME, name);
        values.put(USERS_PASSWORD, password);

        long insert = db.insert(USERS_TABLE_STD, null, values);
        return insert;
    }

    public ArrayList<Notes> getAllNotes(String owner){
        ArrayList<Notes> userModelArrayList = new ArrayList<Notes>();
        String Query = "SELECT * FROM " + NOTES_TABLE_STD + " WHERE " + NOTES_OWNER + " = '" + owner + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(Query, null);

        if(c.moveToFirst()){
            do{
                Notes std = new Notes();
                std.setId(c.getInt(c.getColumnIndex(NOTES_ID)));
                std.setTitle(c.getString(c.getColumnIndex(NOTES_TITLE)));
                std.setContent(c.getString(c.getColumnIndex(NOTES_CONTENT)));
                std.setOwner(c.getString(c.getColumnIndex(NOTES_OWNER)));
                std.setPass(c.getString(c.getColumnIndex(NOTES_PASSWORD)));

                userModelArrayList.add(std);
            }
            while(c.moveToNext());
        }
        return userModelArrayList;
    }

    public void deleteNotes(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTES_TABLE_STD, NOTES_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void deleteUser(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USERS_TABLE_STD, USERS_NAME + " = ?",
                new String[]{username});
        db.delete(NOTES_TABLE_STD, NOTES_OWNER + " = ?",
                new String[]{username});
    }

    public int updateNotes(int id, String Title, String Content){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTES_TITLE, Title);
        values.put(NOTES_CONTENT, Content);

        return db.update(NOTES_TABLE_STD, values, NOTES_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public int updateUser(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USERS_PASSWORD, password);

        return db.update(USERS_TABLE_STD, values, USERS_NAME + " = ?", new String[]{username});
    }

    public boolean checkUser(String username, String password){
        String Query = "SELECT * FROM " + USERS_TABLE_STD + " WHERE " + USERS_NAME + " = '" + username + "' AND " + USERS_PASSWORD + " = '" + password + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(Query, null);
        boolean loggedIn = false;

        c.moveToFirst();
        if(c.getCount() > 0){
            loggedIn = true;
        }
        return loggedIn;
    }

    public boolean checkSignup(String username){
        String Query = "SELECT * FROM " + USERS_TABLE_STD + " WHERE " + USERS_NAME + " = '" + username + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(Query, null);
        boolean available = false;

        c.moveToFirst();
        if(c.getCount() == 0){
            available = true;
        }
        return available;
    }
}
