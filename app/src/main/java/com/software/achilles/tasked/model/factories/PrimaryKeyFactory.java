package com.software.achilles.tasked.model.factories;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import io.realm.Realm;
import io.realm.RealmObject;

public class PrimaryKeyFactory {

    private static AtomicLong unique;

    public static void initialize(Realm realm) {
        // If unique already calculated, skip
        if (unique != null)
            return;

        // Start the DB in id = 2, for some reason id 1 is in being used.
        unique = new AtomicLong(1);

        // Retrieve the model
        Set<Class<? extends RealmObject>> model = realm.getConfiguration().getRealmObjectClasses();

        // Loop through all classes and determine the global maximum value
        for (Class clazz : model) {
            Number num = realm.where(clazz).max("id");

            // If there are no ids for that model class, skip
            if(num != null) {
                AtomicLong maxKey = new AtomicLong(num.longValue());
                if (maxKey.longValue() > unique.longValue())
                    unique = maxKey;
            }
        }
    }

    // Automatically create next key
    public static long nextKey() {
        return unique.incrementAndGet();
    }
}
