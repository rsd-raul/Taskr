package com.software.achilles.tasked.model.repositories;

import io.realm.RealmObject;
import io.realm.RealmResults;

public interface BaseRepository<V extends RealmObject> {

    V findOne(long id);

    RealmResults<V> findAll();

    void save(V domain);

    void deleteById(long id);
}
