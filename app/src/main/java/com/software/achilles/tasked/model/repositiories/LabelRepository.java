package com.software.achilles.tasked.model.repositiories;

import com.software.achilles.tasked.model.domain.Label;
import java.util.UUID;
import io.realm.Realm;

public class LabelRepository {

    public void addUniversity(Label label /*, OnAddUniversityCallback callback*/) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Label u = realm.createObject(Label.class);
//        u.setId(UUID.randomUUID());       TODO generate Unique ID
        u.setTitle(label.getTitle());
        realm.commitTransaction();

        /*
        if (callback != null)
            callback.onSuccess();
        */
    }
}
