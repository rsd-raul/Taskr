package com.software.achilles.tasked.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.configurators.MainAndFilterDrawerConfigurator;
import com.software.achilles.tasked.view.fragments.DashboardFragment;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

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
}