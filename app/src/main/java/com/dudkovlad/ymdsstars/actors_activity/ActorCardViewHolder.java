package com.dudkovlad.ymdsstars.actors_activity;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dudkovlad.ymdsstars.AnimatedPicassoCallback;
import com.dudkovlad.ymdsstars.R;
import com.dudkovlad.ymdsstars.actor_activity.ActorActivity;
import com.dudkovlad.ymdsstars.data.Actor;
import com.squareup.picasso.Picasso;

/**
 * ViewHolder for recycler view with actor_card
 */
public class ActorCardViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public final CardView           cardView;
    public final TextView           nameView;
    public final TextView           genresView;
    public final TextView           countsView;
    public final TextView           descriptionView;
    public final AppCompatImageView photo;
    public final AppCompatImageView photoTmpBackground;


    private Actor actor;

    private final Context context;


    public ActorCardViewHolder ( View v ) {

        super ( v );
        //get views
        cardView           = (CardView)v.findViewById ( R.id.actor_card_view );
        nameView           = (TextView)v.findViewById ( R.id.card_actor_name_view );
        genresView         = (TextView)v.findViewById ( R.id.card_actor_genres_view );
        countsView         = (TextView)v.findViewById ( R.id.card_actor_counts_view );
        descriptionView    = (TextView)v.findViewById ( R.id.card_actor_description_view );
        photo              = (AppCompatImageView)v.findViewById ( R.id.card_actor_photo_view );
        photoTmpBackground = (AppCompatImageView)v.findViewById ( R.id.card_actor_photo_temp_background );

        context = v.getContext ();

        //wile click ActorActivity will start
        cardView.setOnClickListener ( this );

    }

    /**
     * set actor data to this view holder and load image
     */
    public void setActor ( Actor actor ) {

        this.actor = actor;

        //set text to description views
        if ( actor.getName () != null ) {
            nameView.setText ( actor.getName () );
        }
        if ( actor.getGenres () != null ) {
            genresView.setText ( actor.getGenres () );
        }
        if ( actor.getDescription () != null ) {
            descriptionView.setText ( actor.getDescription () );
        }
        
        countsView.setText ( actor.getQuantityCounts ( context ) );

        //create callback for picasso to animate image showing
        AnimatedPicassoCallback callback = 
                new AnimatedPicassoCallback (
                        photo
                        , photoTmpBackground );

        //load photo from server or from cache
        Picasso.with ( context )
               .load ( actor.getCoverSmall () )
               .error ( R.drawable.ic_face ) 
               //don't call fit here because of in actorActivity image will not load fast
               .into ( photo, callback );
    }

    //while clicking on card view start actor sliding activity
    @Override
    public void onClick ( View view ) {

        switch ( view.getId () ) {
            case R.id.actor_card_view:
                startActorActivity ();
                break;

        }

    }


    private void startActorActivity () {

        int location[] = new int[2];
        photo.getLocationInWindow ( location );

        ActorActivity.start (
                context
                , actor
                , location[0]
                , location[1]
                , photo.getWidth ()
                , photo.getHeight () );
    }
}
