package com.tp1.picsearch;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button ButtonGo, ButtonClear, Refresh;
    ImageButton Star;
    EditText SearchBar;
    //liste nécessaire pour générer les listes d'images, elles seront mises à jours
    ListView list;
    // Array où seront stockés les Websites, des objets contenant 3 photos et le nom du site extrait. voir la classe Website
    ArrayList<Website> ArrayOfWebsite;


    // The database.
    public static SQLiteDatabase db;
    // The database creator and updater helper.
    DatabaseResearch dbOpenHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Star = findViewById(R.id.Lastresearch);
        ButtonGo = findViewById(R.id.Lancer_recherche);
        SearchBar = findViewById(R.id.Barre_Recherche);
        ButtonClear = findViewById(R.id.effacer_recherche);
        Refresh = findViewById(R.id.Refreshbutton);

        //appel de la BDD
        dbOpenHelper = new DatabaseResearch(this, DatabaseResearch.Constants.DATABASE_NAME, null,
                DatabaseResearch.Constants.DATABASE_VERSION);
        db = dbOpenHelper.getWritableDatabase();
        db = dbOpenHelper.getReadableDatabase();


        // modification de la listes des images
        list=findViewById(R.id.list);
        ArrayOfWebsite = Website.getWebsites(); //chargement des exemples

        // Create the adapter to convert the array to views
        final WebsiteAdaptater adapter = new WebsiteAdaptater(this, ArrayOfWebsite);

        //Exemple d'ajout d'un webiste
        Integer[] lol = {R.drawable.ic_launcher_background,R.drawable.image1,R.drawable.image1};
        Website newWeb = new Website("Nathan", lol);
        adapter.add(newWeb);
        // Attach the adapter to a ListView et sert à l'actualiser
        list.setAdapter(adapter);




        // Fonction principale : lancer une recherche et va ajouter du contenu dans array of website.
        ButtonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayOfWebsite.clear(); // vide la liste pour chaque nouvelle recherche
                String Recherche = SearchBar.getText().toString(); //objet de la recherche
                String currentTime = Calendar.getInstance().getTime().toString();

                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseResearch.Constants.KEY_COL_DATE, currentTime);
                contentValues.put(DatabaseResearch.Constants.KEY_COL_RECHERCHE, Recherche);
                long rowId = insertRecord(contentValues); // ajout la re


                // lance une recherche sur les différents api
                JsonRechercheUnsplashed task = new JsonRechercheUnsplashed(MainActivity.this, adapter);
                task.execute(Recherche);


                // Actualise la liste.
                list.setAdapter(adapter);
            }
        });


        // Bouton refresh, permet d'actualiser la list view en cas de non affichage
        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.setAdapter(adapter);
            }
        });



        // Cliquer sur un des éléments ( donc des Website ) ouvre l'activité de partage en donnant les 3 images et le nom du site.
        list.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Website web = (Website) parent.getItemAtPosition(position);
                //Toast.makeText(MainActivity.this, "touché", Toast.LENGTH_SHORT).show();


                Intent i = new Intent(MainActivity.this, MessageSender.class );
                //send the information
                i.putExtra("site",web.Name);
                i.putExtra("Picture1",web.Pictures[0]);
                i.putExtra("Picture2",web.Pictures[1]);
                i.putExtra("Picture3",web.Pictures[2]);
                startActivity(i);
            }
        });




        // Effacer la recherche
        ButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            SearchBar.setText("");
                Intent i = new Intent(MainActivity.this, MessageSender.class );
                //i.putExtra(); //send the picture
                startActivity(i);

            }
        });


        // lance la page des dernières recherches
        Star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentTime = Calendar.getInstance().getTime().toString();
                String Recherche = "star";
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseResearch.Constants.KEY_COL_DATE, currentTime);
                contentValues.put(DatabaseResearch.Constants.KEY_COL_RECHERCHE, Recherche);
                long rowId = insertRecord(contentValues);

                Intent i = new Intent(MainActivity.this, LastResearch.class );
                //i.putExtra(); //send the picture
                startActivity(i);
            }
        });
    }


    //gestion BDD
    private long insertRecord(ContentValues contentValues) {
        // Assign the values for each column.

        // Insert the line in the database
        long rowId = db.insert(DatabaseResearch.Constants.MY_TABLE, null, contentValues);

        // Test to see if the insertion was ok
        if (rowId == -1) {
            Toast.makeText(this, "Error when creating an insert",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "recherche created and stored in database",
                    Toast.LENGTH_SHORT).show();
        }
        return rowId;
    }
}
