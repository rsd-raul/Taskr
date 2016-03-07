package com.software.achilles.tasked.presenter;

import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.fragments.DashboardFragment;
import java.util.ArrayList;

public class DashboardPresenter implements Presenter<DashboardFragment, DashboardPresenter> {

    // --------------------------- Values ----------------------------

    private int mKeyConstant = Constants.DASHBOARD;

    // ------------------------- Attributes --------------------------

    private DashboardFragment mFragment;
    private static DashboardPresenter instance;

    // ------------------------- Constructor -------------------------

    public static DashboardPresenter getInstance() {
        if(instance == null)
            instance = new DashboardPresenter();
        return instance;
    }

    // ------------------------- Life Cycle --------------------------

    @Override
    public DashboardPresenter attachView(DashboardFragment mFragment) {
        this.mFragment = mFragment;
        return instance;
    }

    public static void destroyPresenter() {
        if(instance == null)
            return;

        instance.mFragment = null;
        instance = null;

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

    public static void filterByText(String query, Boolean searchDeep){
        ArrayList<Task> tasks = DataManager.getInstance().filterByText(query, searchDeep);
//        mFragment.updateViewPagerList(tasks);
    }

    // -------------------------- Use Cases --------------------------
    // -------------------------- Interface --------------------------
    // --------------------- Add Task Interface ----------------------
    // --------------------------- Details ---------------------------
    // ------------------------ Notifications ------------------------
    // ------------------------- Preferences -------------------------
    // -------------------------- FAB child --------------------------
    // -------------------------- FAB menu ---------------------------

}
