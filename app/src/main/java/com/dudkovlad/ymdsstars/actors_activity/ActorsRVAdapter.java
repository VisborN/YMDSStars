package com.dudkovlad.ymdsstars.actors_activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dudkovlad.ymdsstars.data.Actor;
import com.dudkovlad.ymdsstars.R;
import com.dudkovlad.ymdsstars.RealmAdapterHelper.RealmRecyclerViewAdapter;

/**
 * Created by vlad on 02.04.16.todo description
 */
public class ActorsRVAdapter extends RealmRecyclerViewAdapter<Actor> {

    @Override
    public ActorCardViewHolder onCreateViewHolder ( ViewGroup parent, int viewType ) {
        //inflate actor card view
        View view = LayoutInflater
                .from ( parent.getContext () )
                .inflate ( R.layout.actor_card, parent, false );


        ActorCardViewHolder myViewHolder = new ActorCardViewHolder ( view );

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder ( final RecyclerView.ViewHolder viewHolder,
                                   final int listPosition ) {
        //get actor data from RealmResults
        Actor actor = getItem ( listPosition );

        //setting to viewHolder its actor data,
        //viewHolder will send it to actorActivity on clicking on card view
        ((ActorCardViewHolder)viewHolder).setActor ( actor );
    }

    @Override
    public int getItemCount () {

        if ( getRealmAdapter () != null ) {
            return getRealmAdapter ().getCount ();
        }
        return 0;
    }
}
