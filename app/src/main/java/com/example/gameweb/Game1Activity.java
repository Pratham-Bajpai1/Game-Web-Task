package com.example.gameweb;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class Game1Activity extends AppCompatActivity {

    private LocalServer localServer;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game1);

        WebView webView = findViewById(R.id.webview_game1);

        // Enable JavaScript and other settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true); // Enable DOM storage if needed

        // Debugging and user-friendly behavior
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        // Get the path to the assets folder for "balloonescape"
        String baseDir = getFilesDir().getAbsolutePath() + "/balloonescape";

        // Copy assets to the internal storage for serving
        AssetHelper.copyAssets(this, "balloonescape", new File(baseDir));

        // Log to verify the copied files
        File indexFile = new File(baseDir, "index.html");
        if (indexFile.exists()) {
            Log.d("Game1Activity", "Game 1 index.html exists at: " + indexFile.getAbsolutePath());
        } else {
            Log.e("Game1Activity", "Game 1 index.html not found in: " + baseDir);
        }

        // Start the local server
        localServer = new LocalServer(8081, baseDir);
        try {
            localServer.start();
            Log.d("Game1Activity", "LocalServer started on port 8081");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load the game from the local server
        webView.loadUrl("http://localhost:8081/index.html");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the server when the activity is destroyed
        if (localServer != null) {
            localServer.stop();
        }
    }

}