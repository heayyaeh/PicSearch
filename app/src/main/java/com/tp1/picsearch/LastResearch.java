package com.tp1.picsearch;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static com.tp1.picsearch.DatabaseResearch.Constants.MY_TABLE;

public class LastResearch extends Activity {
    SQLiteDatabase db = MainActivity.db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseResearch dbOpenHelper = new DatabaseResearch(this, DatabaseResearch.Constants.DATABASE_NAME, null,
                DatabaseResearch.Constants.DATABASE_VERSION);
        db = dbOpenHelper.getReadableDatabase();
        ArrayList<String> recherche = getRecherche();
        
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.last_research, recherche);

        ListView listView = (ListView) findViewById(R.id.ExtractFromBDD);
        listView.setAdapter(adapter);
    }
    public ArrayList<String> getRecherche() {
        ArrayList<String> Liste = new ArrayList<>();
// Select All Query
        String selectQuery = "SELECT * FROM " + MY_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String R = cursor.getString(1);
// Adding research to list
                Liste.add(R);
            } while (cursor.moveToNext());
        }
// return contact list
        return Liste;
    }
}
