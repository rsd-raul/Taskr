package com.software.achilles.tasked.presenter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import com.mikepenz.fastadapter.IItem;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.helpers.DialogsHelper;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.util.Utils;
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
    private boolean timStatus = false, locStatus = false, favStatus = false;

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

    public void setupLayout(long taskId, int listIndex){
        boolean edit = taskId != -1;

        // Retrieve from the DB the task or create a new one
        dataManager.setTemporalTask( edit ? dataManager.findTaskById(taskId) : new Task() );

        // If we are editing, set the necessary items and don´t draw if not necessary
        mFragment.setupLayout(edit);
        if(edit){
            Task task = dataManager.getTemporalTask();

//            enabledFields = new boolean[6];
//            enabledFields[0] = !task.getNotes().isEmpty();
//            enabledFields[1] = task.getDue() != null;
//            enabledFields[2] = task.getLocation() != null;
//            enabledFields[3] = !task.getLabels().isEmpty();
//            enabledFields[4] = task.isStarred();
//            enabledFields[5] = task.isCompleted();

            showDescription(task.getNotes());
            String labelsString = Utils.filterAndFormatLabels(task.getLabels(), null, true);
            showLabels(labelsString, task.getLabels());

            // TODO popular los campos
        }

        // Populate the list field
        String listTitle = dataManager.findAllTaskList().get(listIndex).getTitle();
        mFragment.setTaskListTextView(listTitle, listIndex);

        // FIXME si estas editando necesitas un boton para marcar como completado
        // Save if we are editing, Voice if we are creating
        mFragment.setupSaveOrVoice(edit);
    }

//    public void setupLayout(int listIndex, long itemId){
//
//        // Destroy any existing temporal task
//        dataManager.destroyTemporalTask();
//
//        // Setup a temporal task
//        dataManager.getTemporalTask();
//
//        // Setup the layout
//        String listTitle = dataManager.findAllTaskList().get(listIndex).getTitle();
//        mFragment.setupLayout();
//        mFragment.setTaskListTextView(listTitle, listIndex);
//
//        mFragment.setupSaveOrVoice(false);
//    }

    // -------------------------- Listeners --------------------------

    public void setDescription(String description){
        fromDialogSetItem(description, null, R.id.button_description, false);
    }

    public void setLabels(String labels, RealmList<Label> filtered){
        fromDialogSetItem(labels, filtered, R.id.button_label, false);
    }

    public void showDescription(String description){
        fromDialogSetItem(description, null, R.id.button_description, true);
    }

    public void showLabels(String labels, RealmList<Label> filtered){
        fromDialogSetItem(labels, filtered, R.id.button_label, true);
    }

    private void fromDialogSetItem(String result, @Nullable RealmList<Label> filtered, int detailType, boolean onlyShow){
        boolean colorTrigger = false;

        switch (detailType){

            case R.id.button_description:
                if(Utils.notEmpty(result))
                    colorTrigger = true;
                else
                    result = null;          // If the user removes the description, save null

                if(!onlyShow)
                    dataManager.getTemporalTask().setNotes(result);
                break;

            case R.id.button_time:
                break;

            case R.id.button_location:
                break;

            case R.id.button_label:
                if(Utils.notEmpty(filtered))
                    colorTrigger = true;
                else
                    filtered = null;

                if(!onlyShow)
                    dataManager.getTemporalTask().setLabels(filtered);
                break;

            case R.id.text_task_list:
                break;
        }
        // Set or remove the color from the buttons
        mFragment.colorModifierButton(detailType, colorTrigger);

        // Select the desired action depending on the content and the item existence
        int index = indexOf(detailType);
        if(!colorTrigger && index != -1)
            mFragment.deleteItem(index);
        else
            if(index == -1)
                mFragment.createItem(detailType, result);
            else
                mFragment.editItem(index, detailType, result);
    }

    private int indexOf(int detailType){
        List<IItem> items = mFragment.fastAdapter.getAdapterItems();
        for (int i = 0; i < items.size(); i++)
            if(items.get(i).getIdentifier() == detailType)
                return i;
        return -1;
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
}
