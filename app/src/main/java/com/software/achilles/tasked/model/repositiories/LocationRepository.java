package com.software.achilles.tasked.model.repositiories;

import com.software.achilles.tasked.model.domain.Location;
import com.software.achilles.tasked.model.factories.PrimaryKeyFactory;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class LocationRepository implements BaseRepository<Location> {

    // -------------------------- Injected ---------------------------

    Realm realm;

    // ------------------------ Constructor --------------------------

    @Inject
    public LocationRepository(Realm realm) {
        this.realm = realm;
    }

    // ---------------------------- Find -----------------------------

    public Location findOne(long id) {
        return realm.where(Location.class).equalTo("id", id).findFirst();
    }

    public RealmResults<Location> findAll() {
        return realm.where(Location.class).findAll();
    }

    // ----------------------------- Add -----------------------------

    public void save(final Location location) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (location.getId() == 0) {
                    location.setId(PrimaryKeyFactory.nextKey());
                }

                realm.copyToRealmOrUpdate(location);
            }
        });
    }

    // --------------------------- Delete ----------------------------

    public void deleteById(final long id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Location location = realm.where(Location.class).equalTo("id", id).findFirst();
                location.removeFromRealm();
            }
        });
    }

    public void deleteByPosition(final int position) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults results = realm.where(Location.class).findAll();
                results.remove(position);

                realm.commitTransaction();
            }
        });
    }
}
