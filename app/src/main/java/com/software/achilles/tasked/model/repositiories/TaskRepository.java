package com.software.achilles.tasked.model.repositiories;

import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.factories.PrimaryKeyFactory;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class TaskRepository implements BaseRepository<Task> {

    // ---------------------------- Find -----------------------------

    public Task findOne(long id) {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(Task.class).equalTo("id", id).findFirst();
    }

    public RealmResults<Task> findAll() {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(Task.class).findAll();
    }

    // ----------------------------- Add -----------------------------

    public void add(Task task) {
        Realm realm = Realm.getDefaultInstance();
        PrimaryKeyFactory.initialize(realm);

        realm.beginTransaction();

        Task temp = realm.createObject(Task.class);
        temp.setId(PrimaryKeyFactory.nextKey());
        temp.setTitle(task.getTitle());
        temp.setFinished(task.isFinished());
        temp.setStarred(task.isStarred());
        temp.setDescription(task.getDescription());
        temp.setDueDate(task.getDueDate());
        temp.setLocation(task.getLocation());
        RealmList<Label> labels = task.getLabels();
        temp.setLabels(labels != null ? labels : new RealmList<Label>());

        realm.commitTransaction();
    }

    // --------------------------- Delete ----------------------------

    public void deleteById(long id) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        Task task = realm.where(Task.class).equalTo("id", id).findFirst();
        task.removeFromRealm();

        realm.commitTransaction();
    }

    public void deleteByPosition(int position) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        RealmResults results = realm.where(Task.class).findAll();
        results.remove(position);

        realm.commitTransaction();
    }
}
