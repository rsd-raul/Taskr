package com.software.achilles.tasked.model.repositiories;

import com.software.achilles.tasked.model.domain.Location;
import com.software.achilles.tasked.model.factories.PrimaryKeyFactory;
import io.realm.Realm;
import io.realm.RealmResults;

public class LocationRepository implements BaseRepository<Location> {

    // ---------------------------- Find -----------------------------

    public Location findOne(long id) {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(Location.class).equalTo("id", id).findFirst();
    }

    public RealmResults<Location> findAll() {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(Location.class).findAll();
    }

    // ----------------------------- Add -----------------------------

    public void save(Location location) {
        Realm realm = Realm.getDefaultInstance();
        PrimaryKeyFactory.initialize(realm);

        realm.beginTransaction();

//        Location temp = realm.createObject(Location.class);
//        temp.setId(PrimaryKeyFactory.nextKey());
//        temp.setTitle(location.getTitle());
//        temp.setAddress(location.getAddress());
//        temp.setLatitude(location.getLatitude());
//        temp.setLongitude(location.getLongitude());
//        temp.setFavourite(location.isFavourite());
        if(location.getId() == 0)
            location.setId(PrimaryKeyFactory.nextKey());
        realm.copyToRealmOrUpdate(location);

        realm.commitTransaction();
    }

    // --------------------------- Delete ----------------------------

    public void deleteById(long id) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        Location location = realm.where(Location.class).equalTo("id", id).findFirst();
        location.removeFromRealm();

        realm.commitTransaction();
    }

    public void deleteByPosition(int position) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        RealmResults results = realm.where(Location.class).findAll();
        results.remove(position);

        realm.commitTransaction();
    }
}
