package com.dudkovlad.ymdsstars.data;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Describes how to migrate from one version of db to another
 */
public class MyRealmMigration implements RealmMigration {

    @Override
    public void migrate ( DynamicRealm realm, long oldVersion, long newVersion ) {

        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema ();

        // Migrate to version 1: Add a new class.
        /* and its fields
            @PrimaryKey
            private int id;
            @Index
            private String name;
            private int tracks;
            private int albums;
            private String link;
            private String description;
            private String coverSmall;
            private String coverBig;
            @Index
            private String genres;
        */
        if ( oldVersion == 0 ) {
            schema.create ( "Person" )
                  .addField ( "id", int.class, FieldAttribute.PRIMARY_KEY )
                  .addField ( "name", String.class, FieldAttribute.INDEXED )
                  .addField ( "tracks", int.class )
                  .addField ( "albums", int.class )
                  .addField ( "link", String.class )
                  .addField ( "description", String.class )
                  .addField ( "coverSmall", String.class )
                  .addField ( "coverBig", String.class )
                  .addField ( "genres", String.class, FieldAttribute.INDEXED );
            oldVersion++; 
        }

        // Migrate to version 2: Add object references
        //
        //if (oldVersion == 1) {}
    }

}
