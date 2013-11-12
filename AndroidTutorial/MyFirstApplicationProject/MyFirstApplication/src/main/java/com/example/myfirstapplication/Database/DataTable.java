package com.example.myfirstapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myfirstapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ian on 11/12/13.
 */
public class DataTable extends SQLiteOpenHelper{

    /**
     * Constructor
     *
     * @param _context
     */
    public DataTable(Context _context){
        super(_context, DATABASE_NAME, null, DATABASE_VERSION);
        PREF = _context.getSharedPreferences(_context.getString(R.string.pref_file_string),
                                                Context.MODE_PRIVATE);
        CONTEXT = _context;
    }

    /**
     * onCreate Event
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //-- Create Table
        try{
            db.execSQL(DATA_TABLE_CREATE_QUERY);
        }
        catch(Exception err){
            err.printStackTrace();
        }
    }

    /**
     * onUpgrade Event
     *
     * @param db
     * @param i
     * @param i2
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        //-- Jika Upgrade
        try{
            db.execSQL("DROP TABLE IF EXISTS " + DATA_TABLE_NAME);
            this.onCreate(db);
        }
        catch(Exception err){
            err.printStackTrace();
        }
    }

    /**
     * Masukkan Comment ke Database
     *
     * @param _message
     */
    public void createNewComment(String _message){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("MESSAGE", _message);

        try {
            long newCommentID = db.insert(DATA_TABLE_NAME, null, values);

            if (newCommentID >= 0){
                //-- Insert Sukses
                Log.d("DATABASE HELPER", "New Comment Has Been Inserted With ID: " + newCommentID);

                //-- Masukkan pada SharedPreferences lagi
                SharedPreferences.Editor editor = PREF.edit();
                editor.putString(CONTEXT.getString(R.string.pref_file_message_key),
                                    String.valueOf(newCommentID));

                editor.commit();

                Log.d("DATABASE HELPER", "New Comment Has Been Inserted Into SharedPref");
            }
        }
        catch(Exception err){
            err.printStackTrace();
        }
    }

    /**
     * Get Message Comment based on Message ID
     *
     * @param _messageID
     * @return
     */
    public String getMessageComment(int _messageID){
        String message = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + DATA_TABLE_NAME +
                        " WHERE " + DATA_TABLE_COLUMN_ID + " = " + String.valueOf(_messageID);

        Log.d("DATABASE SELECT QUERY", query);

        try{
            Cursor cur = db.rawQuery(query, null);

            if (cur != null){
                cur.moveToFirst();
            }

            message = cur.getString(cur.getColumnIndex(DATA_TABLE_COLUMN_MESSAGE));
        }
        catch (Exception err){
            err.printStackTrace();
        }

        return message;
    }

    /**
     * Get All The Message
     *
     * @return
     */
    public List<String> getAllMessage(){
        List<String> allMessage = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + DATA_TABLE_NAME;

        try{
            Cursor cur = db.rawQuery(query, null);

            if (cur.moveToFirst()){
                do{
                    allMessage.add(cur.getString(cur.getColumnIndex(DATA_TABLE_COLUMN_MESSAGE)));
                } while (cur.moveToNext());
            }
        }
        catch (Exception err){
            err.printStackTrace();
        }

        return allMessage;
    }

    /*
     *****************************
     * Variable Declaration Area *
     *****************************
     */

    public static final String DATABASE_NAME = "testing.db";
    public static final int DATABASE_VERSION = 1;

    public static final String
                            DATA_TABLE_NAME = "COMMENT",
                            DATA_TABLE_COLUMN_ID = "ID",
                            DATA_TABLE_COLUMN_MESSAGE = "MESSAGE";

    public static final String DATA_TABLE_CREATE_QUERY =
                                "CREATE TABLE " + DATA_TABLE_NAME +
                                " (" +
                                DATA_TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                DATA_TABLE_COLUMN_MESSAGE + " TEXT NOT NULL" +
                                ");";

    private SharedPreferences PREF;
    private Context CONTEXT;
}
