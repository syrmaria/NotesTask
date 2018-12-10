package com.syrovama.notesviewer;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.syrovama.notesviewer.Constants.*;

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

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, mNote.getTitle());
        cv.put(COLUMN_CONTENT, mNote.getContent());
        Uri uri = Uri.parse(CONTENT_URI + "/" + mNote.getId());
        int result = getContentResolver().update(uri, cv, null, null);
        if (result == 1) returnResult(false);
        else Toast.makeText(this, "Not saved, try again later", Toast.LENGTH_LONG)
                .show();
    }

    private void deleteNote() {
        Uri uri = Uri.parse(CONTENT_URI + "/" + mNote.getId());
        int result = getContentResolver().delete(uri,null, null);
        if (result == 1) returnResult(true);
        else Toast.makeText(this, "Not deleted, try again later", Toast.LENGTH_LONG)
                .show();
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

