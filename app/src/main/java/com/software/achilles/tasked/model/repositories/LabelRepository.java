package com.software.achilles.tasked.model.repositories;

import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.factories.PrimaryKeyFactory;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class LabelRepository implements BaseRepository<Label> {

    // -------------------------- Injected ---------------------------

    Realm realm;

    // ------------------------ Constructor --------------------------

    @Inject
    public LabelRepository(Realm realm) {
        this.realm = realm;
    }

    // ---------------------------- Find -----------------------------

    public Label findOne(long id) {
        return realm.where(Label.class).equalTo("id", id).findFirst();
    }

    public RealmResults<Label> findAll() {
        return realm.where(Label.class).findAll();
    }

    // ---------------------------- Save -----------------------------

    public void save(final Label label) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (label.getId() == 0)
                    label.setId(PrimaryKeyFactory.nextKey());

                realm.copyToRealmOrUpdate(label);
            }
        });
    }

    // --------------------------- Delete ----------------------------

    public void deleteById(final long Id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Label label = realm.where(Label.class).equalTo("id", Id).findFirst();
                label.deleteFromRealm();
            }
        });
    }

    public void deleteByPosition(final int position /*, OnDeleteLabelCallback callback*/) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Label> results = realm.where(Label.class).findAll();
                results.get(position).deleteFromRealm();
            }
        });
    }
}