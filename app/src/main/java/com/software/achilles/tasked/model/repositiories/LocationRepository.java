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

    public void save(final Location location) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (location.getId() == 0) {
                    PrimaryKeyFactory.initialize(realm);
                    location.setId(PrimaryKeyFactory.nextKey());
                }

                realm.copyToRealmOrUpdate(location);
            }
        });
    }

    // --------------------------- Delete ----------------------------

    public void deleteById(final long id) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Location location = realm.where(Location.class).equalTo("id", id).findFirst();
                location.removeFromRealm();
            }
        });
    }

    public void deleteByPosition(final int position) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults results = realm.where(Location.class).findAll();
                results.remove(position);

                realm.commitTransaction();
            }
        });
    }
}
