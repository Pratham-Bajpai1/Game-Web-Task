package com.example.gameweb;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import fi.iki.elonen.NanoHTTPD;

public class LocalServer extends NanoHTTPD {
    private final String baseDir; // Base directory for serving files

    public LocalServer(int port, String baseDir) {
        super(port);
        this.baseDir = baseDir;
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            String uri = session.getUri();
            if (uri.equals("/")) uri = "/index.html"; // Default to index.html

            File file = new File(baseDir + uri);
            if (file.exists() && !file.isDirectory()) {
                FileInputStream fis = new FileInputStream(file);
                return newFixedLengthResponse(Response.Status.OK, getMimeType(uri), fis, fis.available());
            } else {
                return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "404 Not Found: " + session.getUri());
            }
        } catch (Exception e) {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", "Error: " + e.getMessage());
        }
    }

    private String getMimeType(String uri) {
        if (uri.endsWith(".html")) return "text/html";
        if (uri.endsWith(".css")) return "text/css";
        if (uri.endsWith(".js")) return "application/javascript";
        if (uri.endsWith(".png")) return "image/png";
        if (uri.endsWith(".jpg") || uri.endsWith(".jpeg")) return "image/jpeg";
        if (uri.endsWith(".json")) return "application/json";
        return "application/octet-stream";
    }
}
