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
 * Created by vlad on 17.04.16.  todo make description
 */
public class ActorCardViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public CardView           cardView;
    public TextView           nameView;
    public TextView           genresView;
    public TextView           countsView;
    public TextView           descriptionView;
    public AppCompatImageView photo;
    public AppCompatImageView photoTmpBackground;


    private Actor actor;

    private Context context;


    public ActorCardViewHolder ( View itemView ) {

        super ( itemView );
        //get views
        cardView = (CardView)itemView.findViewById ( R.id.actor_card_view );
        nameView = (TextView)itemView.findViewById ( R.id.card_actor_name_view );
        genresView = (TextView)itemView.findViewById ( R.id.card_actor_genres_view );
        countsView = (TextView)itemView.findViewById ( R.id.card_actor_counts_view );
        descriptionView = (TextView)itemView.findViewById ( R.id.card_actor_description_view );
        photo = (AppCompatImageView)itemView.findViewById ( R.id.card_actor_photo_view );
        photoTmpBackground = (AppCompatImageView)itemView.findViewById (
                R.id.card_actor_photo_temp_background );

        context = itemView.getContext ();

        //wile click ActorActivity will start
        cardView.setOnClickListener ( this );

    }

    /**
     * set actor data to this view holder and load image
     */
    public void setActor ( Actor actor ) {

        this.actor = actor;

        //set text to description views
        nameView.setText ( actor.getName () );
        genresView.setText ( actor.getGenres () );
        descriptionView.setText ( actor.getDescription () );
        countsView.setText ( actor.getQuantityCounts ( context ) );

        //create callback for picasso to animate image showing
        AnimatedPicassoCallback callback = new AnimatedPicassoCallback (
                photo
                , photoTmpBackground );

        //load photo from server or from cache
        Picasso.with ( context )
               .load ( actor.getCoverSmall () )
               .error (
                       R.drawable.ic_face ) //don't call fit here because of in actorActivity image will not load
               .into ( photo, callback );
    }

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
        //while clicking on card view start actor sliding activity

        ActorActivity.start (
                context
                , actor
                , location[0]
                , location[1]
                , photo.getWidth ()
                , photo.getHeight () );
    }
}
