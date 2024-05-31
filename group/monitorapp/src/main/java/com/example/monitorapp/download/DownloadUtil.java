package com.example.monitorapp.download;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadUtil {
    public static final String AUTHORITY = "com.baidu.edge.agent.DOWNLOAD";
    public static final String BASE_PATH = "files";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public void downloadFile(Context context, Uri uri, String outputFileName) {
        ContentResolver contentResolver = context.getContentResolver();
        try (ParcelFileDescriptor pfd = contentResolver.openFileDescriptor(uri, "r");
             FileInputStream inputStream = new FileInputStream(pfd.getFileDescriptor());
             FileOutputStream outputStream = new FileOutputStream(new File(context.getFilesDir(), outputFileName))) {
            copyStream(inputStream, outputStream);
            Log.d("DownloadFile", "File downloaded successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DownloadFile", "File download failed.");
        }
    }

    private void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
    }
}
