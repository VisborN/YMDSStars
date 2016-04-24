package com.dudkovlad.ymdsstars.data;


import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import com.dudkovlad.ymdsstars.MyApp;
import com.dudkovlad.ymdsstars.actors_activity.ActorsRVAdapter;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import io.realm.Realm;
import io.realm.exceptions.RealmException;

/**
 * Async loading and saving to realm db
 * <p>
 * can stop refreshing animation if called 
 * {@link LoadAndSaveActors#stopRefreshing(SwipeRefreshLayout...)}
 * <p>
 * can call {@link ActorsRVAdapter#notifyDataSetChanged()} if called
 * {@link LoadAndSaveActors#updateRecyclerView(ActorsRVAdapter...)}
 */
public class LoadAndSaveActors extends AsyncTask<String, Void, Integer> {

    private static final String     JSON_ACTORS_URL = MyApp.JSON_ACTORS_URL;
    private SwipeRefreshLayout[]    swipeRefreshLayouts;
    private ActorsRVAdapter[]       adapters;
    Realm                           realm;
    
    @Override
    protected Integer doInBackground ( String... v ) {
        
        //load json array from server
        JSONArray jsonActors = getJsonActors ();
        
        //get instance of realm for this thread
        realm = Realm.getDefaultInstance ();
        
        //save or update data in db 
        saveToDB ( jsonActors, realm );
        
        realm.close ();
        
        return jsonActors.length ();

    }


    @Override
    protected void onPostExecute ( Integer count ) {

        if ( swipeRefreshLayouts != null ) {
            for(SwipeRefreshLayout swipeRefreshLayout : swipeRefreshLayouts) {
                swipeRefreshLayout.setRefreshing ( false );
            }
        }
        
        if ( adapters != null ) {
            for(ActorsRVAdapter adapter : adapters) {
                adapter.notifyDataSetChanged ();
            }
        }
    }

    /**
     * Stops refresh animation after loading actors
     * @param swipeRefreshLayouts layouts for stopping refresh animation
     * @return {@link this}
     */
    public LoadAndSaveActors stopRefreshing ( SwipeRefreshLayout... swipeRefreshLayouts ) {

        this.swipeRefreshLayouts = swipeRefreshLayouts;
        return this;
    }


    /**
     * Calls {@link ActorsRVAdapter#notifyDataSetChanged()} in adapter 
     * @param adapters adapters that contains RealmResults with actors
     * @return {@link this}
     */
    public LoadAndSaveActors updateRecyclerView ( ActorsRVAdapter... adapters ) {

        this.adapters = adapters;
        return this;
    }

    /**
     * 
     * @return JSONArray that contains json from {@link LoadAndSaveActors#JSON_ACTORS_URL}
     * or empty JSONArray if have problems
     */
    public static JSONArray getJsonActors () {
        
        JSONArray jsonActors = null;
        
        try {
            URL url    = new URL ( JSON_ACTORS_URL );
            
            jsonActors = new JSONArray ( IOUtils.toString ( url ) );

        } catch ( JSONException e ) {
            e.printStackTrace ();

        } catch ( MalformedURLException e ) {
            e.printStackTrace ();

        } catch ( IOException e ) {
            e.printStackTrace ();
            
        }
        
        if (jsonActors == null) {
            jsonActors = new JSONArray ();
        }
        
        return jsonActors;
    }

    /**
     * saves each item of jsonActors to db with realm
     * @param jsonActors json array that contains actors with fields like in {@link Actor}
     * @param realm realm instance for current thread
     */
    public static void saveToDB ( JSONArray jsonActors, Realm realm ) {

        try {
            realm.beginTransaction ();

            for ( int i = 0; i < jsonActors.length (); i++ ) {
                
                com.dudkovlad.ymdsstars.data.Actor actor = 
                        realm.createOrUpdateObjectFromJson (
                                Actor.class,
                                (JSONObject)jsonActors.get ( i ) );
                
                //fill actor fields that wasn't willed by createObjectFromJson
                actor.completeFromJson ( (JSONObject)jsonActors.get ( i ) );

            }

            realm.commitTransaction ();
        } catch ( JSONException e ) {
            e.printStackTrace ();
            realm.cancelTransaction ();
            
        } catch ( IllegalArgumentException e ) {
            e.printStackTrace ();
            realm.cancelTransaction ();
            
        } catch ( RealmException e ) {
            e.printStackTrace ();
            realm.cancelTransaction ();
        }


    }
}
