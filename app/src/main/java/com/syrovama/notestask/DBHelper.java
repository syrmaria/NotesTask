package com.syrovama.notestask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    private static final String DB_NAME = "notes.sqlite";
    private static final int VERSION = 1;
    private static final String TABLE_NOTE = "note";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_ID = "_id";

    DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table note (_id integer primary key autoincrement, title text, content text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long insertNote(Note note) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, note.getTitle());
        cv.put(COLUMN_CONTENT, note.getContent());
        long id = 0;
        try {
            id = getWritableDatabase().insert(TABLE_NOTE, null, cv);
        } catch (SQLException e) {
            Log.e(TAG, "Not able to insert note");
        }
        return id;
    }

    public void updateNote(Note note) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, note.getTitle());
        cv.put(COLUMN_CONTENT, note.getContent());
        try {
            getWritableDatabase().update(TABLE_NOTE, cv,
                    COLUMN_ID + "= ?", new String[]{String.valueOf(note.getId())});
        } catch (SQLException e) {
            Log.e(TAG, "Not able to update note");
        }
    }

    public void deleteNote(long noteId) {
        try {
            getWritableDatabase().delete(TABLE_NOTE, COLUMN_ID + "= ?",
                    new String[]{String.valueOf(noteId)});
        } catch (SQLException e) {
            Log.e(TAG, "Not able to delete note");
        }
    }

    public NoteCursor queryNotes() {
        try {
            Cursor cursor = getReadableDatabase().query(TABLE_NOTE, null, null,
                    null, null, null, null);
            return new NoteCursor(cursor);
        } catch (SQLException e) {
            Log.e(TAG, "Not able to query notes");
        }
        return null;
    }

    public static class NoteCursor extends CursorWrapper {

        NoteCursor(Cursor c) {
            super(c);
        }

        public Note getNote() {
            Note note = new Note();
            note.setId(getLong(getColumnIndex(COLUMN_ID)));
            note.setTitle(getString(getColumnIndex(COLUMN_TITLE)));
            note.setContent(getString(getColumnIndex(COLUMN_CONTENT)));
            return note;
        }
    }
}
