package com.software.achilles.tasked.presenter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mikepenz.fastadapter.IItem;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.domain.Location;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.util.helpers.DatabaseHelper;
import com.software.achilles.tasked.util.helpers.DialogsHelper;
import com.software.achilles.tasked.util.helpers.LocalisationHelper;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.util.Utils;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;
import java.util.ArrayList;
import java.util.Date;
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

    // ------------------------- CONSTRUCTOR -------------------------

    @Inject
    public TaskCreationPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    // -------------------------- INJECTED ---------------------------

    @Inject
    MainPresenter mainPresenter;

    // ------------------------- ATTRIBUTES --------------------------

    private TaskCreationFragment mFragment;
    private boolean locStatus = false, favStatus = false;

    // ------------------------- LIFE CYCLE --------------------------

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

    // ---------------------------- LAYOUT ---------------------------

    public void setupLayout(long taskId, int listIndex){
        boolean edit = taskId != -1;

        // Setup the basic layout
        mFragment.setupLayout(edit);

        // Create a new task or retrieve it from the DB
        Task temporal;
        TaskList oldTaskList;
        if(!edit) {
            temporal = new Task();
            oldTaskList = null;

        } else {
            // Remove the direct connection with Realm so you can edit it
            temporal = DatabaseHelper.removeRealmFromTask(dataManager.findTaskById(taskId));
            oldTaskList = temporal.getTaskList();

            // Set the necessary items and don´t draw if not necessary
            showStarred(temporal.isStarred());

            showTitle(temporal.getTitle());

            showDescription(temporal.getNotes());

            String labels = LocalisationHelper.filterAndFormatLabels(temporal.getLabels(), null, true);
            showLabels(labels, temporal.getLabels());

            if(temporal.getDue() != null)
                showDueDate(LocalisationHelper.dateToDateTimeString(temporal.getDue()));

            if(temporal.getLocation() != null)
                showLocation(temporal.getLocation().getTitle());
        }
        dataManager.setTemporalTask(temporal);
        dataManager.setOldTaskList(oldTaskList);

        // Populate the list field
        String listTitle = dataManager.findAllTaskList().get(listIndex).getTitle();
        mFragment.setTaskListTextView(listTitle, listIndex);


        // Save if we are editing, Voice if we are creating
        mFragment.setupSaveOrVoice(edit);
    }

    // -------------------------- Listeners --------------------------

    public void setDescription(String description){
        fromDialogSetItem(description, null, null, null, R.id.button_description, false);
    }
    public void showDescription(String description){
        fromDialogSetItem(description, null, null, null, R.id.button_description, true);
    }

    public void setLabels(String labels, RealmList<Label> filtered){
        fromDialogSetItem(labels, filtered, null, null, R.id.button_label, false);
    }
    public void showLabels(String labels, RealmList<Label> filtered){
        fromDialogSetItem(labels, filtered, null, null, R.id.button_label, true);
    }

    public void setDueDate(String date, Date dueDate){
        fromDialogSetItem(date, null, dueDate, null, R.id.button_time, false);
    }
    public void showDueDate(String date){
        fromDialogSetItem(date, null, null, null, R.id.button_time, true);
    }

    public void setLocation(String locationStr, Location location){
        fromDialogSetItem(locationStr, null, null, location, R.id.button_location, false);
    }
    public void showLocation(String location){
        fromDialogSetItem(location, null, null, null, R.id.button_location, true);
    }

    public void showStarred(boolean starred){
        mFragment.colorModifierButton(R.id.button_favourite, starred);
    }

    public void showTitle(String title){
        mFragment.setTaskNameTextView(title);
    }


    private void fromDialogSetItem(String result, @Nullable RealmList<Label> filtered, Date date,
                                    Location location, int detailType, boolean onlyShow){
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

            case R.id.button_label:
                if(Utils.notEmpty(filtered))
                    colorTrigger = true;
                else
                    filtered = null;

                if(!onlyShow)
                    dataManager.getTemporalTask().setLabels(filtered);
                break;

            case R.id.button_time:

                if(Utils.notEmpty(result))
                    colorTrigger = true;
                else
                    date = null;

                if(!onlyShow)
                    dataManager.getTemporalTask().setDue(date);
                break;

            case R.id.button_location:
                if(Utils.notEmpty(result))
                    colorTrigger = true;
                else
                    location = null;

                if(!onlyShow)
                    dataManager.getTemporalTask().setLocation(location);
                break;
        }
        // Set or remove the color from the buttons
        mFragment.colorModifierButton(detailType, colorTrigger);

        // Select the desired action depending on the content and the item existence
        int index = indexOf(detailType);
        if(!colorTrigger && index != -1)
            mFragment.deleteItem(index);
        if(colorTrigger)
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
        temporal.setTaskList(dataManager.findTaskListByPosition(taskListPosition));
        dataManager.saveTask(temporal);

        // If long press, restart all fields, if short, back to Dashboard
        if(reset)
            mFragment.resetFields();
        else
            mainPresenter.backToBack();
    }

    public void itemOnClick(int detailType){

        Task temporal = dataManager.getTemporalTask();

        switch (detailType){

            case R.id.button_description:
                String description = temporal.getNotes();
                DialogsHelper.buildDescriptionDialog(description, mFragment.getActivity(), this);
                break;

            case R.id.button_label:
                // Get all labels and format them to be shown
                RealmResults<Label> items = dataManager.findAllLabels();

                // Get labels for the temporal object and format them to be selected
                RealmList<Label> temporalLabels = temporal.getLabels();

                Integer[] selected = null;
                if(temporalLabels != null){
                    selected = new Integer[temporalLabels.size()];

                    int count = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (temporalLabels.contains(items.get(i))) {
                            selected[count] = i;
                            count++;
                        }
                    }
                }
                DialogsHelper.buildLabelDialogMulti(items, selected, mFragment.getActivity(), this);
                break;

            case R.id.button_time:
                DialogsHelper.buildDateTimePicker(mFragment, temporal.getDue(), this);
                break;

            case R.id.button_location:
                Location location = temporal.getLocation();
                double[] bounds = location != null ? location.getBounds() : null;

                DialogsHelper.buildPlacePicker(mFragment.getActivity(), bounds);
                break;

            case R.id.button_favourite:
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

    public void processPlacePicker(Intent data){
        Place place = PlacePicker.getPlace(mFragment.getActivity(), data);
        if(place.getViewport() == null)
            return;

        // Extract the information to populate our Location model
        LatLngBounds viewport = place.getViewport();
        String name = place.getName().toString(), address = place.getAddress().toString();
        double lat = place.getLatLng().latitude, lon = place.getLatLng().longitude;
        double[] bounds = new double[]{ viewport.southwest.latitude, viewport.southwest.longitude,
                    viewport.northeast.latitude, viewport.northeast.longitude };

        // Set the item and save the location in the temporal task
        setLocation(name, new Location(name, address, lat, lon, bounds, false));
    }
}
