package com.software.achilles.tasked.model.repositiories;

import android.util.Log;

import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.factories.PrimaryKeyFactory;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class TaskRepository implements BaseRepository<Task> {

    // ---------------------------- Find -----------------------------

    @Override
    public Task findOne(long id) {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(Task.class).equalTo("id", id).findFirst();
    }

    @Override
    public RealmResults<Task> findAll() {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(Task.class).findAll();
    }

    // ----------------------------- Add -----------------------------

    @Override
    public void add(Task task) {
        Realm realm = Realm.getDefaultInstance();
        PrimaryKeyFactory.initialize(realm);

        realm.beginTransaction();

        // Failing
//        Task temp = realm.createObject(Task.class);
//        temp.setId(PrimaryKeyFactory.nextKey());
//        temp.setTitle(task.getTitle());
//        temp.setFinished(task.isFinished());
//        temp.setStarred(task.isStarred());
//        temp.setDescription(task.getDescription());
//        temp.setDueDate(task.getDueDate());
//        temp.setLocation(task.getLocation());
//        RealmList<Label> labels = task.getLabels();
//        temp.setLabels(labels != null ? labels : new RealmList<Label>());

        // Working
        task.setId(PrimaryKeyFactory.nextKey());
        realm.copyToRealmOrUpdate(task);

        realm.commitTransaction();
    }

    // --------------------------- Delete ----------------------------

    @Override
    public void deleteById(long id) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        Task task = realm.where(Task.class).equalTo("id", id).findFirst();
        task.removeFromRealm();

        realm.commitTransaction();
    }

    @Override
    public void deleteByPosition(int position) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        RealmResults results = realm.where(Task.class).findAll();
        results.remove(position);

        realm.commitTransaction();
    }
}
