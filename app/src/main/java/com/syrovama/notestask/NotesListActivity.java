package com.syrovama.notestask;

import android.content.Intent;
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
import java.util.ArrayList;

public class NotesListActivity extends AppCompatActivity {
    private static final String TAG = "NotesListActivity";
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_CONTENT = "content";
    public static final String EXTRA_DELETED = "isDeleted";
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_OPEN = 2;
    private ArrayList<Note> mNotes;
    private NotesAdapter mAdapter;
    private int mSelectedPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        RecyclerView recyclerView = findViewById(R.id.notes_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mNotes = NoteManager.getInstance(this).getNotes();
        mAdapter = new NotesAdapter(mNotes);
        mAdapter.setOnItemClickListener(new NotesAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                openNote(position);
            }
        });
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_list_menu, menu);
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

    @Override
    protected void onDestroy() {
        NoteManager.getInstance(this).closeDBConnection();
        Log.d(TAG, "DB closed");
        super.onDestroy();
    }

    private void openNote(int position) {
        Log.d(TAG, "openNote called");
        mSelectedPosition = position;
        Note note = mNotes.get(position);
        Intent intent = new Intent(this, UpdateNoteActivity.class);
        intent.putExtra(EXTRA_ID, note.getId());
        intent.putExtra(EXTRA_TITLE, note.getTitle());
        intent.putExtra(EXTRA_CONTENT, note.getContent());
        startActivityForResult(intent, REQUEST_OPEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (resultCode != RESULT_OK) return;
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

    private Note getNoteFromIntent(Intent intent) {
        Note note = new Note();
        note.setId(intent.getLongExtra(EXTRA_ID, 0));
        note.setTitle(intent.getStringExtra(EXTRA_TITLE));
        note.setContent(intent.getStringExtra(EXTRA_CONTENT));
        return note;
    }

}
