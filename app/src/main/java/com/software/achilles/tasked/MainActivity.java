package com.software.achilles.tasked;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.adapter.DrawerAdapter;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.software.achilles.tasked.adapters.Adapter;
import com.software.achilles.tasked.controllers.TaskController;
import com.software.achilles.tasked.domain.BasicType;
import com.software.achilles.tasked.domain.FavoriteLocation;
import com.software.achilles.tasked.domain.Label;
import com.software.achilles.tasked.domain.TaskList;
import com.software.achilles.tasked.fragments.DashboardListFragment;
import com.software.achilles.tasked.listeners.FloatingActionMenuConfigurator;
import com.software.achilles.tasked.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private FloatingActionMenuConfigurator mFamConfigurator;
    private TaskController mTaskController;
    private Toolbar mToolbar;
    public ViewPager mViewPager;
    private AccountHeader mAccountHeader;
    private Drawer mDrawer;
    private Drawer mFilterDrawer;
    private List<Integer> mTaskListIds;
    private List<Integer> mLabelListIds;
    private List<Integer> mLocationListIds;
    private PrimaryDrawerItem mTaskListCollapsableMain;
    private BadgeStyle mBadgeStyleExpand;
    private BadgeStyle mBadgeStyleCollapse;
    private Context mAppContext;
    private boolean mExpandedTaskListFilter = false;
    private boolean mExpandedLabelListFilter = false;
    private boolean mExpandedLocationListFilter = false;
    private PrimaryDrawerItem mTaskListCollapsable;
    private PrimaryDrawerItem mLabelListCollapsable;
    private PrimaryDrawerItem mLocationListCollapsable;

    // ------------------------- Constructor -------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize TaskController
        mTaskController = TaskController.getInstance();

        // Initialize context
        mAppContext = getApplicationContext();

        // Set ActionBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Setup Navigation , behavior and first item to checked
        initializeBadges();
        setupDrawer();
        setupFilterDrawer();
        // TODO a peticion de filter mejor? recuerda problemas de drawer
        // TODO si ejecuto aqui me cargo la sombra transparente para profile en la otra
        // SI LO PONGO ARRIBA SE CORRIJE, PERO QUEDA FEO TELA

        // Configure the fab menu and its children.
        mFamConfigurator = new FloatingActionMenuConfigurator(this);

        // Setup the fragment composing the ViewPager
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager, TaskController.sTaskLists);

        // Setup tabs for Dashboard and make Scrollable
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    // ---------------------- Navigation Drawer ----------------------

    private void initializeBadges(){
        int color = ContextCompat.getColor(this, R.color.secondaryText);

        Drawable expand = ContextCompat.getDrawable(this, R.drawable.ic_expand_filled);
        expand.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        mBadgeStyleExpand = new BadgeStyle().withBadgeBackground(expand);

        Drawable collapse = ContextCompat.getDrawable(this, R.drawable.ic_collapse_filled);
        collapse.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        mBadgeStyleCollapse = new BadgeStyle().withBadgeBackground(collapse);
    }

    private void setupHeader() {
        // Create the AccountHeader
        mAccountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.default_image_navigation)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("John Doe")
                                .withEmail("jonnydoe@gmail.com")
                                .withIcon(ContextCompat.getDrawable(mAppContext, R.drawable.person_image_empty)),
                        new ProfileDrawerItem()
                                .withName("John Doe Work")
                                .withEmail("jonnydoework@gmail.com")
                                .withIcon(ContextCompat.getDrawable(mAppContext, R.drawable.person_image_empty))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        //TODO onProfileChanged
                        return true;
                    }
                })
                .build();
    }

    private void setupDrawer(){

        setupHeader();

        // Setup the main components of the Navigation Drawer
        PrimaryDrawerItem dashboard = new PrimaryDrawerItem().withIdentifier(Constants.DASHBOARD)
                .withName(R.string.dashboard)
                .withIcon(R.drawable.ic_dashboard)
                .withIconColorRes(R.color.colorPrimary)
                .withIconTintingEnabled(true);
        PrimaryDrawerItem snoozed = new PrimaryDrawerItem().withIdentifier(Constants.SNOOZED)
                .withName(R.string.snoozed)
                .withIcon(R.drawable.ic_time_clean)
                .withIconColorRes(R.color.amberDate)
                .withSelectedIconColor(ContextCompat.getColor(mAppContext, R.color.amberDate))
                .withSelectedTextColorRes(R.color.amberDate)
                .withIconTintingEnabled(true);
        PrimaryDrawerItem completed = new PrimaryDrawerItem().withIdentifier(Constants.COMPLETED)
                .withName(R.string.completed)
                .withIcon(R.drawable.ic_done)
                .withIconColorRes(R.color.colorSuccess)
                .withSelectedIconColor(ContextCompat.getColor(mAppContext, R.color.colorSuccess))
                .withSelectedTextColorRes(R.color.colorSuccess)
                .withIconTintingEnabled(true);
        PrimaryDrawerItem glance = new PrimaryDrawerItem().withIdentifier(Constants.GLANCE)
                .withName(R.string.glance)
                .withIcon(R.drawable.ic_calendar_list)
                .withIconTintingEnabled(true);
        PrimaryDrawerItem planner = new PrimaryDrawerItem().withIdentifier(Constants.PLANNER)
                .withName(R.string.planner)
                .withIcon(R.drawable.ic_view_carousel)
                .withIconTintingEnabled(true);
        SecondaryDrawerItem settings = new SecondaryDrawerItem().withIdentifier(Constants.SETTINGS)
                .withName(R.string.settings)
                .withIcon(R.drawable.ic_settings)
                .withIconTintingEnabled(true)
                .withSelectable(false);
        SecondaryDrawerItem contact = new SecondaryDrawerItem().withIdentifier(Constants.CONTACT)
                .withName(R.string.contact)
                .withIcon(R.drawable.ic_email)
                .withIconTintingEnabled(true)
                .withSelectable(false);

        mTaskListCollapsableMain = new PrimaryDrawerItem()
                .withName(R.string.taskList)
                .withIcon(R.drawable.ic_list_bullet)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSABLE_TASK_LIST)
                .withSelectable(false)
                .withBadge("");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean(Constants.COLLAPSABLE_TASK_LIST_STATUS+"",
                prefs.getBoolean(Constants.COLLAPSABLE_TASK_LIST_STATUS+"", true)).apply();

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(mAccountHeader)
                .addDrawerItems(
                        dashboard, snoozed, completed,
                        new DividerDrawerItem(),
                        glance, planner,
                        new DividerDrawerItem(),
                        mTaskListCollapsableMain
                )
                .addStickyDrawerItems(
                        settings, contact
                )
                .withActionBarDrawerToggleAnimated(true)
                .build();

        setupDrawerListener();
    }

    private void setupDrawerListener(){
        mDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                int identifier = drawerItem.getIdentifier();
                switch (identifier) {

                    case Constants.DASHBOARD:
                        break;
                    case Constants.SNOOZED:
                        break;
                    case Constants.COMPLETED:
                        break;
                    case Constants.GLANCE:
                        break;
                    case Constants.PLANNER:
                        break;

                    case Constants.SETTINGS:
                        // Launch the Preferences Fragment
                        Intent intentSettings = new Intent(getApplicationContext(), Preferences.class);
                        startActivity(intentSettings);
                        break;

                    case Constants.CONTACT:
                        // Create a custom intent for Emails
                        Intent intentEmail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                getResources().getString(R.string.mailto),
                                getResources().getString(R.string.developer_email), null));

                        //Populate the fields by default
                        intentEmail.putExtra(Intent.EXTRA_SUBJECT,
                                getResources().getString(R.string.subject_email));

                        // Create a dialog only for Email clients
                        if (intentEmail.resolveActivity(getPackageManager()) != null)
                            startActivity(Intent.createChooser(intentEmail,
                                    getResources().getString(R.string.send_email)));
                        break;

                    case Constants.COLLAPSABLE_TASK_LIST:
                        switchExpandableTaskListContentAndPreference();
                        break;

                    case Constants.ADD_TASK_LIST:
                        break;

                    default:
                        int index = TaskController.getPositionById(identifier);
                        if (index != -1)
                            mViewPager.setCurrentItem(index, true);
                        break;
                }

                if (identifier != Constants.COLLAPSABLE_TASK_LIST)
                    mDrawer.closeDrawer();
                return true;
            }
        });

        setupExpandableTaskList();
    }

    private void setupExpandableTaskList(){
        // if Task List is expanded populate the Navigation Drawer
        if(getAndOrSwitch(false)) {
            addTaskListToMainDrawer(TaskController.sTaskLists);
            adaptTaskListExpandable(false);
        }else
            adaptTaskListExpandable(true);
    }

    private boolean getAndOrSwitch(boolean switchValue){
        // Get the preference that indicates if the user wants the Task List collapsed or not
        Context context = getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean status = preferences.getBoolean(Constants.COLLAPSABLE_TASK_LIST_STATUS + "", true);

        // If switch (click on Task List case), switch the values to reflect the change
        if(switchValue)
            preferences.edit().putBoolean(Constants.COLLAPSABLE_TASK_LIST_STATUS + "", !status).apply();

        return status;
    }

    private void switchExpandableTaskListContentAndPreference(){
        // Get current status for the Task List and in this case switch it in the preferences
        Boolean status = getAndOrSwitch(true);

        // If it opened, remove all items, if closed, populate the drawer
        if(status) {
            adaptTaskListExpandable(true);
            for (int i = 0; i < mTaskListIds.size(); i++)
                mDrawer.removeItem(mTaskListIds.get(i));
        } else{
            adaptTaskListExpandable(false);
            addTaskListToMainDrawer(TaskController.sTaskLists);
        }
    }

    private void adaptTaskListExpandable(boolean expand){
        // Switch the badge between expand and collapse icons
        if(expand)
            mTaskListCollapsableMain.withBadgeStyle(mBadgeStyleExpand);
        else
            mTaskListCollapsableMain.withBadgeStyle(mBadgeStyleCollapse);

        mDrawer.updateItem(mTaskListCollapsableMain);
    }

    private void setupFilterDrawer(){

        SectionDrawerItem mainSection = new SectionDrawerItem()
                .withName(R.string.main_filters);
        SectionDrawerItem listSection = new SectionDrawerItem()
                .withName(R.string.list_filters);

        PrimaryDrawerItem starred = new PrimaryDrawerItem()
                .withName(R.string.starred)
                .withIcon(R.drawable.ic_flag)
                .withIconTintingEnabled(true)
                .withIconColorRes(R.color.colorAccent)
                .withSelectedIconColor(ContextCompat.getColor(mAppContext, R.color.colorAccent))
                .withSelectedTextColorRes(R.color.colorAccent)
                .withIdentifier(Constants.STARRED)
                .withSelectable(true)
                .withBadge("");

        PrimaryDrawerItem today = new PrimaryDrawerItem()
                .withName(R.string.dueToday)
                .withIcon(R.drawable.ic_calendar_today)
                .withIconColorRes(R.color.amberDate)
                .withSelectedIconColor(ContextCompat.getColor(mAppContext, R.color.amberDate))
                .withSelectedTextColorRes(R.color.amberDate)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.DUE_TODAY)
                .withSelectable(true)
                .withBadge("");

        PrimaryDrawerItem thisWeek = new PrimaryDrawerItem()
                .withName(R.string.dueThisWeek)
                .withIcon(R.drawable.ic_calendar_list)
                .withIconColorRes(R.color.colorPrimary)
                .withSelectedIconColor(ContextCompat.getColor(mAppContext, R.color.colorPrimary))
                .withSelectedTextColorRes(R.color.colorPrimary)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.DUE_THIS_WEEK)
                .withSelectable(true)
                .withBadge("");

        mTaskListCollapsable = new PrimaryDrawerItem()
                .withName(R.string.by_task_list)
                .withIcon(R.drawable.ic_list_bullet)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSABLE_TASK_LIST)
                .withSelectable(false)
                .withBadge("")
                .withBadgeStyle(mBadgeStyleExpand);

        mLabelListCollapsable = new PrimaryDrawerItem()
                .withName(R.string.by_label)
                .withIcon(R.drawable.ic_label_outline)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSABLE_LABEL_LIST)
                .withSelectable(false)
                .withBadge("")
                .withBadgeStyle(mBadgeStyleCollapse);

        mLocationListCollapsable = new PrimaryDrawerItem()
                .withName(R.string.by_location)
                .withIcon(R.drawable.ic_map)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSABLE_LOCATION_LIST)
                .withSelectable(false)
                .withBadge("")
                .withBadgeStyle(mBadgeStyleExpand);

        mFilterDrawer = new DrawerBuilder()
                .withActivity(this)
                .withDisplayBelowStatusBar(true)
                .withCloseOnClick(false)
                .addDrawerItems(
                        mainSection,
                        starred, today, thisWeek,
                        listSection,
                        mLabelListCollapsable, mLocationListCollapsable, mTaskListCollapsable
                )
                .withDrawerGravity(Gravity.END)
                .append(mDrawer);

        // TODO esto bloquea el gesto para el derecho... Por si te da por ahí
