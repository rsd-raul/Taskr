package com.software.achilles.tasked.util.providers;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.software.achilles.tasked.model.domain.Task;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class TasksSearchProvider extends ContentProvider {



    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Get the user input
        String query = uri.getLastPathSegment();

        // Set the cursor that will store all results
        MatrixCursor cursor = new MatrixCursor(
                new String[] {
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
                }
        );

        // Query the DB for the tasks that meet the desired criteria
        RealmResults<Task> tasks = Realm.getDefaultInstance().where(Task.class)
                .contains("title", query, Case.INSENSITIVE).or()
                .contains("notes", query, Case.INSENSITIVE).findAll();

        // Add those to the cursor and return it (or as much of those as possible).
        int size = tasks.size();
        int limit = Integer.parseInt(uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT));
        for (int i = 0; i < size && cursor.getCount() < limit; i++) {
            Task task = tasks.get(i);
            cursor.addRow(new Object[]{ task.getId(), task.getTitle(), task.getId()});
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
