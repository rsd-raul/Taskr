package com.software.achilles.tasked.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.helpers.DialogsHelper;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.util.Utils;
import com.software.achilles.tasked.view.adapters.TaskDetailFAItem;
import com.software.achilles.tasked.view.adapters.TaskFAItem;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.RealmList;
import io.realm.RealmResults;

@Singleton
public class TaskCreationPresenter
        implements Presenter<TaskCreationFragment> {

    // -------------------------- Injected ---------------------------

    private DataManager dataManager;

    // ------------------------- Constructor -------------------------

    @Inject
    public TaskCreationPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    // ------------------------- Attributes --------------------------

    @Inject
    MainPresenter mainPresenter;

    private TaskCreationFragment mFragment;
    private boolean desStatus = false, timStatus = false, locStatus = false,
            labStatus = false, favStatus = false;

//    // -------------------------- Singleton --------------------------
//
//    private static final Object lock = new Object();
//    private static volatile TaskCreationPresenter instance;
//
//    //  Double-checked locking - Effective in Java 1.5 and later:
//    public static TaskCreationPresenter getInstance() {
//        TaskCreationPresenter result = instance;
//
//        // Only synchronize if the TaskCreationPresenter haven't been instantiated
//        if (result == null) {
//            synchronized (lock) {
//                result = instance;
//
//                // If no other threads have instantiated the TaskCreationPresenter while waiting for the lock.
//                if (result == null) {
//                    result = new TaskCreationPresenter();
//                    instance = result;
//                }
//            }
//        }
//        return result;
//    }

    // ------------------------- Life Cycle --------------------------

    @Override
    public void attachView(TaskCreationFragment view) {
        mFragment = view;
//        return instance;
    }

//    public static void destroyPresenter() {
//        if(instance == null)
//            return;
//
//        instance.mFragment = null;
//        instance = null;
//
////      Un-subscribe from the thread?
////        if (subscription != null)
////            subscription.unsubscribe();
//    }

    // ---------------------------- Layout ---------------------------

    public void setupLayout(int listIndex){

        // Destroy any existing temporal task
        dataManager.destroyTemporalTask();

        // Setup a temporal task
        dataManager.getTemporalTask();

        // Setup the layout
        String listTitle = dataManager.findAllTaskList().get(listIndex).getTitle();
        mFragment.setupLayout();
        mFragment.setTaskListTextView(listTitle, listIndex);

        mFragment.setupSaveOrVoice(false);
    }

    // -------------------------- Listeners --------------------------

    public void setDescription(String description){
        fromDialogSetItem(description, null, R.id.button_description);
    }

    public void setLabels(String labels, RealmList<Label> filtered){
        fromDialogSetItem(labels, filtered, R.id.button_label);
    }

    private void fromDialogSetItem(String result, @Nullable RealmList<Label> filtered, int type){
        boolean colorTrigger = false;

        switch (type){
            case R.id.button_description:
                if(Utils.notEmpty(result)){
                    desStatus = true;
                    colorTrigger = true;
                }else{
                    desStatus = false;
                    result = null;          // If the user removes the description, save null
                }
                dataManager.getTemporalTask().setNotes(result);
                break;
            case R.id.button_time:
                break;
            case R.id.button_location:
                break;
            case R.id.button_label:
                if(Utils.notEmpty(filtered)){
                    labStatus = true;
                    colorTrigger = true;
                }else{
                    labStatus = false;
                    filtered = null;
                }

                dataManager.getTemporalTask().setLabels(filtered);
                break;
            case R.id.text_task_list:
                break;
        }

        mFragment.colorModifierButton(type, colorTrigger);
    }







    public boolean isDataPresent(){
        return mFragment.isDataPresent();
    }

    // -------------------------- Use Cases --------------------------

    public void saveTask(boolean reset){

//        mFragment.populateAndGetTemporal();
        Task temporal = mFragment.populateAndGetTemporal();
        int taskListPosition = dataManager.getTemporalTaskListPosition();
        dataManager.saveTask(taskListPosition, temporal);

        Log.d("TaskCreationPresenter", "" +
                dataManager.getTemporalTask().getTitle()
                + " " + dataManager.getTemporalTask().isStarred()
                + " " + dataManager.getTemporalTask().isCompleted()
                + " " + dataManager.getTemporalTask().getNotes()
                + " " + dataManager.getTemporalTask().getDue()
                + " " + dataManager.getTemporalTask().getLabels()
                + " " + dataManager.getTemporalTask().getLocation()
                + " " + dataManager.getTemporalTask().getId()
        );

        dataManager.destroyTemporalTask();

        // If long press, restart all fields, if short, back to Dashboard
        if(reset)
            mFragment.resetFields();
        else
            mainPresenter.backToBack();
    }

    public void iconOnClick(View v){
        itemOnClick(v.getId());
    }

    public void itemOnClick(int detailType){

        switch (detailType){
            case R.id.button_description:

                String description = dataManager.getTemporalTask().getNotes();
                DialogsHelper.buildDescriptionDialog(description, mFragment.getActivity(), this);
                break;
            case R.id.button_time:
                // Show picker, then deploy result if any

                // If it's OFF, turn ON and vice-versa
                timStatus = !timStatus;
                mFragment.colorModifierButton(detailType, timStatus);
                break;
            case R.id.button_label:
                // Get all labels and format them to be shown
                RealmResults<Label> items = dataManager.findAllLabels();

                // Get labels for the temporal object and format them to be selected
                RealmList<Label> temporal = dataManager.getTemporalTask().getLabels();

                Integer[] selected = null;
                if(temporal != null){
                    selected = new Integer[temporal.size()];

                    int count = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (temporal.contains(items.get(i))) {
                            selected[count] = i;
                            count++;
                        }
                    }
                }
                DialogsHelper.buildLabelDialogMulti(items, selected, mFragment.getActivity(), this);
                break;
            case R.id.button_location:
                // Show picker, then deploy result if any

                // If it's OFF, turn ON and vice-versa
                locStatus = !locStatus;
                mFragment.colorModifierButton(detailType, locStatus);
                break;
            case R.id.button_favourite:

                // If it's OFF, turn ON and vice-versa
                favStatus = !favStatus;

                // Save
                dataManager.getTemporalTask().setStarred(favStatus);

                mFragment.colorModifierButton(detailType, favStatus);
                break;
            case R.id.text_task_list:

                List<String> taskListTitles = new ArrayList<>();

                // Get data for setting the ViewPager
                RealmResults<TaskList> taskList = dataManager.findAllTaskList();

                // Format that data for the spinner
                for (int i = 0; i < taskList.size(); i++)
                    taskListTitles.add(taskList.get(i).getTitle());

                DialogsHelper.buildChoiceFromList(taskListTitles, dataManager.getTemporalTaskListPosition() ,mFragment);

                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private int indexOf(RealmResults<Label> items, Label temporal){
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(temporal)) {
                return i;
            }
        }
        return -1;
    }
}
