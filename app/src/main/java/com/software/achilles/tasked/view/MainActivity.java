package com.software.achilles.tasked.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import com.github.clans.fab.FloatingActionMenu;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.controllers.TaskController;
import com.software.achilles.tasked.model.managers.ThreadManager;
import com.software.achilles.tasked.view.fragments.DashboardFragment;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;
import com.software.achilles.tasked.view.configurators.FloatingActionMenuConfigurator;
import com.software.achilles.tasked.view.configurators.MainAndFilterDrawerConfigurator;
import com.software.achilles.tasked.util.Constants;

public class MainActivity extends AppCompatActivity {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private FloatingActionMenuConfigurator mFamConfigurator;
    private MainAndFilterDrawerConfigurator mDrawersConfigurator;

    private TaskController mTaskController;
    public Toolbar mToolbar;
    public ViewPager mViewPager;
    public TaskCreationFragment mTaskCreationFragment;

    // ------------------------- Constructor -------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TaskController
        mTaskController = TaskController.getInstance();

        // Set ActionBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Configure the drawers, both main and filter
        mDrawersConfigurator = new MainAndFilterDrawerConfigurator(this);

        // Initialize with Dashboard
        setFragment(Constants.DASHBOARD);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
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
                        // Perform the final search
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // Text has changed, apply filtering
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
        switch (view.getId()){
            // Close interface
//            case R.id.button_close:       // DEPRECATED no more close button
//                dialogForTaskRemoval();
//                break;
            // Task customization buttons
            default:
                mTaskCreationFragment.taskCustomization(view);
                break;

        }
    }

    private void dialogForTaskRemoval(){

        // Correcting bug, when switching to Landscape the onCreate does not initialize the fragment
        if(mTaskCreationFragment == null)
            mTaskCreationFragment = new TaskCreationFragment();

        // If the user haven't typed anything, close the interface
        if(!mTaskCreationFragment.isDataPresent()){
            removeAddTask();
            return;
        }

        // Else, ask for confirmation
        Dialog dialog = new AlertDialog.Builder(this)
                // For simple dialogs we don't use a Title (Google Guidelines)
                .setMessage(getString(R.string.discard_changes))

                // Only on discard the removeAddTask is triggered
                .setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        removeAddTask();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .show();

        // Dialog width customization (BUG > LOLLIPOP)  TODO light BUG with dialog size xD
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return;

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = 850;
        dialog.getWindow().setAttributes(lp);
    }

    public void deployAddTask() {

        // Deploy the layout
        setFragment(Constants.ADD_TASK);

        // Hide the fam
        mFamConfigurator.famVisibility(false);

//        // If the layout was created, show it and restart the fields
//        if (mAddTaskView != null) {
//            mAddTaskView.setVisibility(View.VISIBLE);
//            mTaskCreationFragment.resetFields();
//            return;
//        }
//
//        // Create a new Fragment to be placed in the activity layout
//        mTaskCreationFragment = new TaskCreationFragment();
//
//        // Add the fragment to the 'fragment_container' FrameLayout
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.main_fragment_container, mTaskCreationFragment).commit();
//
//        // Color the status bar in order to adapt the color to the shadow deployed
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.BLACK);
//        }
//
        // Block filter and navigation drawers
        mDrawersConfigurator.blockDrawers(true);
    }

    public void removeAddTask(){

        setFragment(Constants.DASHBOARD);

        // Show the FAM
        mFamConfigurator.famVisibility(true);



//        // Hide the container with the layout
//        mAddTaskView = findViewById(R.id.main_fragment_container);
//        mAddTaskView.setVisibility(View.GONE);

//        // Color the status bar in order to adapt the color to the shadow deployed
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
////            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            int color = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
//            window.setStatusBarColor(color);
//        }

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

    int currentFragmentKey;

    public void setFragment(int keyConstant) {
        if(keyConstant == currentFragmentKey)
            return;
        currentFragmentKey = keyConstant;

        Fragment newOne = null;

        switch (keyConstant) {
            case Constants.DASHBOARD:
                newOne = new DashboardFragment();

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

                break;
            case Constants.ADD_TASK:
                newOne = new TaskCreationFragment();

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