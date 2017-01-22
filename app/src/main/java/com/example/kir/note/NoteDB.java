package com.example.kir.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import com.example.kir.note.recycler_builder.NotePreview;

import java.util.List;

public class NoteDB extends SQLiteOpenHelper {

    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "notesDB";
    public static final String TABLE_NOTES = "notes";

    public static final String KEY_ID = "_id";
    public static final String KEY_HEADER = "header";
    public static final String KEY_TEXT = "text";
    public static final String KEY_DATA = "data";


    public NoteDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NOTES + "(" + KEY_ID
                + " integer primary key," + KEY_HEADER + " text," + KEY_TEXT + " text," + KEY_DATA + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NOTES);

        onCreate(db);

    }

    public static void readFromDB(View v, List<NotePreview> list) {
        SQLiteDatabase database;
        NoteDB savedNotes = new NoteDB(v.getContext());
        database = savedNotes.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NOTES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(KEY_ID);
            int headerIndex = cursor.getColumnIndex(KEY_HEADER);
            int textIndex = cursor.getColumnIndex(KEY_TEXT);
            int dataIndex = cursor.getColumnIndex(KEY_DATA);
            do {
                list.add(new NotePreview(cursor.getString(idIndex), cursor.getString(headerIndex), cursor.getString(textIndex), cursor.getString(dataIndex)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        savedNotes.close();
    }


    public static void saveToDB(View v, String header, String text, String formattedDate) {
        SQLiteDatabase database;
        NoteDB savedNotes = new NoteDB(v.getContext());
        database = savedNotes.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (text.length() > 0) contentValues.put(NoteDB.KEY_TEXT, text);

        if (text.length() == 0) contentValues.put(NoteDB.KEY_TEXT, " ");

        if (header.length() > 0) contentValues.put(NoteDB.KEY_HEADER, header);

        if (header.length() == 0) contentValues.put(NoteDB.KEY_HEADER, " ");

            contentValues.put(NoteDB.KEY_DATA, formattedDate);
            database.insert(NoteDB.TABLE_NOTES, null, contentValues);

        savedNotes.close();
    }

    public static void updateDB(View v, String id, String header, String text, String formattedDate) {
        SQLiteDatabase database;
        NoteDB savedNotes = new NoteDB(v.getContext());
        database = savedNotes.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if (text.length() > 0) contentValues.put(NoteDB.KEY_TEXT, text);

        if (text.length() == 0) contentValues.put(NoteDB.KEY_TEXT, " ");

        if (header.length() > 0) contentValues.put(NoteDB.KEY_HEADER, header);

        if (header.length() == 0) contentValues.put(NoteDB.KEY_HEADER, " ");

            contentValues.put(NoteDB.KEY_DATA, formattedDate);
            database.update(NoteDB.TABLE_NOTES, contentValues, NoteDB.KEY_ID + "= ?", new String[] {id});

        savedNotes.close();
    }


}