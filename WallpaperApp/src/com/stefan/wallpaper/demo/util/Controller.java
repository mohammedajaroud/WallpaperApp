package com.stefan.wallpaper.demo.util;

import com.google.gson.GsonBuilder;
import com.stefan.wallpaper.demo.models.Categories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Controller {

    private static final int CONNECTION_TIMEOUT = 45 * 1000; // 45 seconds

    public static final String ROOT_URL = "http://www.pengaja.com/";
    public static final String WALLPAPER_URL = ROOT_URL + "wallpaper2/images/";
    public static final String THUMBS_URL = ROOT_URL + "wallpaper2/thumbs/";

    private static BufferedReader getInputStream(String urlString)
            throws IOException {
        urlString = urlString.replaceAll(" ", "%20");
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(CONNECTION_TIMEOUT);
        return new BufferedReader(new InputStreamReader(conn.getInputStream()));
    }

    public static Categories fetchCategories() throws IOException {
        String urlString = WALLPAPER_URL + "/service.php";
        BufferedReader in = getInputStream(urlString);
        try {
            return new GsonBuilder().create().fromJson(in, Categories.class);
        } finally {
            closeReader(in);
        }
    }

    private static void closeReader(BufferedReader in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
}
