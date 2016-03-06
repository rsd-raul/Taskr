package com.software.achilles.tasked.presenter;

import com.software.achilles.tasked.model.controllers.TaskController;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.MainActivity;
import com.software.achilles.tasked.view.fragments.DashboardFragment;

import java.util.ArrayList;
import java.util.List;

public class DashboardPresenter implements Presenter<MainActivity> {

    // --------------------------- Values ----------------------------

    private int mKeyConstant = Constants.DASHBOARD;

    // ------------------------- Attributes --------------------------

    private MainActivity mActivity;
    private DashboardFragment mFragment;

    // ------------------------- Constructor -------------------------

    public DashboardPresenter(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

    public DashboardPresenter(DashboardFragment mFragment){
        this.mFragment = mFragment;
    }

    // ------------------------- Life Cycle --------------------------

    @Override
    public void attachView(MainActivity view) {
        mActivity = view;
    }

    @Override
    public void detachView() {
        mActivity = null;

//      Un-subscribe from the thread?
//        if (subscription != null) subscription.unsubscribe();
    }


    // ---------------------------- Layout ---------------------------

    public void setupLayout(){
        DataManager dataManager = new DataManager();

        // Get data for setting the ViewPager
        ArrayList<TaskList> taskList = dataManager.findAllTaskList();

        // Setup the viewpager
        mFragment.setupViewPager(taskList);
        mFragment.setupTabLayout(taskList.size());

        // Activate a Progress Bar (Circle)
        // Retrieve new data from API if Preferences to Sync -> On App Start
        // Update the ViewPager
        // Deactivate the Progress Bar
    }

    // ---------------------------- Menu -----------------------------
    // -------------------------- Use Cases --------------------------
    // -------------------------- Interface --------------------------
    // --------------------- Add Task Interface ----------------------
    // --------------------------- Details ---------------------------
    // ------------------------ Notifications ------------------------
    // ------------------------- Preferences -------------------------
    // -------------------------- FAB child --------------------------
    // -------------------------- FAB menu ---------------------------

}
