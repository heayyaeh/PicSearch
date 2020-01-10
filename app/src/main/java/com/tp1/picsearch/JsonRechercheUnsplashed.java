package com.tp1.picsearch;


import android.graphics.Bitmap;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class JsonRechercheUnsplashed extends AsyncTask<String, Void, JSONObject> {

    private AppCompatActivity myActivity;
    private WebsiteAdaptater adaptater;

    public JsonRechercheUnsplashed(AppCompatActivity mainActivity, WebsiteAdaptater Adaptater) {
        myActivity = mainActivity;
        adaptater = Adaptater;
    }



    @Override
    protected JSONObject doInBackground(String... strings) {
        URL url = null;
        HttpURLConnection urlConnection = null;
        String result = null;
        String Recherche = (strings[0]);
        String Access = "c0f7c3d48ec5f978f1674dbff41fa53b09dc21556d2f9aa07933e80442302115";

        try {

            url = new URL("https://api.unsplash.com/search/photos?query="+Recherche+"&page=1&per_page=3&client_id="+Access);
            urlConnection = (HttpURLConnection) url.openConnection(); // Open
            InputStream in = new BufferedInputStream(urlConnection.getInputStream()); // Stream

            result = readStream(in); // Read stream de Flickr

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        JSONObject json = null;
        try {
            json = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json; // returns the result
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        ArrayList<Bitmap> Picture = new ArrayList<>();

        try {
            JSONArray items = s.getJSONArray("results");

            for (int i = 0; i < items.length(); i++) {
                JSONObject result = items.getJSONObject(i);
                String urlmedia = result.getJSONObject("links").getString("download");
                //Log.i("CIO", "URL media: " + urlmedia);

                // Downloading image and addind to the list of bitmap
                Downloader abd = new Downloader(Picture);
                abd.execute(urlmedia);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    // Extraction de l'objet Json de Unsplash
    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        is.close();

        // Extracting the JSON object from the String
        String jsonextracted = sb.toString();
        Log.i("CIO", jsonextracted);
        return jsonextracted;
    }
}

