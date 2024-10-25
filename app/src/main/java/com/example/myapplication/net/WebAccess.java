package com.example.myapplication.net;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class WebAccess {
    String url;
    public WebAccess(String url) {
        this.url = url;
    }
    public String uploadPhoto(File file) {
        try {
            URL url = new URL("http://" + this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            String boundary = "===" + System.currentTimeMillis() + "===";
            String lineFeed = "\r\n";
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes("--" + boundary + lineFeed);
            os.writeBytes( "Content-Disposition: form-data; name=\"" + "File"
                    + "\"; fileName=\"" + file.getName() + "\""+lineFeed);
            os.writeBytes(
                    "Content-Type: "
                            + URLConnection.guessContentTypeFromName(file.getName())+lineFeed);
            os.writeBytes("Content-Transfer-Encoding: binary" + lineFeed);
            os.writeBytes(lineFeed);
            FileInputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            os.writeBytes(lineFeed);
            os.writeBytes(lineFeed);
            os.writeBytes("--" + boundary + "--" + lineFeed);
            os.flush();
            os.close();
            String response = null;
            int status = conn.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                response += line;
            }
            br.close();
            conn.disconnect();
            return response;
        } catch (Exception e) {
            Log.e("Web Access:", e.getMessage());
            return e.getMessage();
        }
    }
    public String[] listAllPhotos() {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("http://" + this.url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            StringBuffer buffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String response = null, line = null;
            while ((line = br.readLine()) != null) {
                response += line;
            }
            br.close();
            conn.disconnect();
            if (response != null && response.length() > 0) {
                return response.split(",");
            }
        } catch (Exception e) {
            Log.e("Web Access:", e.getMessage());
        }
        return null;
    }
}


