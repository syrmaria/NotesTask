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


public class AddNoteActivity extends AppCompatActivity {
    private EditText mTitleEditText;
    private EditText mContentEditText;
    private Note mNote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        mTitleEditText = findViewById(R.id.add_title);
        mContentEditText = findViewById(R.id.add_content);
        Button saveButton = findViewById(R.id.add_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
    }

    private void saveNote() {
        mNote = new Note();
        mNote.setTitle(mTitleEditText.getText().toString());
        mNote.setContent(mContentEditText.getText().toString());

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, mNote.getTitle());
        cv.put(COLUMN_CONTENT, mNote.getContent());
        Uri uri = getContentResolver().insert(CONTENT_URI, cv);
        if (uri != null) {
            mNote.setId(Long.parseLong(uri.getLastPathSegment()));
            returnResult();
        } else Toast.makeText(this, "Not saved, try again later", Toast.LENGTH_LONG)
                .show();
    }

    private void returnResult() {
        Intent data = new Intent();
        data.putExtra(EXTRA_ID, mNote.getId());
        data.putExtra(EXTRA_TITLE, mNote.getTitle());
        data.putExtra(EXTRA_CONTENT, mNote.getContent());
        setResult(RESULT_OK, data);
        finish();
    }

}
