package com.dudkovlad.ymdsstars.data;


import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import com.dudkovlad.ymdsstars.actors_activity.ActorsRVAdapter;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import io.realm.Realm;

/**
 * Created by vlad on 28.03.16.todo complete
 */
public class LoadAndSaveActors extends AsyncTask<String, Void, Integer> {

    public static final String JACTORS_URL = "http://download.cdn.yandex.net/mobilization-2016/artists.json";
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActorsRVAdapter    adapter;
    Realm realm;

    @Override
    protected Integer doInBackground ( String... v ) {

        JSONArray jsonActors = getJsonActors ();

        realm = Realm.getDefaultInstance ();
        SaveToDB ( jsonActors, realm );
        //todo make updating actors list
        realm.close ();
        return jsonActors.length ();

    }


    @Override
    protected void onPostExecute ( Integer count ) {

        if ( swipeRefreshLayout != null ) {
            swipeRefreshLayout.setRefreshing ( false );
        }
        if ( adapter != null ) {
            adapter.notifyDataSetChanged ();
        }
    }

    public LoadAndSaveActors stopRefreshing ( SwipeRefreshLayout swipeRefreshLayout ) {

        this.swipeRefreshLayout = swipeRefreshLayout;
        return this;
    }


    public LoadAndSaveActors updateRecyclerView ( ActorsRVAdapter adapter ) {

        this.adapter = adapter;
        return this;
    }


    public static JSONArray getJsonActors () {

        try {
            URL       url        = new URL ( JACTORS_URL );
            JSONArray jsonActors = new JSONArray ( IOUtils.toString ( url ) );

            return jsonActors;

        } catch ( JSONException e ) {
            e.printStackTrace ();

        } catch ( MalformedURLException e ) {

            e.printStackTrace ();

        } catch ( IOException e ) {

            e.printStackTrace ();

        }
        return new JSONArray ();
    }


    public static void SaveToDB ( JSONArray jsonActors, Realm realm ) {

        realm.beginTransaction ();
        try {
            for ( int i = 0; i < jsonActors.length (); i++ ) {
                com.dudkovlad.ymdsstars.data.Actor actor = realm.createOrUpdateObjectFromJson (
                        com.dudkovlad.ymdsstars.data.Actor.class,
                        (JSONObject)jsonActors.get ( i ) );
                actor.completeFromJson ( (JSONObject)jsonActors.get ( i ) );

            }
            realm.commitTransaction ();
        }//todo make catches
        catch ( JSONException e ) {
            e.printStackTrace ();
            realm.cancelTransaction ();
        }


    }
}
