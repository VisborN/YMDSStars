package com.dudkovlad.ymdsstars.actors_activity;

import android.content.Context;

import com.dudkovlad.ymdsstars.RealmAdapterHelper.RealmModelAdapter;
import com.dudkovlad.ymdsstars.data.Actor;

import io.realm.RealmResults;

/**
 * Created by vlad on 17.04.16. todo make description
 */
public class RealmActorsAdapter extends RealmModelAdapter<Actor> {

    public RealmActorsAdapter ( Context context,
                                RealmResults<Actor> realmResults,
                                boolean automaticupdate ) {

        super ( context, realmResults, automaticupdate );
    }
}
