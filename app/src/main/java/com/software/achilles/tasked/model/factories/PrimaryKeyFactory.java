package com.software.achilles.tasked.model.factories;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import io.realm.Realm;
import io.realm.RealmObject;

public class PrimaryKeyFactory {

    // Model classes
    private static AtomicLong unique;


//    public synchronized void initialize(Realm realm) {
    public static void initialize(Realm realm) {
        // If unique already calculated, skip
        if (unique != null)
            return;

        // Retrieve the model
        Set<Class<? extends RealmObject>> model = realm.getConfiguration().getRealmObjectClasses();

        // Start the DB in id = 2, for some reason id 1 is in being used.
        unique = new AtomicLong(1);

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
//    public synchronized long nextKey() {
    public static long nextKey() {
        return unique.incrementAndGet();
    }
}

//        private static Map<Class, AtomicLong> keys = new HashMap<>();
//
//    public static void initialize(Realm realm) {
//
//        // If keys have being initialized, don't do nothing
//        if(!keys.isEmpty())
//            return;
//
//        // 1. Loop through all classes using RealmSchema / RealmObjectSchema / RealmConfiguration.getRealmObjectClassees()
//        // 2. Determine the maximum value for each primary key field and save it in the `keys` map.
//        keys.put(Location.class, new AtomicLong(realm.where(Location.class).max("id").longValue()));
//        keys.put(Label.class, new AtomicLong(realm.where(Label.class).max("id").longValue()));
//        keys.put(Task.class, new AtomicLong(realm.where(Task.class).max("id").longValue()));
//        keys.put(TaskList.class, new AtomicLong(realm.where(TaskList.class).max("id").longValue()));
//    }
//
//    // Automatically create next key
//    public synchronized long nextKey(Class clazz) {
//        return keys.get(clazz).incrementAndGet();
//    }
//}