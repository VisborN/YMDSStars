package com.dudkovlad.ymdsstars.actor_activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dudkovlad.ymdsstars.R;
import com.dudkovlad.ymdsstars.data.Actor;
import com.klinker.android.sliding.SlidingActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Activity that shows name, full description, cover from actor
 */
public class ActorActivity extends SlidingActivity implements View.OnClickListener {

    private Actor       actor;
    private TextView    genresView
                        , descriptionView
                        , countsView;
    private ImageView   photoView;
    private int         expansionLeftOffset
                        , expansionTopOffset
                        , expansionViewWidth
                        , expansionViewHeight;


    @Override
    public void init ( Bundle savedInstanceState ) {

        setContent ( R.layout.activity_actor );

        Resources res = getResources ();

        //define Actor object and expansion coordinates with data from intent
        deserializeIntent ();

        //get views
        genresView      = (TextView)findViewById ( R.id.genres_view );
        descriptionView = (TextView)findViewById ( R.id.description_view );
        countsView      = (TextView)findViewById ( R.id.counts_view );
        photoView       = (ImageView)findViewById ( R.id.photo );


        setPrimaryColors ( res.getColor ( R.color.colorPrimary )
                         , res.getColor ( R.color.colorPrimaryDark ) );

        //making readable phrase from counts of albums and tracks
        String countOfAlbumsTracks = actor.getQuantityCounts ( this );


        //setting text to description views
        if ( actor.getName () != null ) {
            setTitle ( actor.getName () );
        }
        //todo there is problem with title margin on api 16 and may be others

        if ( actor.getGenres () != null ) {
            genresView.setText ( actor.getGenres () );
        }

        if ( actor.getDescription () != null ) {
            descriptionView.setText ( actor.getDescription () );
        }

        countsView.setText ( countOfAlbumsTracks );

        //setting floating action button to open link in browser
        if (    actor.getLink () != null 
             && !actor.getLink ().equals ( "" ) ) {
            
            setFab ( res.getColor ( R.color.colorAccent )
                    , R.drawable.ic_open_in_browser
                    , this );
        }


        //set up activity starting animation parameters
        if ( expansionLeftOffset >= 0
             && expansionTopOffset >= 0
             && expansionViewWidth >= 0
             && expansionViewHeight >= 0 ) {
            
            expandFromPoints (
                    expansionLeftOffset
                    , expansionTopOffset
                    , expansionViewWidth
                    , expansionViewHeight );
        }
        //load and set cover photo with picasso
        loadAndSetPhoto ();


    }

    /**
     * fast set cached small photo then load and set big photo
     */
    private void loadAndSetPhoto () {

        try {
            Picasso.with ( this )
                   .load ( actor.getCoverSmall () )
                   .noPlaceholder ()
                   .into ( photoView
                           , new Callback () {

                               @Override
                               public void onSuccess () {
                                   //replace small photo with big photo
                                   //noPlaceHolder used for inconspicuous changing image
                                   try {
                                       Picasso.with ( photoView.getContext () )
                                              .load ( actor.getCoverBig () )
                                              .noPlaceholder ()
                                              .error ( R.drawable.ic_face )
                                              .into ( photoView );
                                   } catch ( IllegalArgumentException e ) {
                                       e.printStackTrace (); //just don't load image
                                   }
                               }

                               @Override
                               public void onError () {

                               }
                           } );

        } catch ( IllegalArgumentException e ) {
            e.printStackTrace (); //just don't load image
        }
    }

    //calls while clicked on FAB button
    @Override
    public void onClick ( View v ) {

        try {
            Uri link = Uri.parse ( actor.getLink () );

            Intent browserIntent = new Intent ( Intent.ACTION_VIEW, link );

            startActivity ( browserIntent );

        } catch ( ActivityNotFoundException e ) {
            e.printStackTrace ();
            Toast.makeText ( this, getText ( R.string.error ), Toast.LENGTH_LONG )
                 .show ();
        }
    }

    private void deserializeIntent () {

        final Intent intent = getIntent ();

        actor = new Actor ();
        //define actor
        actor.setName ( intent.getStringExtra ( "NAME" ) );
        actor.setTracks ( intent.getIntExtra ( "TRACKS", 0 ) );
        actor.setAlbums ( intent.getIntExtra ( "ALBUMS", 0 ) );
        actor.setLink ( intent.getStringExtra ( "LINK" ) );
        actor.setDescription ( intent.getStringExtra ( "DESCRIPTION" ) );
        actor.setCoverSmall ( intent.getStringExtra ( "COVER_SMALL" ) );
        actor.setGenres ( intent.getStringExtra ( "GENRES" ) );
        actor.setCoverBig ( intent.getStringExtra ( "COVER_BIG" ) );

        //get activity starting animation parameters
        expansionLeftOffset = intent.getIntExtra ( "EXPANSION_LEFT_OFFSET", 0 );
        expansionTopOffset = intent.getIntExtra ( "EXPANSION_TOP_OFFSET", 0 );
        expansionViewWidth = intent.getIntExtra ( "EXPANSION_VIEW_WIDTH", 0 );
        expansionViewHeight = intent.getIntExtra ( "EXPANSION_VIEW_HEIGHT", 0 );
    }

    /**
     * Starts this activity with actor data and expansion animation
     *
     * @param actor               data to show in activity
     * @param expansionLeftOffset coordinate for expansion animation
     * @param expansionTopOffset  coordinate for expansion animation
     * @param expansionViewWidth  start size for expansion animation
     * @param expansionViewHeight start size for expansion animation
     */
    public static void start (
            Context context
            , Actor actor
            , int expansionLeftOffset
            , int expansionTopOffset
            , int expansionViewWidth
            , int expansionViewHeight ) {

        Intent intent = new Intent ( context, ActorActivity.class );

        //putting actor to intent
        intent.putExtra ( "NAME", actor.getName () );
        intent.putExtra ( "TRACKS", actor.getTracks () );
        intent.putExtra ( "ALBUMS", actor.getAlbums () );
        intent.putExtra ( "LINK", actor.getLink () );
        intent.putExtra ( "DESCRIPTION", actor.getDescription () );
        intent.putExtra ( "COVER_SMALL", actor.getCoverSmall () );
        intent.putExtra ( "COVER_BIG", actor.getCoverBig () );
        intent.putExtra ( "GENRES", actor.getGenres () );

        //putting activity starting animation parameters
        intent.putExtra ( "EXPANSION_LEFT_OFFSET", expansionLeftOffset );
        intent.putExtra ( "EXPANSION_TOP_OFFSET", expansionTopOffset );
        intent.putExtra ( "EXPANSION_VIEW_WIDTH", expansionViewWidth );
        intent.putExtra ( "EXPANSION_VIEW_HEIGHT", expansionViewHeight );


        context.startActivity ( intent );
    }
}
