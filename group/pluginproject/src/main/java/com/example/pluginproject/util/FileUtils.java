package com.example.pluginproject.util;

import android.content.Context;

import com.example.pluginproject.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    private static void copyAssetsFileToPrivateDirectory(Context context, String fileName, String destinationFileName) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            File privateDirectory = context.getFilesDir();
            File outputFile = new File(privateDirectory, destinationFileName);

            OutputStream outputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 移动asserts文件到私有目录files下
     * @param context
     * @param fileName
     * @param destinationFileName
     */
    public static void copyAssertsFiles(Context context, String fileName, String destinationFileName) {
        copyAssetsFileToPrivateDirectory(context, fileName, destinationFileName);
    }
}
