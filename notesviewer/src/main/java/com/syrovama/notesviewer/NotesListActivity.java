package com.syrovama.notesviewer;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import java.util.ArrayList;

public class NotesListActivity extends AppCompatActivity {
    private static final String TAG = "NotesListActivity";
    static final String AUTHORITY = "com.syrovama.notestask";
    static final String PATH = "note";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_ID = "_id";

    private ArrayList<Note> mNotes = new ArrayList<>();
    private NotesAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        initNotes();
        initRecycler();
    }

    private void initRecycler() {
        Log.d(TAG, "initRecycler");
        RecyclerView recyclerView = findViewById(R.id.notes_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new NotesAdapter(mNotes);
        recyclerView.setAdapter(mAdapter);
    }

    public void initNotes() {
        Log.d(TAG, "initNotes");
        NoteCursor noteCursor = null;
        try {
            noteCursor = new NoteCursor(getContentResolver()
                    .query(CONTENT_URI, null, null, null, null));
            Log.d(TAG, "1");
            while (noteCursor.moveToNext()) {
                mNotes.add(noteCursor.getNote());
                Log.d(TAG, "2");
            }
            Log.d(TAG, "Success");
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (noteCursor != null) {
                noteCursor.close();
            }
        }
    }

    public static class NoteCursor extends CursorWrapper {

        NoteCursor(Cursor c) {
            super(c);
        }

        Note getNote() {
            Note note = new Note();
            note.setId(getLong(getColumnIndex(COLUMN_ID)));
            note.setTitle(getString(getColumnIndex(COLUMN_TITLE)));
            note.setContent(getString(getColumnIndex(COLUMN_CONTENT)));
            return note;
        }
    }

}

