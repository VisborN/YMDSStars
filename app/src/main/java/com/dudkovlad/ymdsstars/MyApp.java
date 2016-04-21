package com.dudkovlad.ymdsstars;


import android.app.Application;

import com.dudkovlad.ymdsstars.data.MyRealmMigration;
import com.yandex.metrica.YandexMetrica;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by vlad on 31.03.16. todo make description
 */
public class MyApp extends Application {

    public final static String YM_API_KEY = "af438558-8be4-40bb-8efc-47c76dffab7d";

    @Override
    public void onCreate () {

        super.onCreate ();
        YandexMetrica.activate ( getApplicationContext (), YM_API_KEY );
        YandexMetrica.enableActivityAutoTracking ( this );
        YandexMetrica.setSessionTimeout ( 60 );


        RealmConfiguration config = new RealmConfiguration
                .Builder ( this )
                .schemaVersion ( 1 ) // Must be bumped when the schema changes
                .migration (
                        new MyRealmMigration () ) // Migration to run instead of throwing an exception
                .build ();
        Realm.setDefaultConfiguration ( config );
    }
}
