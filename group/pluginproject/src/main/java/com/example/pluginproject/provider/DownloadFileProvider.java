package com.example.pluginproject.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.example.pluginproject.util.ContextUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class DownloadFileProvider extends ContentProvider {

    private static final String AUTHORITY = "com.baidu.edge.agent.DOWNLOAD";
    private static final String BASE_PATH = "files";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final int DOWNLOADS = 1;
    private static final int DOWNLOAD_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, DOWNLOADS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", DOWNLOAD_ID);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "name", "path"});
        Context context = ContextUtils.getInstance().getContext();
        if (context == null) return cursor;

        File downloadDir = context.getFilesDir();
        File[] files = downloadDir.listFiles();
        if (files != null) {
            for (File file : files) {
                cursor.addRow(new Object[]{file.hashCode(), file.getName(), file.getAbsolutePath()});
            }
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case DOWNLOADS:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + BASE_PATH;
            case DOWNLOAD_ID:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + "." + BASE_PATH;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        Context context = getContext();
        if (context == null) throw new FileNotFoundException("Context is null");

        File file = new File(context.getFilesDir(), uri.getLastPathSegment());
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + uri.getLastPathSegment());
        }

        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
    }
}
