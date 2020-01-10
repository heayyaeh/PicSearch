package com.tp1.picsearch;

import java.util.ArrayList;

public class Website {
    public String Name;
    public Integer[] Pictures;

    public Website (String Name, Integer[] Pictures){
        this.Name = Name;
        this.Pictures = Pictures;
    }

    // Fonction permettant de remplir la liste d'exemple
    public static ArrayList<Website> getWebsites() {
        ArrayList<Website> webs = new ArrayList<Website>();
        Integer[] lol = {R.drawable.image1,R.drawable.image1,R.drawable.image1};
        webs.add(new Website("Flickr", lol));
        webs.add(new Website("Unsplashed", lol));
        webs.add(new Website("Pinterest", lol));
        return webs;
    }
}
