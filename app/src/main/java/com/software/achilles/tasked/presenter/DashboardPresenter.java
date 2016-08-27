package com.software.achilles.tasked.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.domain.Location;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.configurators.MainAndFilterDrawerConfigurator;
import com.software.achilles.tasked.view.fragments.DashboardFragment;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

@Singleton
public class DashboardPresenter implements Presenter<DashboardFragment> {

    // -------------------------- INJECTED ---------------------------

    private DataManager dataManager;

    // ------------------------- CONSTRUCTOR -------------------------

    @Inject
    public DashboardPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    // ------------------------- ATTRIBUTES --------------------------

    private DashboardFragment mFragment;

    // ------------------------- LIFE CYCLE --------------------------

    @Override
    public void attachView(DashboardFragment mFragment) {
        this.mFragment = mFragment;
    }

    public DashboardFragment getView() {
        return mFragment;
    }


//    public static void destroyPresenter() {
//        if(instance == null)
//            return;
//
//        instance.mFragment = null;
//        instance = null;
//
////      Un-subscribe from the thread?
////        if (subscription != null) subscription.unsubscribe();
//    }

    // ---------------------------- LAYOUT ---------------------------

    /**
     * Main method to setup the Dashboard interface, mainly the TabLayout and the ViewPager
     */
    public void setupLayout(){

        setupViewPagerAndTabs(false);

        // TODO
        // Activate a Progress Bar (Circle)
        // Retrieve new data from API if Preferences to Sync -> On App Start
        // Update the ViewPager
        // Deactivate the Progress Bar
    }

    // FIXME mFragment is null as Presenter has been destroyed before
    public void setupViewPagerAndTabs(boolean goToEnd){
        // Get data for setting the ViewPager
        RealmResults<TaskList> taskList = dataManager.findAllTaskList();

        // Setup the viewpager
        mFragment.setupViewPager(taskList);
        mFragment.setupTabLayout(taskList.size());

        if(goToEnd)
            DashboardFragment.mViewPager.setCurrentItem(taskList.size()-1, true);
    }

    // ---------------------------- MENU -----------------------------

    public void filterByText(String query, Boolean searchDeep){
        if(query.isEmpty()) {
            // TODO volver a vacio, no dejar simplemente en el ultimo resultado
        }else {
            RealmResults<Task> tasks = dataManager.filterByText(query, searchDeep);

            for (Task aux : tasks) {
                Log.i("TEST", "filterByText: " + aux.getTitle());
            }
        }
//        mFragment.updateViewPagerList(tasks);
    }

    // -------------------------- USE CASES --------------------------

    @Inject
    MainPresenter mainPresenter;

    public void itemOnClick(long id){
        mainPresenter.deployEditLayout(id);
    }

    public void taskModifier(int uniqueParameterId, Task task){
        dataManager.dashTaskModifier(uniqueParameterId, task, null);
    }

    public void setDueDate(long id, Date date){
        Task task = dataManager.findTaskById(id);
        dataManager.dashTaskModifier(Constants.DASH_DATE, task, date);
    }

    public void notifyItemChange(){
        mFragment.notifyItemChange();
    }


    public void reorderLists(int identifier){
        mFragment.reorderLists(identifier);
    }

    // ---------------------------- FILTER ---------------------------

    List<Integer> activeMainFilter = new ArrayList<>();
    List<Label> activeLabelFilter = new ArrayList<>();
    List<Location> activeLocationFilter = new ArrayList<>();
    List<TaskList> activeTaskListFilter = new ArrayList<>();



    public void clearFilter(){
        boolean reset = false;

        if(activeMainFilter.size() > 0) {
            activeMainFilter.clear();
            reset = true;
        }
        if(activeLabelFilter.size() > 0) {
            activeLabelFilter.clear();
            reset = true;
        }
        if(activeLocationFilter.size() > 0) {
            activeLocationFilter.clear();
            reset = true;
        }
        if(activeTaskListFilter.size() > 0) {
            activeTaskListFilter.clear();
            reset = true;
        }

        if(reset) {
            filterAllLists();
            //TODO desceleccionar todos los items del configurator
        }
    }

