package com.dudkovlad.ymdsstars.actors_activity;

import android.content.Context;

import com.dudkovlad.ymdsstars.RealmAdapterHelper.RealmModelAdapter;
import com.dudkovlad.ymdsstars.data.Actor;

import io.realm.RealmResults;

/**
 * realm model adapter for actors
 */
public class RealmActorsAdapter extends RealmModelAdapter<Actor> {

    public RealmActorsAdapter ( Context context,
                                RealmResults<Actor> realmResults,
                                boolean automaticUpdate ) {

        super ( context, realmResults, automaticUpdate );
    }
}
