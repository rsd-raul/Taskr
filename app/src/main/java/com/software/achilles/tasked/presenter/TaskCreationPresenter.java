package com.software.achilles.tasked.presenter;

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

    public void addOrEditItem(String text, int type){

    }

    public void modifiersColor(boolean color, int type){
        // If it's OFF, turn ON and vice-versa
        desStatus = color;
        mFragment.colorModifierButton(R.id.button_description, desStatus);
    }

    public void modifiersOnClick(View v){
        int id = v.getId();

        switch (id){
            case R.id.button_description:
                // Show field

                String description = dataManager.getTemporalTask().getNotes();
//                DialogsHelper.buildDescriptionDialog(null, null, mFragment.getActivity(), v);
                DialogsHelper.buildDescriptionDialog(description, mFragment.getActivity(), this);

                break;
            case R.id.button_time:
                // Show picker, then deploy result if any

                // If it's OFF, turn ON and vice-versa
                timStatus = !timStatus;
                mFragment.colorModifierButton(id, timStatus);
                break;
            case R.id.button_location:
                // Show picker, then deploy result if any

                // If it's OFF, turn ON and vice-versa
                locStatus = !locStatus;
                mFragment.colorModifierButton(id, locStatus);
                break;
            case R.id.button_label:
                // Show pop up?, then deploy result and color if any

                // If it's OFF, turn ON and vice-versa
                labStatus = !labStatus;
                mFragment.colorModifierButton(id, labStatus);
                break;
            case R.id.button_favourite:

                // If it's OFF, turn ON and vice-versa
                favStatus = !favStatus;

                // Save
                dataManager.getTemporalTask().setStarred(favStatus);

                mFragment.colorModifierButton(id, favStatus);
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
        }
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

    public void detailOnClick(int detailType, TaskDetailFAItem item, final View view){

        switch (detailType){
            case Constants.DETAIL_DESCRIPTION:

                String description = dataManager.getTemporalTask().getNotes();
                DialogsHelper.buildDescriptionDialog(description, item, mFragment.getActivity(), view);
                break;
            case Constants.DETAIL_ALARM:
                break;
            case Constants.DETAIL_LABELS:

                // Get all labels and format them to be shown
                RealmResults<Label> items = dataManager.findAllLabels();
                List<String> labels = new ArrayList<>();
                for (int i = 0; i < items.size(); i++)
                    labels.add(items.get(i).getTitle());

                // Get labels for the temporal object and format them to be selected
                RealmList<Label> temporal = dataManager.getTemporalTask().getLabels();
                Integer[] selected = new Integer[temporal.size()];
                for (int i = 0; i < temporal.size(); i++)
                    selected[i] = indexOf(items, temporal.get(i));



                DialogsHelper.buildChoiceFromListMulti(labels, selected, item, mFragment.getActivity(), view);
                break;
            case Constants.DETAIL_LOCATION:
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

    public void setDescription(String description){
        dataManager.getTemporalTask().setNotes(description);
    }

    public void setLabels(Integer[] labelIndexes){
        RealmResults<Label> labels = dataManager.findAllLabels();
        RealmList<Label> filtered = new RealmList<>();

        for(Integer index : labelIndexes)
                filtered.add(labels.get(index));

        dataManager.getTemporalTask().setLabels(filtered);
    }


}
