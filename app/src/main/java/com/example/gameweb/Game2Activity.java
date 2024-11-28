package com.example.gameweb;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class Game2Activity extends AppCompatActivity {

    private static final String GAME2_URL = "https://gam2hostingtask.web.app/Three%20Dots%20Game.zip"; // Replace with your Firebase URL
    private static final String GAME2_ZIP_NAME = "Three Dots Game.zip";
    private static final String GAME2_FOLDER_NAME = "Three Dots Game";
    private LocalServer localServer;

    private Button downloadButton;
    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game2);

        downloadButton = findViewById(R.id.download_button);
        webView = findViewById(R.id.webview_game2);

        // Configure WebView settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());

        // Check if the game files are already extracted
        File gameFolder = new File(getExternalFilesDir(null), GAME2_FOLDER_NAME);
        if (gameFolder.exists() && gameFolder.isDirectory()) {
            downloadButton.setVisibility(View.GONE);
            loadGame();
        }

        // Handle download button click
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadGameZip();
            }
        });
    }

    private void downloadGameZip() {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(GAME2_URL);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        // Save the file in the app's external files directory
        request.setDestinationInExternalFilesDir(this, null, GAME2_ZIP_NAME);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        long downloadId = downloadManager.enqueue(request);

        // Monitor download progress
        new Thread(() -> {
            boolean downloading = true;
            while (downloading) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                Cursor cursor = downloadManager.query(query);
                if (cursor != null && cursor.moveToFirst()) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                        runOnUiThread(() -> {
                            Toast.makeText(Game2Activity.this, "Download complete", Toast.LENGTH_SHORT).show();
                            extractGameZip();
                        });
                    }
                }
                if (cursor != null) cursor.close();
            }
        }).start();
    }

    private void extractGameZip() {
        try {
            File zipFile = new File(getExternalFilesDir(null), GAME2_ZIP_NAME);
            File destinationFolder = new File(getExternalFilesDir(null), GAME2_FOLDER_NAME);

            Log.d("Game2Activity", "ZIP File Path: " + zipFile.getAbsolutePath());
            Log.d("Game2Activity", "Extraction Path: " + destinationFolder.getAbsolutePath());

            // Use ZipUtils to extract the ZIP file
            ZipUtils.unzip(zipFile, destinationFolder); // Implement this utility method

            // Check for nested folder
            File nestedFolder = new File(destinationFolder, GAME2_FOLDER_NAME);
            if (nestedFolder.exists() && nestedFolder.isDirectory()) {
                // Move contents of the nested folder to the parent folder
                for (File file : nestedFolder.listFiles()) {
                    file.renameTo(new File(destinationFolder, file.getName()));
                }
                // Delete the now-empty nested folder
                nestedFolder.delete();
            }

            zipFile.delete(); // Delete the ZIP file after extraction
            downloadButton.setVisibility(View.GONE);
            loadGame();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to extract files", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadGame() {
        try {
            File gameFolder = new File(getExternalFilesDir(null), GAME2_FOLDER_NAME);
            File nestedFolder = new File(gameFolder, GAME2_FOLDER_NAME);

            // Handle nested subfolder issue
            if (nestedFolder.exists() && nestedFolder.isDirectory()) {
                for (File file : nestedFolder.listFiles()) {
                    file.renameTo(new File(gameFolder, file.getName()));
                }
                nestedFolder.delete(); // Remove the empty subfolder
            }

            File indexFile = new File(gameFolder, "index.html");
            if (indexFile.exists()) {
                // Start a local server to serve Game 2 files
                localServer = new LocalServer(8082, gameFolder.getAbsolutePath());
                localServer.start();

                // Load the game via HTTP
                String gameUrl = "http://localhost:8082/index.html";
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(gameUrl);
            } else {
                Toast.makeText(this, "Game files are missing or corrupted", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to start local server", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (localServer != null) {
            localServer.stop();
        }
    }

}