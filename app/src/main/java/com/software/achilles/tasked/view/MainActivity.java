package com.software.achilles.tasked.view;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.software.achilles.tasked.App;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.presenter.DashboardPresenter;
import com.software.achilles.tasked.presenter.MainPresenter;
import com.software.achilles.tasked.presenter.TaskCreationPresenter;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.util.extras.ErrorReporter;
import com.software.achilles.tasked.util.helpers.PreferencesHelper;
import com.software.achilles.tasked.util.helpers.PreferencesHelper.Keys;
import com.software.achilles.tasked.util.helpers.PreferencesHelper.Value;
import com.software.achilles.tasked.view.configurators.FloatingActionMenuConfigurator;
import com.software.achilles.tasked.view.configurators.MainAndFilterDrawerConfigurator;
import com.software.achilles.tasked.view.fragments.DashboardFragment;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.Lazy;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    // -------------------------- Injected ---------------------------

    @Inject
    public FloatingActionMenuConfigurator mFamConfigurator;
    @Inject
    public MainAndFilterDrawerConfigurator mDrawersConfigurator;
    @Inject
    DataManager dataManager;
    @Inject
    MainPresenter mainPresenter;
    @Inject
    Lazy<DashboardFragment> dashboardFragmentLazy;
    @Inject
    Provider<TaskCreationFragment> taskCreationFragmentProvider;

    // ------------------------- Constructor -------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Dagger FTW - Inject dependencies
        ((App) getApplication()).component().inject(this);

//REVIEW        ****** ONLY FOR DEVELOPMENT ******
        // Simple BUG report, retrieves the last error and tries to send an email
        ErrorReporter errorReporter = ErrorReporter.getInstance();
        errorReporter.Init(this);
        errorReporter.CheckErrorAndSendMail(this);
        Toast.makeText(MainActivity.this, "Email Reporter Active", Toast.LENGTH_SHORT).show();
//REVIEW        ****** ONLY FOR DEVELOPMENT ******

        // If first time, launch the introduction
        if (PreferencesHelper.getShaPrefBoolean(this, Keys.FIRST_TIME, Value.FIRST_TIME, true)) {
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
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        appData.putBoolean(Constants.LIST_INDEX, true);
        startSearch(null, false, appData, false);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Set the menu depending on the fragment
        switch (mCurrentFragmentKey){

            case Constants.DASHBOARD:
                getMenuInflater().inflate(R.menu.menu_dashboard, menu);
                configureSearch(menu);
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
        int identifier = item.getItemId();
        switch (identifier) {
            case R.id.action_filter:
                mDrawersConfigurator.mFilterDrawer.openDrawer();
                break;

            case R.id.action_complete:
            case R.id.action_delete:
                long id = dataManager.getTemporalTask().getId();

                if(id < 1) {
                    Toast.makeText(MainActivity.this, R.string.task_not_saved, Toast.LENGTH_SHORT).show();
                    break;
                }

                if(identifier == R.id.action_complete)
                    dashboardPresenter.taskModifier(Constants.DASH_DONE, dataManager.findTaskById(id));
                else
                    dashboardPresenter.deleteTemporalTaskFromRealm(id);

                removeAddTask();
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
        setFragment(Constants.DASHBOARD);

        // Show the FAM
        mFamConfigurator.famVisibility(true);

        // Unblock filter and navigation drawers
        mDrawersConfigurator.blockDrawers(false);

        // Restore @string/appbar_scrolling_view_behavior
        bestBehaviour_drake(false);

        // Hide Keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // --------------------------- Details ---------------------------

    @Override
    public void onBackPressed() {
        switch (mCurrentFragmentKey){
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

    private int mCurrentFragmentKey;
    @Inject
    DashboardPresenter dashboardPresenter;
    @Inject
    TaskCreationPresenter taskCreationPresenter;

    public void setFragment(int keyConstant){
        setFragment(keyConstant, -1);
    }

    public void setEditItem(long itemId){
        setFragment(Constants.ADD_TASK, itemId);
    }

    private void setFragment(int keyConstant, long itemId) {
        if(keyConstant == mCurrentFragmentKey)
            return;

        int animIn = R.anim.slide_in_left, animOut = R.anim.slide_out_right;

        Fragment newFragment = null;

        switch (keyConstant) {
            case Constants.DASHBOARD:
                newFragment = dashboardFragmentLazy.get();

                // Configure the fab menu and its children.
                mFamConfigurator.configure(this);

                // Unblock filter and navigation drawers, show FAM
                if(mFamConfigurator != null) {
                    mFamConfigurator.famVisibility(true);
                    mDrawersConfigurator.blockDrawers(false);
                    break;
                }
                dashboardPresenter.attachView((DashboardFragment) newFragment);
//                taskCreationPresenter.destroyPresenter();
                break;

            case Constants.ADD_TASK:
                newFragment = taskCreationFragmentProvider.get();

                // Send the list the user is on
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.LIST_INDEX, DashboardFragment.mViewPager.getCurrentItem());
                bundle.putLong(Constants.TASK_ID, itemId);
                newFragment.setArguments(bundle);

                taskCreationPresenter.attachView((TaskCreationFragment) newFragment);
//                DashboardPresenter.destroyPresenter();
                //FIXME
                if(mCurrentFragmentKey == Constants.DASHBOARD) {
                    animIn = android.R.anim.fade_in;
                    animOut = android.R.anim.fade_out;
                }
                break;
        }

        mDrawersConfigurator.customizeActionBar(keyConstant);
        mCurrentFragmentKey = keyConstant;

        if(newFragment == null)
            return;

        // Initialize the fragment change
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.main_fragment_container, newFragment).commit();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(animIn, animOut);
        ft.replace(R.id.main_fragment_container, newFragment).commit();
    }

    public void setupViewPagerAndTabs(boolean goToEnd){
        dashboardPresenter.setupViewPagerAndTabs(goToEnd);
    }

    // ------------------- Fragment and Presenter --------------------
    // ------------------------ PLACE PICKER -------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK)
            taskCreationPresenter.processPlacePicker(data);
    }

    // --------------------------- SEARCH ----------------------------

    private void configureSearch(Menu menu){
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Searching by: "+ query, Toast.LENGTH_SHORT).show();

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String uri = intent.getDataString().replace("/","");

            long id;
            try { id = Long.parseLong(uri); }
            catch (NumberFormatException ex){ id = -1; }

            if(id != -1)
                dashboardPresenter.itemOnClick(id);
            else
                Toast.makeText(MainActivity.this, R.string.warning_item_access, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Close the Realm instance.
        Realm.getDefaultInstance().close();
    }
}