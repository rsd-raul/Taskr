package com.software.achilles.tasked.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.github.clans.fab.FloatingActionMenu;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.controllers.TaskController;
import com.software.achilles.tasked.model.managers.ThreadManager;
import com.software.achilles.tasked.presenter.DashboardPresenter;
import com.software.achilles.tasked.presenter.TaskCreationPresenter;
import com.software.achilles.tasked.view.fragments.DashboardFragment;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;
import com.software.achilles.tasked.view.configurators.FloatingActionMenuConfigurator;
import com.software.achilles.tasked.view.configurators.MainAndFilterDrawerConfigurator;
import com.software.achilles.tasked.util.Constants;

public class MainActivity extends AppCompatActivity {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    public FloatingActionMenuConfigurator mFamConfigurator;
    public MainAndFilterDrawerConfigurator mDrawersConfigurator;

    private TaskController mTaskController = TaskController.getInstance();

    // ------------------------- Constructor -------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set ActionBar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Configure the drawers, both main and filter
        mDrawersConfigurator = new MainAndFilterDrawerConfigurator(this);

        // Initialize with Dashboard
        setFragment(Constants.DASHBOARD);
    }

    // -------------------------- Landscape --------------------------

    // ------------------------ Actionbar Menu -----------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        switch (currentFragmentKey){

            case Constants.DASHBOARD:
                getMenuInflater().inflate(R.menu.menu_dashboard, menu);

                MenuItem menuItem = menu.findItem(R.id.action_search);
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
                // TODO Filtering
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        DashboardPresenter.filterByText(query, true);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String query) {
                        DashboardPresenter.filterByText(query, false);
                        return false;
                    }
                });
                break;

            case Constants.ADD_TASK:
                getMenuInflater().inflate(R.menu.menu_create_task, menu);
                break;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_filter:
                mDrawersConfigurator.mFilterDrawer.openDrawer();
                break;

//            case R.id.action_search:
//                 The calculations and filtering are done in "onCreateOptionsMenu"
//                break;

        }
        return super.onOptionsItemSelected(item);
    }

    // -------------------------- Use Cases --------------------------

    // --------------------- Add Task Interface ----------------------

    public void onClickTaskCustomization(View view){
//        switch (view.getId()){
//            // Close interface
////            case R.id.button_close:       // DEPRECATED no more close button
////                dialogForTaskRemoval();
////                break;
//            // Task customization buttons
//            default:
//                mTaskCreationFragment.taskCustomization(view);
//                break;
//
//        }
    }

    public void deployAddTask() {

        // Deploy the layout
        setFragment(Constants.ADD_TASK);

        // Hide the fam
        mFamConfigurator.famVisibility(false);

        // Block filter and navigation drawers
        mDrawersConfigurator.blockDrawers(true);
    }

    public void removeAddTask(){

        setFragment(Constants.DASHBOARD);

        // Show the FAM
        mFamConfigurator.famVisibility(true);

        // Unblock filter and navigation drawers
        mDrawersConfigurator.blockDrawers(false);
    }

    // --------------------------- Details ---------------------------

    @Override
    public void onBackPressed() {
        FloatingActionMenu fam = (FloatingActionMenu) findViewById(R.id.menuFAB);

        if(fam.isOpened() || mDrawersConfigurator.mMainDrawer.isDrawerOpen()) {
            fam.close(true);
            mDrawersConfigurator.mMainDrawer.closeDrawer();

        // Add mDrawersConfigurator.mFilterDrawer != null && if not only on dashboard
        } else if(mDrawersConfigurator.mFilterDrawer.isDrawerOpen()) {
            mDrawersConfigurator.mFilterDrawer.closeDrawer();
        } else
            super.onBackPressed();
    }

    // ------------------------ Notifications ------------------------

    // ------------------------- Preferences -------------------------

    // ------------------------- Deprecated --------------------------

    // ------------------- Fragment and Presenter --------------------

    private int currentFragmentKey;
    private DashboardPresenter mDashboardPresenter;
    private TaskCreationPresenter mTaskCreationPresenter;

    public void setFragment(int keyConstant) {
        if(keyConstant == currentFragmentKey)
            return;
        currentFragmentKey = keyConstant;

        Fragment newOne = null;

        switch (keyConstant) {
            case Constants.DASHBOARD:
                DashboardFragment dashboardFragment = new DashboardFragment();
                newOne = dashboardFragment;

                // Unblock filter and navigation drawers, show FAM
                if(mFamConfigurator != null) {
                    mFamConfigurator.famVisibility(true);
                    mDrawersConfigurator.blockDrawers(false);
                    break;
                }

                // Configure the fab menu and its children. - NEW THREAD
                final MainActivity aux = this;
                ThreadManager.launchIfPossible(new Runnable() {
                    public void run() {
                        mFamConfigurator = new FloatingActionMenuConfigurator(aux);
                    }
                });

                mDashboardPresenter = new DashboardPresenter(dashboardFragment);
                mTaskCreationPresenter = null;

                break;
            case Constants.ADD_TASK:
                TaskCreationFragment taskCreationFragment = new TaskCreationFragment();
                newOne = taskCreationFragment;

                mTaskCreationPresenter = new TaskCreationPresenter(taskCreationFragment);
                mDashboardPresenter = null;

                break;
        }

        mDrawersConfigurator.customizeActionBar(keyConstant);

        if(newOne == null)
            return;

        // Initialize the fragment change
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, newOne).commit();
    }
}