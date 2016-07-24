package com.software.achilles.tasked.model.repositiories;

import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.factories.PrimaryKeyFactory;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class LabelRepository implements BaseRepository<Label> {

    // REVIEW quitar URL cuando este listo completamente
    // http://mlsdev.com/en/blog/47-realm-practical-use-in-android

    // -------------------------- Injected ---------------------------

    Realm realm;

    // ------------------------ Constructor --------------------------

    @Inject
    public LabelRepository(Realm realm) {
        this.realm = realm;
    }

    // ---------------------------- Find -----------------------------

    public Label findOne(long id /*, OnGetLabelByIdCallback callback*/) {
        return realm.where(Label.class).equalTo("id", id).findFirst();
    }

    public RealmResults<Label> findAll(/*, OnGetAllLabelCallback callback*/) {
        return realm.where(Label.class).findAll();
//        if (callback != null)
//            callback.onSuccess(results);
    }

    // ---------------------------- Save -----------------------------

    public void save(final Label label /*, OnAddLabelCallback callback*/) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (label.getId() == 0)
                    label.setId(PrimaryKeyFactory.nextKey());

                realm.copyToRealmOrUpdate(label);
            }
        });
//        if (callback != null)
//            callback.onSuccess();
    }

    // --------------------------- Delete ----------------------------

    public void deleteById(final long Id /*, OnDeleteLabelCallback callback*/) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Label label = realm.where(Label.class).equalTo("id", Id).findFirst();
                label.removeFromRealm();
            }
        });
    }

    public void deleteByPosition(final int position /*, OnDeleteLabelCallback callback*/) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults results = realm.where(Label.class).findAll();
                results.remove(position);
            }
        });
    }
}