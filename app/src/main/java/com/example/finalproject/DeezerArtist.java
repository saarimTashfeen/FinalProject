package com.example.finalproject;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author Austin
 * @version 1.0
 */

public class DeezerArtist {
    /** The title of the song and album it's located on */
    private String title, albumName;
    /** How many seconds the song lasts */
    private int duration;
    /** A bitmap representation of the album artwork */
    private Bitmap albumCover;
    /** Identification number used for database retrieval  */
    private long id;

    /**
     * Constructor for creating new instances of DeezerArtist using all parameters
     * @param title The title of the song
     * @param albumName The album that the song is located on
     * @param duration The length of song in seconds
     * @param albumCover The album artwork
     * @param id Identification number from database
     */
    public DeezerArtist(String title, String albumName, int duration, Bitmap albumCover, long id) {
        this.title = title;
        this.albumName = albumName;
        this.duration = duration;
        this.albumCover = albumCover;
        this.id = id;
    }

    /**
     * Constructor for creating new instances of DeezerArtist without a database id
     * @param title The title of this song
     * @param albumName The album that this song is located on
     * @param duration The length of this song in seconds
     * @param albumCover This album artwork
     */

    public DeezerArtist(String title, String albumName, int duration, Bitmap albumCover) {
        this.title = title;
        this.albumName = albumName;
        this.duration = duration;
        this.albumCover = albumCover;
    }

    /**
     * Constructor for creating new instances of DeezerArtist without database id or album art
     * @param title The title of this song
     * @param albumName The album that this song is located on
     * @param duration The length of this song in seconds
     */

    //mostly for testing purposes, but in case an Deezer doesn't have the album cover available
    public DeezerArtist(String title, String albumName, int duration) {
        this.title = title;
        this.albumName = albumName;
        this.duration = duration;
    }

    /**
     * Constructor for creating new instances of DeezerArtist using only the title
     * @param title The title of this song
     */

    public DeezerArtist(String title) {
        this.title = title;
    }

    /**
     * Gets the title of this song
     * @return The title of the song
     */

    public String getTitle() {
        return title;
    }

    /**
     * Changes the title of this song
     * @param title The title of the song
     */

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the title of this album
     * @return The title of the album
     */

    public String getAlbumName() {
        return albumName;
    }

    /**
     * Changes the name of this album
     * @param albumName The title of the album
     */
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    /**
     * Gets the duration of this song
     * @return The duration of the song in seconds
     */

    public int getDuration() {
        return duration;
    }

    /**
     * Changes the duration of this song
     * @param duration The duration of the song in seconds
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets the album artwork for this song
     * @return The album artwork as a Bitmap
     */

    public Bitmap getAlbumCover() {
        return albumCover;
    }

    /**
     * Changes the album artwork for this song
     * @param albumCover The album artwork as a Bitmap
     */

    public void setAlbumCover(Bitmap albumCover) {
        this.albumCover = albumCover;
    }

}
