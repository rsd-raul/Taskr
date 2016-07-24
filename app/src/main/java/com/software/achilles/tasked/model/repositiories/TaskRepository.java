package com.software.achilles.tasked.model.repositiories;

import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.factories.PrimaryKeyFactory;
import com.software.achilles.tasked.util.Constants;

import javax.inject.Inject;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class TaskRepository implements BaseRepository<Task> {

    // -------------------------- Injected ---------------------------

    Realm realm;

    // ------------------------ Constructor --------------------------

    @Inject
    public TaskRepository(Realm realm) {
        this.realm = realm;
    }

    // ---------------------------- Find -----------------------------

    @Override
    public Task findOne(long id) {
        return realm.where(Task.class).equalTo("id", id).findFirst();
    }

    @Override
    public RealmResults<Task> findAll() {
        return realm.where(Task.class).findAll();
    }

    public RealmList<Task> findAllByTaskListPosition(int position){
        return realm.where(TaskList.class).findAll().get(position).getTasks();
    }

    public RealmResults<Task> findAllByTitle(String query){
        return realm.where(Task.class).contains("title", query, Case.INSENSITIVE).findAll();
    }

    // ----------------------------- Add -----------------------------

    @Override
    public void save(final Task task) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (task.getId() == 0) {
                    task.setId(PrimaryKeyFactory.nextKey());
                }

                realm.copyToRealmOrUpdate(task);
            }
        });
    }

    // --------------------------- Delete ----------------------------

    @Override
    public void deleteById(final long id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Task task = realm.where(Task.class).equalTo("id", id).findFirst();
                task.removeFromRealm();
            }
        });
    }

    @Override
    public void deleteByPosition(final int position) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults results = realm.where(Task.class).findAll();
                results.remove(position);
            }
        });
    }

    public void taskModifier(final int uniqueParameterId, final Task task){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                switch (uniqueParameterId){

                    case Constants.DASH_DONE:
                        task.setCompleted(!task.isCompleted());
                        break;

                    case Constants.DASH_FAVE:
                        task.setStarred(!task.isStarred());
                        break;

                    case Constants.DASH_DATE:
//                     TODO   Retrieve the date from SOME place (DataManager?) and save it
                        break;
                }
            }
        });
    }
}

