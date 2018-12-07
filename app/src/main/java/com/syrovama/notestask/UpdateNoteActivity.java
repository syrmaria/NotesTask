package com.syrovama.notestask;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import static com.syrovama.notestask.NotesListActivity.EXTRA_CONTENT;
import static com.syrovama.notestask.NotesListActivity.EXTRA_DELETED;
import static com.syrovama.notestask.NotesListActivity.EXTRA_ID;
import static com.syrovama.notestask.NotesListActivity.EXTRA_TITLE;

public class UpdateNoteActivity extends AppCompatActivity {
    private EditText mTitleEditText;
    private EditText mContentEditText;
    private Note mNote = new Note();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_note);
        initViews();
        parseIntent();
    }

    private void initViews() {
        mTitleEditText = findViewById(R.id.title_edit_view);
        mContentEditText = findViewById(R.id.content_edit_view);
        Button updateButton = findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote();
            }
        });
        Button deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });
    }

    private void parseIntent() {
        Intent data = getIntent();
        mNote.setId(data.getLongExtra(EXTRA_ID, 0));
        mTitleEditText.setText(data.getStringExtra(EXTRA_TITLE));
        mContentEditText.setText(data.getStringExtra(EXTRA_CONTENT));
    }

    private void updateNote() {
        mNote.setTitle(mTitleEditText.getText().toString());
        mNote.setContent(mContentEditText.getText().toString());
        ((MyApplication)getApplication()).getDBHelper().updateNote(mNote);
        returnResult(false);
    }

    private void deleteNote() {
        ((MyApplication)getApplication()).getDBHelper().deleteNote(mNote.getId());
        returnResult(true);
    }

    private void returnResult(boolean isDeleted) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ID, mNote.getId());
        data.putExtra(EXTRA_TITLE, mNote.getTitle());
        data.putExtra(EXTRA_CONTENT, mNote.getContent());
        data.putExtra(EXTRA_DELETED, isDeleted);
        setResult(RESULT_OK, data);
        finish();
    }

}
