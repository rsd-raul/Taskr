package com.software.achilles.tasked.model.repositiories;

import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.factories.PrimaryKeyFactory;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class TaskListRepository implements BaseRepository<TaskList> {

    // ---------------------------- Find -----------------------------

    public TaskList findOne(long id) {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(TaskList.class).equalTo("id", id).findFirst();
    }

    public RealmResults<TaskList> findAll() {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(TaskList.class).findAll();
    }

    // ----------------------------- Add -----------------------------

    public void add(TaskList taskList) {
        Realm realm = Realm.getDefaultInstance();
        PrimaryKeyFactory.initialize(realm);

        realm.beginTransaction();

        TaskList temp = realm.createObject(TaskList.class);
        temp.setId(PrimaryKeyFactory.nextKey());
        temp.setTitle(taskList.getTitle());
        RealmList<Task> tasks = taskList.getTasks();
        temp.setTasks(tasks != null ? tasks : new RealmList<Task>());

        realm.commitTransaction();
    }

    // --------------------------- Delete ----------------------------

    public void deleteById(long id) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        TaskList taskList = realm.where(TaskList.class).equalTo("id", id).findFirst();
        taskList.removeFromRealm();

        realm.commitTransaction();
    }

    public void deleteByPosition(int position) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        RealmResults results = realm.where(TaskList.class).findAll();
        results.remove(position);

        realm.commitTransaction();
    }
}
