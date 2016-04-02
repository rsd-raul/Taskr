package com.software.achilles.tasked.model.repositiories;

import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.factories.PrimaryKeyFactory;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class TaskListRepository implements BaseRepository<TaskList> {

    // ---------------------------- Find -----------------------------

    @Override
    public TaskList findOne(long id) {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(TaskList.class).equalTo("id", id).findFirst();
    }

    @Override
    public RealmResults<TaskList> findAll() {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(TaskList.class).findAll();
    }

    public TaskList findByPosition(int position) {

        return findAll().get(position);
    }

    // ----------------------------- Add -----------------------------

    @Override
    public void save(final TaskList taskList) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (taskList.getId() == 0) {
                    PrimaryKeyFactory.initialize(realm);
                    taskList.setId(PrimaryKeyFactory.nextKey());
                } else if (taskList.getTasks() == null)
                    taskList.setTasks(new RealmList<Task>());

                realm.copyToRealmOrUpdate(taskList);
            }
        });
    }

    public void addTaskToTaskList(final long taskListId, final Task task) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                findOne(taskListId).getTasks().add(task);
            }
        });
    }

    // --------------------------- Delete ----------------------------

    @Override
    public void deleteById(final long id) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskList taskList = realm.where(TaskList.class).equalTo("id", id).findFirst();
                taskList.removeFromRealm();
            }
        });
    }

    @Override
    public void deleteByPosition(final int position) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults results = realm.where(TaskList.class).findAll();
                results.remove(position);

            }
        });
    }
}
