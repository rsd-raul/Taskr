package com.software.achilles.tasked.model.repositiories;

import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.factories.PrimaryKeyFactory;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class TaskListRepository implements BaseRepository<TaskList> {

    // -------------------------- Injected ---------------------------

    Realm realm;

    // ------------------------ Constructor --------------------------

    @Inject
    public TaskListRepository(Realm realm) {
        this.realm = realm;
    }

    // ---------------------------- Find -----------------------------

    @Override
    public TaskList findOne(long id) {
        return realm.where(TaskList.class).equalTo("id", id).findFirst();
    }

    @Override
    public RealmResults<TaskList> findAll() {
        return realm.where(TaskList.class).findAll();
    }

    public TaskList findByPosition(int position) {
        return findAll().get(position);
    }

    // ----------------------------- Add -----------------------------

    @Override
    public void save(final TaskList taskList) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (taskList.getId() == 0) {
                    taskList.setId(PrimaryKeyFactory.nextKey());
                } else if (taskList.getTasks() == null)
                    taskList.setTasks(new RealmList<Task>());

                realm.copyToRealmOrUpdate(taskList);
            }
        });
    }

    public void addTaskToTaskList(final int taskListPosition, final Task task) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                findByPosition(taskListPosition).getTasks().add(task);
            }
        });
    }

    // --------------------------- Delete ----------------------------

    @Override
    public void deleteById(final long id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskList taskList = realm.where(TaskList.class).equalTo("id", id).findFirst();
                taskList.removeFromRealm();
            }
        });
    }

    @Override
    public void deleteByPosition(final int position) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults results = realm.where(TaskList.class).findAll();
                results.remove(position);

            }
        });
    }
}
