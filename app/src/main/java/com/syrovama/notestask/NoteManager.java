package com.syrovama.notestask;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class NoteManager {
    private static final String TAG = "MyNoteManager";
    private static NoteManager sNoteManager;
    private DBHelper mDBHelper;

    private NoteManager(Context context) {
        mDBHelper = new DBHelper(context);
    }

    public static NoteManager getInstance(Context c) {
        if (sNoteManager == null) {
            sNoteManager = new NoteManager(c.getApplicationContext());
        }
        return sNoteManager;
    }

    public long createNote(Note note) {
        long id = mDBHelper.insertNote(note);
        Log.d(TAG, "New note id " + id);
        return id;
    }

    public void updateNote(Note note) {
        mDBHelper.updateNote(note);
    }

    public void deleteNote(long id) {
        mDBHelper.deleteNote(id);
    }

    public ArrayList<Note> getNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        DBHelper.NoteCursor cursor = mDBHelper.queryNotes();
        if (cursor == null) return null;
        while(cursor.moveToNext()) {
            notes.add(cursor.getNote());
        }
        cursor.close();
        return notes;
    }

    public void closeDBConnection() {
        mDBHelper.close();
    }
}
