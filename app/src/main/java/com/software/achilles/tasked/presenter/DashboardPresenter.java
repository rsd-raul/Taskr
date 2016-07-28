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

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import io.realm.RealmResults;

@Singleton
public class DashboardPresenter implements Presenter<DashboardFragment> {

    // -------------------------- Injected ---------------------------

    private DataManager dataManager;

    // ------------------------- Constructor -------------------------

    @Inject
    public DashboardPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    // ------------------------- Attributes --------------------------

    private DashboardFragment mFragment;

//    // -------------------------- Singleton --------------------------
//
//    private static final Object lock = new Object();
//    private static volatile DashboardPresenter instance;
//
//    //  Double-checked locking - Effective in Java 1.5 and later:
//    public static DashboardPresenter getInstance() {
//        DashboardPresenter result = instance;
//
//        // Only synchronize if the DashboardPresenter haven't been instantiated
//        if (result == null) {
//            synchronized (lock) {
//                result = instance;
//
//                // If no other threads have instantiated the DashboardPresenter while waiting for the lock.
//                if (result == null) {
//                    result = new DashboardPresenter();
//                    instance = result;
//                }
//            }
//        }
//        return result;
//    }

    // ------------------------- Life Cycle --------------------------

    @Override
    public void attachView(DashboardFragment mFragment) {
        this.mFragment = mFragment;
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
////        if (subscription != null) subscription.unsubscribe();
//    }

    // ---------------------------- Layout ---------------------------

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

    // ---------------------------- Menu -----------------------------

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

    // -------------------------- Use Cases --------------------------

//    @Inject
//    Provider<TaskCreationFragment> taskCreationFragmentProvider;
//    @Inject
//    TaskCreationPresenter taskCreationPresenter;
//    @Inject
//    MainAndFilterDrawerConfigurator mainAndFilterDrawerConfigurator;

    @Inject
    MainPresenter mainPresenter;

    public void itemOnClick(long id){
        mainPresenter.deployEditLayout(id);
        Toast.makeText(mFragment.getContext(), "ItemOnClick", Toast.LENGTH_SHORT).show();
//        Fragment newOne = taskCreationFragmentProvider.get();
//
//        // Send the list the user is on
//        Bundle bundle = new Bundle();
//        bundle.putInt("listIndex", DashboardFragment.mViewPager.getCurrentItem());
//        bundle.putLong("itemId", id);
//        newOne.setArguments(bundle);
//
//        taskCreationPresenter.attachView((TaskCreationFragment) newOne);
//
//        mainAndFilterDrawerConfigurator.customizeActionBar(Constants.ADD_TASK);
//
//        // Initialize the fragment change
//        FragmentTransaction fragmentTransaction = mFragment.getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.main_fragment_container, newOne).commit();
    }

    public void taskModifier(int uniqueParameterId, Task task){
        dataManager.dashTaskModifier(uniqueParameterId, task);
    }

}