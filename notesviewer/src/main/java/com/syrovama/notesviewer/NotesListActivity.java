package com.syrovama.notesviewer;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import static com.syrovama.notesviewer.Constants.*;
import java.util.ArrayList;

public class NotesListActivity extends AppCompatActivity {
    private static final String TAG = "NotesListActivity";
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NotesAdapter mAdapter;
    private int mSelectedPosition;


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
        mAdapter.setOnItemClickListener(new NotesAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                openNote(position);
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_note_menu_item:
                Intent intent = new Intent(this, AddNoteActivity.class);
                startActivityForResult(intent, REQUEST_ADD);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openNote(int position) {
        Log.d(TAG, "openNote called");
        mSelectedPosition = position;
        Note note = mNotes.get(position);
        startActivityForResult(getUpdateNoteIntent(note), REQUEST_OPEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_OPEN) {
            if (data.getBooleanExtra(EXTRA_DELETED, false)) {
                mNotes.remove(mSelectedPosition);
                Log.d(TAG, "Note deleted");
            } else {
                mNotes.set(mSelectedPosition, getNoteFromIntent(data));
                Log.d(TAG, "Note updated");
            }
        } else if (requestCode == REQUEST_ADD) {
            mNotes.add(getNoteFromIntent(data));
            Log.d(TAG, "Note added");
        }
        mAdapter.notifyDataSetChanged();
    }

    public Intent getUpdateNoteIntent(Note note) {
        Intent intent = new Intent(this, UpdateNoteActivity.class);
        intent.putExtra(EXTRA_ID, note.getId());
        intent.putExtra(EXTRA_TITLE, note.getTitle());
        intent.putExtra(EXTRA_CONTENT, note.getContent());
        return intent;
    }

    private Note getNoteFromIntent(Intent intent) {
        Note note = new Note();
        note.setId(intent.getLongExtra(EXTRA_ID, 0));
        note.setTitle(intent.getStringExtra(EXTRA_TITLE));
        note.setContent(intent.getStringExtra(EXTRA_CONTENT));
        return note;
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