    public void clearGroupFilter(int identifier){
        boolean reset = false;

        switch (identifier){
            case Constants.COLLAPSIBLE_TASK_LIST:
                if(activeTaskListFilter.size() > 0) {
                    activeTaskListFilter.clear();
                    reset = true;
                }
                break;
            case Constants.COLLAPSIBLE_LABEL_LIST:
                if(activeLabelFilter.size() > 0) {
                    activeLabelFilter.clear();
                    reset = true;
                }
                break;
            case Constants.COLLAPSIBLE_LOCATION_LIST:
                if(activeLocationFilter.size() > 0) {
                    activeLocationFilter.clear();
                    reset = true;
                }
                break;
        }

        if(reset)
            filterAllLists();
    }

    public void filterByMain(int identifier){
        int index = activeMainFilter.indexOf(identifier);
        if(index != -1)
            activeMainFilter.remove(index);
        else
            activeMainFilter.add(identifier);

        filterAllLists();
    }

    public void filterByGrouped(int identifier){
        Label label;
        Location location = null;
        TaskList taskList = null;
        int which = Constants.NONE;

        // Get the type of the element and retrieve that element
        label = dataManager.findLabelById(identifier);
        if(label == null) {
            location = dataManager.findLocationById(identifier);
            if(location == null){
                taskList = dataManager.findTaskListById(identifier);
                if(taskList != null)
                    which = Constants.TASK_LIST;
            }else
                which = Constants.LOCATION;
        }else
            which = Constants.LABEL;

        // React to the element by types
        switch (which){
            case Constants.TASK_LIST:
                if(activeTaskListFilter.contains(taskList))
                    activeTaskListFilter.remove(taskList);
                else
                    activeTaskListFilter.add(taskList);
                break;

            case Constants.LOCATION:
                if(activeLocationFilter.contains(location))
                    activeLocationFilter.remove(location);
                else
                    activeLocationFilter.add(location);
                break;

            case Constants.LABEL:
                if(activeLabelFilter.contains(label))
                    activeLabelFilter.remove(label);
                else
                    activeLabelFilter.add(label);
                break;
            case Constants.NONE:
                Log.e("DashboardPresenter", "filterByGrouped: " + "Item not recognized, thus, not filtered");
                return;
        }

        filterAllLists();
    }

    private void filterAllLists(){
        Log.e("AA", "General: " + activeMainFilter.size());
        Log.e("AA", "TaskList: " + activeTaskListFilter.size());
        Log.e("AA", "Location: " + activeLocationFilter.size());
        Log.e("AA", "Label: " + activeLabelFilter.size());

        mFragment.filterAllLists();
    }

    public List<Task> getFilteredTasksByTaskListPosition(int position){


        RealmQuery<Task> main = dataManager.findAllTasksByTaskListPosition(position).where();

        if(activeMainFilter.contains(Constants.STARRED)) {
            main.equalTo("starred", true);
        } if(activeMainFilter.contains(Constants.DUE_TODAY)) {
            Calendar dateFrom = Calendar.getInstance();
            dateFrom.set(Calendar.HOUR_OF_DAY, 0);
            dateFrom.set(Calendar.MINUTE, 0);
            dateFrom.set(Calendar.SECOND, 0);
            Calendar dateTo = Calendar.getInstance();
            dateTo.set(Calendar.HOUR_OF_DAY, 23);
            dateTo.set(Calendar.MINUTE, 59);
            dateTo.set(Calendar.SECOND, 59);
            main.between("due", dateFrom.getTime(), dateTo.getTime());
        } if(activeMainFilter.contains(Constants.DUE_THIS_WEEK)) {
            Calendar dateFrom = Calendar.getInstance();
            Calendar dateTo = Calendar.getInstance();
            dateTo.add(Calendar.DATE, 7);
            main.between("due", dateFrom.getTime(), dateTo.getTime());
        }

        List<Task> result = new ArrayList<>();
        for (Task task : main.findAll())
            if(isNotFiltered(task))
                result.add(task);

        return result;
    }

    private boolean isNotFiltered(Task task){
        for(Label label : activeLabelFilter)
            if(task.getLabels().contains(label))
                return false;

        for(Location location : activeLocationFilter)
            if(task.getLocation().getId() == location.getId())
                return false;

        return true;
    }
}