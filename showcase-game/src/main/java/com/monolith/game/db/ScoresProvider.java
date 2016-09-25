package com.monolith.game.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import com.monolith.game.BuildConfig;

public class ScoresProvider extends ContentProvider {

    private DbHelper mDbHelper;

    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".scores";

    @StringDef({
            BASE_PATH_SCORES,
    })
    public @interface BasePaths {
    }

    public static final String BASE_PATH_SCORES = "scores";

    private static final int ID_SCORES = 1;
    private static final int ID_SPECIFIC_SCORE = 2;

    private static final String URI_PREFIX = "content://";
    private static final String URI_SEPARATOR = "/";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, BASE_PATH_SCORES, ID_SCORES);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH_SCORES + URI_SEPARATOR + "#", ID_SPECIFIC_SCORE);
    }

    public static Uri getUri(@BasePaths String basePath) {
        return Uri.parse(URI_PREFIX + AUTHORITY + URI_SEPARATOR + basePath);
    }

    public static Uri getUri(@BasePaths String basePath, long id) {
        return Uri.withAppendedPath(getUri(basePath), Long.valueOf(id).toString());
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        UriMatchResult match = matchUri(uri);

        if(match.id != null) {
            if(!TextUtils.isEmpty(selection)) {
                selection = BaseColumns._ID + "=? AND (" + selection + " )";
            } else {
                selection = BaseColumns._ID + "=?";
            }
            if(selectionArgs != null) {
                String[] oldSelectionArgs = selectionArgs;
                selectionArgs = new String[oldSelectionArgs.length + 1];
                selectionArgs[0] = match.id;
                for(int i = 0; i < oldSelectionArgs.length; ++i) {
                    selectionArgs[i + 1] = oldSelectionArgs[i];
                }
            } else {
                selectionArgs = new String[]{match.id};
            }
        }
        return mDbHelper.getReadableDatabase().query(match.tableName, projection, selection, selectionArgs, null, null, sortOrder);
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        UriMatchResult match = matchUri(uri);
        if (match.id != null) {
            values.put(BaseColumns._ID, match.id);
        }
        long insertRes = mDbHelper.getWritableDatabase().insertWithOnConflict(match.tableName, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if (match.id != null) {
            return uri;
        } else {
            return Uri.withAppendedPath(uri, Long.toString(insertRes));
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        UriMatchResult match = matchUri(uri);
        if (match.id == null) {
            return mDbHelper.getWritableDatabase().delete(match.tableName, selection, selectionArgs);
        } else {
            return mDbHelper.getWritableDatabase().delete(match.tableName, BaseColumns._ID + "=?", new String[]{match.id});
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        UriMatchResult match = matchUri(uri);
        if(match.id == null) {
            return mDbHelper.getWritableDatabase().update(match.tableName, values, selection, selectionArgs);
        } else {
            return mDbHelper.getWritableDatabase().update(match.tableName, values, BaseColumns._ID + "=?", new String[]{match.id});
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**
     * Class representing result of uri matching.
     * id is null if uri is not specific, otherwise it's a value of uri id.
     */
    private static class UriMatchResult {
        public String basePath;
        public String tableName;
        public String id;
    }

    private UriMatchResult matchUri(Uri uri) {
        UriMatchResult res = new UriMatchResult();
        switch (sUriMatcher.match(uri)) {
            case ID_SCORES:
                res.basePath = BASE_PATH_SCORES;
                res.tableName = DbContract.Scores.TABLE_NAME;
                break;
            case ID_SPECIFIC_SCORE:
                res.basePath = BASE_PATH_SCORES;
                res.tableName = DbContract.Scores.TABLE_NAME;
                res.id = uri.getLastPathSegment();
                break;
        }
        return res;
    }
}
