package com.example.gameweb;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AssetHelper {

    public static void copyAssets(Context context, String assetFolderName, File targetDir) {
        try {
            targetDir.mkdirs(); // Ensure the target directory exists
            String[] assetFiles = context.getAssets().list(assetFolderName);

            if (assetFiles != null && assetFiles.length > 0) {
                for (String fileName : assetFiles) {
                    File outFile = new File(targetDir, fileName);

                    try {
                        InputStream inputStream = context.getAssets().open(assetFolderName + "/" + fileName);
                        FileOutputStream outputStream = new FileOutputStream(outFile);

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }

                        outputStream.close();
                        inputStream.close();

                        Log.d("AssetHelper", "Copied file: " + outFile.getAbsolutePath());
                    } catch (Exception e) {
                        // Handle subdirectories
                        copyAssets(context, assetFolderName + "/" + fileName, outFile);
                    }
                }
            } else {
                Log.w("AssetHelper", "No files found in: " + assetFolderName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AssetHelper", "Failed to copy assets: " + e.getMessage());
        }
    }
}