//        mFilterDrawer.getDrawerLayout().setDrawerLockMode(
//                DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);


        // TODO tal vez reducir un poco el ancho del Drawer :S
//        mFilterDrawer.getContent().getLayoutParams().width = DrawerLayout.LayoutParams.WRAP_CONTENT;
//        mFilterDrawer.getRecyclerView().getLayoutParams().width = 20;
//        mFilterDrawer.getRecyclerView().getLayoutParams().width = RecyclerView.LayoutParams.WRAP_CONTENT;

        // Setup the listeners for the drawer
        setupFilterDrawerListener();

        // We want the labels opened by default
        addLabelsToFilterDrawer(TaskController.sLabels);
        mExpandedLabelListFilter = true;
    }

    private void setupFilterDrawerListener(){

        mFilterDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                int identifier = drawerItem.getIdentifier();
                switch (identifier) {

                    case Constants.STARRED:
                        break;
                    case Constants.DUE_TODAY:
                        break;
                    case Constants.DUE_THIS_WEEK:
                        break;

                    case Constants.COLLAPSABLE_TASK_LIST:
                        if(mExpandedTaskListFilter) {
                            for (int i = 0; i < mTaskListIds.size(); i++)
                                mFilterDrawer.removeItem(mTaskListIds.get(i));
                            mExpandedTaskListFilter = false;
                            mTaskListCollapsable.withBadgeStyle(mBadgeStyleExpand);
                            mFilterDrawer.updateItem(mTaskListCollapsable);
                        } else {
                            addTaskListToFilterDrawer(TaskController.sTaskLists);
                            mExpandedTaskListFilter = true;
                            mTaskListCollapsable.withBadgeStyle(mBadgeStyleCollapse);
                            mFilterDrawer.updateItem(mTaskListCollapsable);
                        }
                        break;

                    case Constants.COLLAPSABLE_LABEL_LIST:
                        if(mExpandedLabelListFilter) {
                            for (int i = 0; i < mLabelListIds.size(); i++)
                                mFilterDrawer.removeItem(mLabelListIds.get(i));
                            mExpandedLabelListFilter = false;
                            mLabelListCollapsable.withBadgeStyle(mBadgeStyleExpand);
                            mFilterDrawer.updateItem(mLabelListCollapsable);
                        } else {
                            addLabelsToFilterDrawer(TaskController.sLabels);
                            mExpandedLabelListFilter = true;
                            mLabelListCollapsable.withBadgeStyle(mBadgeStyleCollapse);
                            mFilterDrawer.updateItem(mLabelListCollapsable);
                        }
                        break;

                    case Constants.COLLAPSABLE_LOCATION_LIST:
                        if(mExpandedLocationListFilter) {
                            for (int i = 0; i < mLocationListIds.size(); i++)
                                mFilterDrawer.removeItem(mLocationListIds.get(i));
                            mExpandedLocationListFilter = false;
                            mLocationListCollapsable.withBadgeStyle(mBadgeStyleExpand);
                            mFilterDrawer.updateItem(mLocationListCollapsable);
                        } else {
                            addLocationsFilterToDrawer(TaskController.sFavouriteLocations);
                            mExpandedLocationListFilter = true;
                            mLocationListCollapsable.withBadgeStyle(mBadgeStyleCollapse);
                            mFilterDrawer.updateItem(mLocationListCollapsable);
                        }
                        break;

                    default:
                        int index = TaskController.getPositionById(identifier);
                        if (index != -1)
                            mViewPager.setCurrentItem(index, true);
                        break;
                }

                return true;
            }
        });

        setupExpandableTaskList();
    }

    private void addTaskListToFilterDrawer(ArrayList<TaskList> listTaskList){
        addItemListToDrawer(new ArrayList<BasicType>(listTaskList), mFilterDrawer,
                false, R.drawable.ic_done_all, Constants.COLLAPSABLE_TASK_LIST);
    }
    private void addTaskListToMainDrawer(ArrayList<TaskList> listTaskList){
        addItemListToDrawer(new ArrayList<BasicType>(listTaskList), mDrawer,
                true, R.drawable.ic_done_all, Constants.COLLAPSABLE_TASK_LIST);
    }
    private void addLabelsToFilterDrawer(ArrayList<Label> listLabels){
        addItemListToDrawer(new ArrayList<BasicType>(listLabels), mFilterDrawer,
                false, R.drawable.ic_label_filled, Constants.COLLAPSABLE_LABEL_LIST);
    }
    private void addLocationsFilterToDrawer(ArrayList<FavoriteLocation> favLocations){
        addItemListToDrawer(new ArrayList<BasicType>(favLocations), mFilterDrawer,
                false, R.drawable.ic_place, Constants.COLLAPSABLE_LOCATION_LIST);
    }

    private void addItemListToDrawer(ArrayList<BasicType> taskLists, Drawer drawer,
                                     boolean main, int iconRes, int identifier){
        List<Integer>addedIds = new ArrayList<>();

        // Get the position for the item in the drawer, in order to add its children (+1)
        Integer position = drawer.getPosition(identifier) +1;

        // Add the Task Lists to the drawer by order at the right position
        for (int i = 0; i < taskLists.size(); i++) {

            // In case of the Label we customize the color, else, we use the default
            int color = R.color.secondaryText;
            if(identifier == Constants.COLLAPSABLE_LABEL_LIST)
                color = ((Label) taskLists.get(i)).getColorRes();

            // Construct the Item to add on the Drawer
            IDrawerItem itemToAdd = new SecondaryDrawerItem().withIdentifier(taskLists.get(i).getId())
                    .withLevel(2)
                    .withName(taskLists.get(i).getTitle())
                    .withIcon(iconRes)
                    .withIconColorRes(color)
                    .withIconTintingEnabled(true)
                    .withSelectable(false);
            drawer.addItemAtPosition(itemToAdd, position);

            // Move the pointer for an ordered insertion and save the id for posterior deletion
            position++;
            addedIds.add(taskLists.get(i).getId());
        }

        // If on the main drawer, add a new "add Task List" item for convenience and its ID
        if(main) {
            mDrawer.addItem(new SecondaryDrawerItem().withIdentifier(Constants.ADD_TASK_LIST)
                    .withLevel(2)
                    .withName(R.string.addList)
                    .withIcon(R.drawable.ic_add)
                    .withIconTintingEnabled(true)
                    .withSelectable(false));
            addedIds.add(Constants.ADD_TASK_LIST);
        }

        // Save the id to control the Navigation Drawer more properly
        switch (identifier){
            case Constants.COLLAPSABLE_TASK_LIST:
                mTaskListIds = addedIds;
                break;
            case Constants.COLLAPSABLE_LABEL_LIST:
                mLabelListIds = addedIds;
                break;
            case Constants.COLLAPSABLE_LOCATION_LIST:
                mLocationListIds = addedIds;
                break;
        }
    }

    // -------------------------- View Pager -------------------------

    private void setupViewPager(ViewPager viewPager, ArrayList<TaskList> taskLists) {
        Adapter adapter = new Adapter(getSupportFragmentManager());

//        adapter.addFragment(new DashboardSearchFragment(), "Search");
        // TODO quick jump to the desired list


        // Populate each of the pages of the ViewPager
        for (TaskList taskList : taskLists) {
            // Pick the fragment the page is going to show
            DashboardListFragment dashboardListFragment = new DashboardListFragment();

            // Introduce the TaskList corresponding to that fragment
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.TASK_LIST + "", taskList);
            dashboardListFragment.setArguments(bundle);

            // Add the fragment and it's bundle to the adapter
            adapter.addFragment(dashboardListFragment, taskList.getTitle());
        }
        viewPager.setAdapter(adapter);
    }

    // -------------------------- Landscape --------------------------

    // ------------------------ Actionbar Menu -----------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_filter:
                if(mFilterDrawer == null)
                    setupFilterDrawer();

                mFilterDrawer.openDrawer();
                break;

            case R.id.action_search:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    // -------------------------- Use Cases --------------------------

    // --------------------- Add Task Interface ----------------------

    // --------------------------- Details ---------------------------

    @Override
    public void onBackPressed() {
        FloatingActionMenu fam = (FloatingActionMenu) findViewById(R.id.menuFAB);

        if(fam.isOpened() || mFilterDrawer != null && mDrawer.isDrawerOpen()) {
            fam.close(true);
            mDrawer.closeDrawer();
        }else if(mFilterDrawer != null && mFilterDrawer.isDrawerOpen())
            mFilterDrawer.closeDrawer();
        else
            super.onBackPressed();
    }

    // ------------------------ Notifications ------------------------

    // ------------------------- Preferences -------------------------










    // ------------------------- Deprecated --------------------------

//    private void addTaskListToDrawer(ArrayList<TaskList> taskLists){
//        List<Integer>addedIds = new ArrayList<>();
//
//        // Add the Task Lists to the drawer by order and at the end
//        for(TaskList taskList : taskLists) {
//            mDrawer.addItem(
//                    new SecondaryDrawerItem().withIdentifier(taskList.getId())
//                            .withLevel(2)
//                            .withName(taskList.getTitle())
//                            .withIcon(R.drawable.ic_done_all)
//                            .withIconTintingEnabled(true)
//                            .withSelectable(false));
//            addedIds.add(taskList.getId());
//        }
//
//        // Add a new add Task List item for convenience, also add its ID to control it.
//        mDrawer.addItem(new SecondaryDrawerItem().withIdentifier(Constants.ADD_TASK_LIST)
//                .withLevel(2)
//                .withName(R.string.addList)
//                .withIcon(R.drawable.ic_add)
//                .withIconTintingEnabled(true)
//                .withSelectable(false));
//        addedIds.add(Constants.ADD_TASK_LIST);
//
//        // Save the id to control the Navigation Drawer more properly
//        mTaskListIds = addedIds;
//    }
}