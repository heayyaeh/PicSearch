package com.tp1.picsearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Downloader extends AsyncTask<String, Void, Bitmap> {
    ArrayList<Bitmap> picture;
    ImageView pic1, pic2, pic3;

    public Downloader(ArrayList<Bitmap> Picture) {
        picture = Picture;
    }
        // le but de de Downloader va être de télécharger et remplir


    @Override
    protected Bitmap doInBackground(String... strings) {

        URL url = null;
        HttpURLConnection urlConnection = null;
        Bitmap bm = null;
        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection(); // Open
            InputStream in = new BufferedInputStream(urlConnection.getInputStream()); // Stream
            bm = BitmapFactory.decodeStream(in);
            in.close();
        }
        catch (MalformedURLException e) { e.printStackTrace(); }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return bm;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        // complète l'array de bitmap
        Log.i("CIO", "Image received !");
        picture.add(bitmap);
    }
}