package com.syrovama.notestask;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class NoteProvider extends ContentProvider {
    static final String AUTHORITY = "com.syrovama.notestask";
    static final String PATH = "note";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + PATH;
    static final int URI_NOTES = 1;
    static final int URI_NOTES_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH, URI_NOTES);
        uriMatcher.addURI(AUTHORITY, PATH + "/#", URI_NOTES_ID);
    }

    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return dbHelper.queryNotes();
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        if (uriMatcher.match(uri) != URI_NOTES) return null;
        return CONTENT_TYPE;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if ((uriMatcher.match(uri) != URI_NOTES)||(values == null)) return null;
        long id = dbHelper.insert(values);
        if (id == 0) return null;
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (uriMatcher.match(uri) != URI_NOTES_ID) return 0;
        String idString = uri.getLastPathSegment();
        long id = Long.parseLong(idString);
        dbHelper.deleteNote(id);
        return 1;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        if ((uriMatcher.match(uri) != URI_NOTES_ID)||(values == null)) return 0;
        String idString = uri.getLastPathSegment();
        long id = Long.parseLong(idString);
        dbHelper.update(id, values);
        return 1;
    }
}
