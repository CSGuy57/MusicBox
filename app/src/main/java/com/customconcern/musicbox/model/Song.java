package com.customconcern.musicbox.model;

/**
 * Created by Daniel Lande on 7/19/2016.
 */
public class Song {
    //region Fields
    //garrett was here

    private long id;
    private String title;
    private String artist;

    //endregion


    //region Getter and Setters

    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}

    //endregion


    //region Constructors

    public Song(long songID, String songTitle, String songArtist) {
        id=songID;
        title=songTitle;
        artist=songArtist;
    }
    //endregion


    // region Methods


    //endregion
}
