package com.dudkovlad.ymdsstars.data;

import android.content.Context;
import android.content.res.Resources;

import com.dudkovlad.ymdsstars.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * class that describes an actor, and adds it to db like:_id name    tracks  albums  link
 * description cover_small cover_big 12  John    100     10      ...     ...         ... ...
 */
public class Actor extends RealmObject {

    //if change fields then change MyRealmMigration
    @PrimaryKey
    private int    id;
    @Index
    private String name;
    private int    tracks;
    private int    albums;
    private String link;
    private String description;
    private String coverSmall;
    private String coverBig;
    @Index
    private String genres;

    public void completeFromJson ( JSONObject jsonObject ) {

        try {
            JSONArray genresJsonArray = jsonObject.getJSONArray ( "genres" );

            StringBuilder genresBuilder = new StringBuilder ();
            if ( genresJsonArray.length () > 0 ) {
                genresBuilder.append ( genresJsonArray.get ( 0 ) );
            }
            for ( int i = 1; i < genresJsonArray.length (); i++ ) {
                genresBuilder.append ( ", " );
                genresBuilder.append ( genresJsonArray.get ( i ) );
            }
            setGenres ( genresBuilder.toString () );
            setCoverSmall ( jsonObject.getJSONObject ( "cover" )
                                      .get ( "small" )
                                      .toString () );
            setCoverBig ( jsonObject.getJSONObject ( "cover" )
                                    .get ( "big" )
                                    .toString () );
        } catch ( JSONException e ) {
            e.printStackTrace ();
            setGenres ( "" );
            setCoverSmall ( "" );
            setCoverBig ( "" );
        }
    }

    /**
     * method for making readable string from counts of albums and tracksb
     */
    public String getQuantityCounts ( Context context ) {

        Resources res = context.getResources ();
        String countOfAlbumsTracks =
                res.getQuantityString ( R.plurals.numberOfAlbums, albums, albums );
        countOfAlbumsTracks +=
                res.getString ( R.string.counts_divider );
        countOfAlbumsTracks +=
                res.getQuantityString ( R.plurals.numberOfTracks, tracks, tracks );
        return countOfAlbumsTracks;
    }


    //next are only getters and setters of fields
    public int getId () {

        return id;
    }

    public void setId ( int id ) {

        this.id = id;
    }

    public String getName () {

        return name;
    }

    public void setName ( String name ) {

        this.name = name;
    }

    public int getTracks () {

        return tracks;
    }

    public void setTracks ( int tracks ) {

        this.tracks = tracks;
    }

    public int getAlbums () {

        return albums;
    }

    public void setAlbums ( int albums ) {

        this.albums = albums;
    }

    public String getLink () {

        return link;
    }

    public void setLink ( String link ) {

        this.link = link;
    }

    public String getDescription () {

        return description;
    }

    public void setDescription ( String description ) {

        this.description = description;
    }

    public String getCoverBig () {

        return coverBig;
    }

    public String getCoverSmall () {

        return coverSmall;
    }

    public void setCoverBig ( String coverBig ) {

        this.coverBig = coverBig;
    }

    public void setCoverSmall ( String coverSmall ) {

        this.coverSmall = coverSmall;
    }

    public String getGenres () {

        return genres;
    }

    public void setGenres ( String genres ) {

        this.genres = genres;
    }
}

