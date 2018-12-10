package com.syrovama.notesviewer;

import android.net.Uri;

public class Constants {
    static final String COLUMN_TITLE = "title";
    static final String COLUMN_CONTENT = "content";
    static final String COLUMN_ID = "_id";
    static final String AUTHORITY = "com.syrovama.notestask";
    static final String PATH = "note";
    static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
    static final String EXTRA_ID = "id";
    static final String EXTRA_TITLE = "title";
    static final String EXTRA_CONTENT = "content";
    static final String EXTRA_DELETED = "isDeleted";
    static final int REQUEST_ADD = 1;
    static final int REQUEST_OPEN = 2;

}
