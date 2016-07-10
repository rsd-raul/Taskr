package com.software.achilles.tasked.view;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.github.clans.fab.FloatingActionMenu;
import com.software.achilles.tasked.App;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.helpers.PreferencesHelper;
import com.software.achilles.tasked.model.helpers.PreferencesHelper.*;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.presenter.DashboardPresenter;
import com.software.achilles.tasked.presenter.MainPresenter;
import com.software.achilles.tasked.presenter.TaskCreationPresenter;
import com.software.achilles.tasked.util.extras.ErrorReporter;
import com.software.achilles.tasked.view.fragments.DashboardFragment;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;
import com.software.achilles.tasked.view.configurators.FloatingActionMenuConfigurator;
import com.software.achilles.tasked.view.configurators.MainAndFilterDrawerConfigurator;
import com.software.achilles.tasked.util.Constants;
import javax.inject.Inject;
import dagger.Lazy;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    // -------------------------- Injected ---------------------------

    @Inject
    FloatingActionMenuConfigurator mFamConfigurator;
    @Inject
    public MainAndFilterDrawerConfigurator mDrawersConfigurator;
    @Inject
    DataManager dataManager;
    @Inject
    MainPresenter mainPresenter;
    @Inject
    Lazy<DashboardFragment> dashboardFragmentLazy;
    @Inject
    Lazy<TaskCreationFragment> taskCreationFragmentLazy;

    // ------------------------- Constructor -------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Dagger FTW - Inject dependencies
        ((App) getApplication()).component().inject(this);

//REVIEW        ****** ONLY FOR DEVELOPMENT ******
        // Simple BUG report, retrieves the last error and tries to send an email
//        ErrorReporter errorReporter = ErrorReporter.getInstance();
//        errorReporter.Init(this);
//        errorReporter.CheckErrorAndSendMail(this);
//REVIEW        ****** ONLY FOR DEVELOPMENT ******

        // Set the RealmConfiguration for Realm usage
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this).build());

        // If first time, launch the introduction
        if (PreferencesHelper.getShaPrefBoolean(this, Keys.FIRST_TIME, Defaults.FIRST_TIME, true)) {
            // Set the value to false
            PreferencesHelper.setShaPrefBoolean(this, Keys.FIRST_TIME, false, true);
            // launchIntro();
            // return;
            Toast.makeText(this, "- populating -", Toast.LENGTH_LONG).show();

            // Data population for testing and/or introduction
            dataManager.firstTimePopulation();
        }

        // Set ActionBar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Configure the drawers, both main and filter
        mDrawersConfigurator.configure(this);

        // Initialize the main Presenter
        mainPresenter.attachView(this);

        // Initialize with Dashboard
        setFragment(Constants.DASHBOARD);
    }

    // -------------------------- Landscape --------------------------

    // ------------------------ Actionbar Menu -----------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Set the menu depending on the fragment
        switch (currentFragmentKey){

            case Constants.DASHBOARD:
                getMenuInflater().inflate(R.menu.menu_dashboard, menu);

                // Set the filtering by text
                MenuItem searchItem = menu.findItem(R.id.action_search);
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String query) {
                        dashboardPresenter.filterByText(query, false);
                        Log.i("onQueryTextChange1: ", query);
                        return true;
                    }
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        dashboardPresenter.filterByText(query, true);
                        Log.i("onQueryTextChange2: ", query);
                        return true;
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
        // Remove the behaviour
        bestBehaviour_drake(true);

        // Deploy the layout
        setFragment(Constants.ADD_TASK);

        // Hide the fam
        mFamConfigurator.famVisibility(false);

        // Block filter and navigation drawers
        mDrawersConfigurator.blockDrawers(true);
    }

    // TODO esto deberia hacer que la ActionBar volviera - NO LO HACE

    /**
     * This method will make sure the behavior that hides the action bar is disabled
     * or enabled at convenience.
     *
     * @param remove    Boolean that controls if the behaviour is implemented or not
     */
    private void bestBehaviour_drake(boolean remove){
        FrameLayout fl = ((FrameLayout) findViewById(R.id.main_fragment_container));

        if (fl == null)
            return;

        CoordinatorLayout.LayoutParams aux = (CoordinatorLayout.LayoutParams) fl.getLayoutParams();
        aux.setBehavior(remove ? null : new AppBarLayout.ScrollingViewBehavior());

        fl.requestLayout();
    }

    public void removeAddTask(){
        // TODO volver a Dashboard o a Glance
        setFragment(Constants.DASHBOARD);

        // Show the FAM
        mFamConfigurator.famVisibility(true);

        // Unblock filter and navigation drawers
        mDrawersConfigurator.blockDrawers(false);

        // Restore @string/appbar_scrolling_view_behavior
        bestBehaviour_drake(false);
    }

    // --------------------------- Details ---------------------------

    @Override
    public void onBackPressed() {
        switch (currentFragmentKey){
            case Constants.ADD_TASK:
                mainPresenter.backToBack();
                return;
            case Constants.SNOOZED:
                break;
            case Constants.COMPLETED:
                break;
        }

        FloatingActionMenu fam = (FloatingActionMenu) findViewById(R.id.menuFAB);

        if((fam != null && fam.isOpened()) || mDrawersConfigurator.mMainDrawer.isDrawerOpen()) {
            if(fam != null)
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
    @Inject
    DashboardPresenter dashboardPresenter;
    @Inject
    TaskCreationPresenter taskCreationPresenter;

    public void setFragment(int keyConstant) {
        if(keyConstant == currentFragmentKey)
            return;
        currentFragmentKey = keyConstant;

        Fragment newOne = null;

        switch (keyConstant) {
            case Constants.DASHBOARD:
                newOne = dashboardFragmentLazy.get();

                // Configure the fab menu and its children.
                mFamConfigurator.configure(this);

                // Unblock filter and navigation drawers, show FAM
                if(mFamConfigurator != null) {
                    mFamConfigurator.famVisibility(true);
                    mDrawersConfigurator.blockDrawers(false);
                    break;
                }

                dashboardPresenter.attachView((DashboardFragment) newOne);
//                taskCreationPresenter.destroyPresenter();

                break;
            case Constants.ADD_TASK:
                newOne = taskCreationFragmentLazy.get();

                // Send the list the user is on
                Bundle bundle = new Bundle();
                bundle.putInt("listIndex", DashboardFragment.mViewPager.getCurrentItem());
                newOne.setArguments(bundle);

                taskCreationPresenter.attachView((TaskCreationFragment) newOne);
//                DashboardPresenter.destroyPresenter();

                break;
        }

        mDrawersConfigurator.customizeActionBar(keyConstant);

        if(newOne == null)
            return;

        // Initialize the fragment change
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, newOne).commit();
    }

    public void setupViewPagerAndTabs(boolean goToEnd){
        dashboardPresenter.setupViewPagerAndTabs(goToEnd);
    }
}