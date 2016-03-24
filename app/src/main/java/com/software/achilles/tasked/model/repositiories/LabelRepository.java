package com.software.achilles.tasked.model.repositiories;

import android.util.Log;

import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.factories.PrimaryKeyFactory;

import java.security.KeyFactory;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class LabelRepository {

    // TODO quitar URL cuando todo funcione
    // http://mlsdev.com/en/blog/47-realm-practical-use-in-android

    public void add(Label label /*, OnAddLabelCallback callback*/) {
        Realm realm = Realm.getDefaultInstance();
        PrimaryKeyFactory.initialize(realm);

        realm.beginTransaction();

            Label temp = realm.createObject(Label.class);
            temp.setId(PrimaryKeyFactory.nextKey());
            temp.setTitle(label.getTitle());
            temp.setColorRes(label.getColorRes());

        realm.commitTransaction();
        
//        if (callback != null)
//            callback.onSuccess();
    }

    public void deleteById(String Id /*, OnDeleteLabelCallback callback*/) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
            Label label = realm.where(Label.class).equalTo("id", Id).findFirst();
            label.removeFromRealm();
        realm.commitTransaction();

//        if (callback != null)
//            callback.onSuccess();
    }

    public void deleteByPosition(int position /*, OnDeleteLabelCallback callback*/) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

            RealmQuery query = realm.where(Label.class);
            RealmResults results = query.findAll();
            results.remove(position);

        realm.commitTransaction();

//        if (callback != null)
//            callback.onSuccess();
    }

    public Label findOneById(String id /*, OnGetLabelByIdCallback callback*/) {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(Label.class).equalTo("id", id).findFirst();
//        if (callback != null)
//            callback.onSuccess(result);
    }

    public RealmResults<Label> findAll(/*, OnGetAllLabelCallback callback*/) {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(Label.class).findAll();
//        if (callback != null)
//            callback.onSuccess(results);
    }
}
