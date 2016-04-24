package com.dudkovlad.ymdsstars.actors_activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dudkovlad.ymdsstars.R;
import com.dudkovlad.ymdsstars.data.Actor;
import com.dudkovlad.ymdsstars.data.LoadAndSaveActors;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Activity with recycler view that contains actor_card views 
 * with search view in actors by name and genres 
 * with swipe refreshing
 */
public class ActorsRVActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener
        , MaterialSearchView.OnQueryTextListener
        , MaterialSearchView.SearchViewListener {

    private RecyclerView       actorsRecyclerView;
    private ActorsRVAdapter    actorsRVAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Realm              realm;
    private MaterialSearchView searchView;
    private Toolbar            toolbar;


    private RealmActorsAdapter realmAdapter;

    private String             searchQuery = "";

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {

        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_actors ); 

        // Get a Realm db instance for this thread
        realm = Realm.getDefaultInstance ();

        //get views
        actorsRecyclerView = (RecyclerView)findViewById ( R.id.actors_recycler_view );
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById ( R.id.actors_swipe_refresh_layout );
        searchView         = (MaterialSearchView)findViewById ( R.id.search_view );
        toolbar            = (Toolbar)findViewById ( R.id.toolbar );

        setSupportActionBar ( toolbar );

        
        
        searchView.setOnQueryTextListener ( this );
        searchView.setOnSearchViewListener ( this );

        //init Recycler view adapter for actors data
        actorsRVAdapter = new ActorsRVAdapter ();

        //set up recycler view for showing actors
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager ( this );
        
        actorsRecyclerView.setLayoutManager ( layoutManager );
        actorsRecyclerView.setAdapter ( actorsRVAdapter );   // Устанавливаем адаптер

        //on refresh it will load data from server and update it i realm db
        // and then realm auto updates recycler view content
        swipeRefreshLayout.setOnRefreshListener ( this );


    }

    private void setActorsToRV () {

        // Set the data and tell the RecyclerView to draw
        RealmResults<Actor> actors = realm
                .where ( Actor.class )
                .contains ( "name", searchQuery, Case.INSENSITIVE )
                //todo Case.INSENSITIVE doesn't work for russian text
                .or ()
                .contains ( "genres", searchQuery, Case.INSENSITIVE )
                .findAllSortedAsync ( "name" );
        //we don't need actors pointer anymore because of RealmObjects updates automatically

        //set up data adapter between realm db and recycler view adapter
        realmAdapter = new RealmActorsAdapter ( this, actors, true );
        actorsRVAdapter.setRealmAdapter ( realmAdapter );


        actorsRVAdapter.notifyDataSetChanged ();
    }

    @Override
    public void onResume () {

        super.onResume ();

        setActorsToRV ();

        //if it's not first run then may be data is in db
        //so it's unnecessary to load data from server because of it updates hardly ever:)
        if ( actorsRVAdapter.getItemCount () > 0 ) {

            actorsRVAdapter.notifyDataSetChanged ();

        } else {
            new LoadAndSaveActors ()  //loading actors from server and saving them to realm db
                    .updateRecyclerView ( actorsRVAdapter )
                    //after loading it calls notifyDataSetChanged on adapter
                    .stopRefreshing ( swipeRefreshLayout )
                    //stop refreshing animation on swipeRefreshLayout
                    .execute ();
        }
    }

    @Override
    public boolean onCreateOptionsMenu ( Menu menu ) {

        MenuInflater inflater = getMenuInflater ();
        inflater.inflate ( R.menu.activity_actor_menu, menu );


        MenuItem item = menu.findItem ( R.id.menu_search );
        searchView.setMenuItem ( item );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected ( MenuItem item ) {
        // Handle item selection
        switch ( item.getItemId () ) {
            case R.id.menu_refresh:
                //start refreshing like if user swipe down
                swipeRefreshLayout.setRefreshing ( true );
                onRefresh ();
                break;
            default:
                return super.onOptionsItemSelected ( item );
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange ( String newText ) {

        searchQuery = newText;
        setActorsToRV ();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit ( String query ) {

        return false;
    }

    @Override
    public void onSearchViewShown () {

    }

    @Override
    public void onSearchViewClosed () {
        //todo make animation of closing
        setActorsToRV ();
    }

    @Override
    public void onBackPressed () {
        //close searchView if shown
        if ( searchView.isSearchOpen () ) {
            searchView.closeSearch ();
        } else {
            super.onBackPressed ();
        }
    }

    @Override //called when swiped to refresh or clicked refresh
    public void onRefresh () {

        new LoadAndSaveActors ()  //loading actors from server and saving them to realm db
                .updateRecyclerView ( actorsRVAdapter )
                                  //after loading it calls notifyDataSetChanged on adapter
                .stopRefreshing ( swipeRefreshLayout ) 
                                  //stop refreshing animation on swipeRefreshLayout
                .execute ();      //I don't think it's unnecessary code repetition
    }

    @Override
    protected void onDestroy () {

        super.onDestroy ();

        realm.close ();
        realm = null;
    }
}
