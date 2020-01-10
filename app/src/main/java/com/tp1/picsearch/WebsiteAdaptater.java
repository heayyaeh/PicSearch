package com.tp1.picsearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WebsiteAdaptater extends ArrayAdapter<Website> {
    public WebsiteAdaptater(Context context, ArrayList<Website> Webs) {
        super(context, 0, Webs);        //Webs contient donc plusieurs Website.  chaque Website contient un nom et 3 images
    }
        @Override
            public View getView(int position, View convertView, ViewGroup parent) {

            // Get the data item for this position
            Website website = getItem(position);        //on récupère un website
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_seule, parent, false);
            }
            // Lookup view for data population
            TextView WebsiteName = (TextView) convertView.findViewById(R.id.WebsiteTexte);
            ImageView Picture1 =  convertView.findViewById(R.id.imgFlicker1);
            ImageView Picture2 =  convertView.findViewById(R.id.imgFlicker2);
            ImageView Picture3 =  convertView.findViewById(R.id.imgFlicker3);

            // Populate the data into the template view using the data object
            WebsiteName.setText(website.Name);
            Picture1.setImageResource(website.Pictures[0]);
            Picture2.setImageResource(website.Pictures[1]);
            Picture3.setImageResource(website.Pictures[2]);

            // Return the completed view to render on screen
            return convertView;
    }

}
