package com.software.achilles.tasked.listeners;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.software.achilles.tasked.MainActivity;
import com.software.achilles.tasked.Preferences;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.controllers.TaskController;
import com.software.achilles.tasked.domain.BasicType;
import com.software.achilles.tasked.domain.FavoriteLocation;
import com.software.achilles.tasked.domain.Label;
import com.software.achilles.tasked.domain.TaskList;
import com.software.achilles.tasked.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainAndFilterDrawerConfiguration {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private Activity mActivity;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private AccountHeader mAccountHeader;
    public Drawer mMainDrawer;
    public Drawer mFilterDrawer;
    private List<Integer> mTaskListIds;
    private List<Integer> mLabelListIds;
    private List<Integer> mLocationListIds;
    private PrimaryDrawerItem mTaskListCollapsableMain;
    private BadgeStyle mBadgeStyleExpand;
    private BadgeStyle mBadgeStyleCollapse;
    private boolean mExpandedTaskList = false;
    private boolean mExpandedTaskListFilter = false;
    private boolean mExpandedLabelListFilter = false;
    private boolean mExpandedLocationListFilter = false;
    private PrimaryDrawerItem mTaskListCollapsable;
    private PrimaryDrawerItem mLabelListCollapsable;
    private PrimaryDrawerItem mLocationListCollapsable;

    // ------------------------- Constructor -------------------------

    public MainAndFilterDrawerConfiguration(MainActivity activity, boolean filter) {
        this.mActivity = activity;

        // Set ActionBar
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);

        // Setup Navigation , behavior and first item to checked

        initializeBadges();

        setupProfileHeader();
        setupMainDrawer();
        setupMainDrawerListener();
        setupExpandableTaskList();

        if(!filter)
            return;

        // ------------------------ Dashboard Only -----------------------

        mViewPager = (ViewPager) mActivity.findViewById(R.id.viewpager);

        setupFilterDrawer();
        setupFilterDrawerListener();
//        setupExpandableTaskList();  NO SE QUE HACIA AHI


    }

    // ----------------------- Badges Creation -----------------------

    private void initializeBadges(){
        int color = ContextCompat.getColor(mActivity, R.color.secondaryText);

        Drawable expand = ContextCompat.getDrawable(mActivity, R.drawable.ic_expand_filled);
        expand.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        mBadgeStyleExpand = new BadgeStyle().withBadgeBackground(expand);

        Drawable collapse = ContextCompat.getDrawable(mActivity, R.drawable.ic_collapse_filled);
        collapse.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        mBadgeStyleCollapse = new BadgeStyle().withBadgeBackground(collapse);
    }

    // ---------------------- Navigation Drawer ----------------------

    private void setupProfileHeader() {
        // Create the AccountHeader
        mAccountHeader = new AccountHeaderBuilder()
                .withActivity(mActivity)
                .withHeaderBackground(R.drawable.default_image_navigation)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("John Doe")
                                .withEmail("jonnydoe@gmail.com")
                                .withIcon(R.drawable.person_image_empty),
                        new ProfileDrawerItem()
                                .withName("John Doe Work")
                                .withEmail("jonnydoework@gmail.com")
                                .withIcon(R.drawable.ic_place)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {

                        return true;
                    }
                })
                .build();
    }

    private void setupMainDrawer(){

        // Create main items
        int color = R.color.colorPrimary;
        PrimaryDrawerItem dashboard = new PrimaryDrawerItem().withIdentifier(Constants.DASHBOARD)
                .withIcon(R.drawable.ic_dashboard)
                .withName(R.string.dashboard)
                .withIconColorRes(color)
                .withIconTintingEnabled(true);
        color = R.color.amberDate;
        PrimaryDrawerItem snoozed = new PrimaryDrawerItem().withIdentifier(Constants.SNOOZED)
                .withIcon(R.drawable.ic_time_clean)
                .withName(R.string.snoozed)
                .withIconColorRes(color)
                .withSelectedIconColorRes(color)
                .withSelectedTextColorRes(color)
                .withIconTintingEnabled(true);
        color = R.color.colorSuccess;
        PrimaryDrawerItem completed = new PrimaryDrawerItem().withIdentifier(Constants.COMPLETED)
                .withIcon(R.drawable.ic_done)
                .withName(R.string.completed)
                .withIconColorRes(color)
                .withSelectedIconColorRes(color)
                .withSelectedTextColorRes(color)
                .withIconTintingEnabled(true);

        // Create secondary items
        PrimaryDrawerItem glance = new PrimaryDrawerItem().withIdentifier(Constants.GLANCE)
                .withName(R.string.glance)
                .withIcon(R.drawable.ic_calendar_list)
                .withIconTintingEnabled(true);
        PrimaryDrawerItem planner = new PrimaryDrawerItem().withIdentifier(Constants.PLANNER)
                .withName(R.string.planner)
                .withIcon(R.drawable.ic_view_carousel)
                .withIconTintingEnabled(true);

        // Create the footer items
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

        // Create expandable and collapsable item
        mTaskListCollapsableMain = new PrimaryDrawerItem()
                .withName(R.string.taskList)
                .withIcon(R.drawable.ic_list_bullet)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSABLE_TASK_LIST)
                .withSelectable(false)
                .withBadge("");

        // Create Filter Drawer
        mMainDrawer = new DrawerBuilder()
                .withActivity(mActivity)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(mAccountHeader)
                .withActionBarDrawerToggleAnimated(true)
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
//      TODO 1 de 2 - Descomentando esto tienes drawer solo en el click y puedes cerrarlo a mano :D
//                .withOnDrawerListener(new Drawer.OnDrawerListener() {
//                    @Override
//                    public void onDrawerOpened(View drawerView) {
//                        mFilterDrawer.getDrawerLayout().setDrawerLockMode(
//                                DrawerLayout.LOCK_MODE_UNLOCKED);
//                    }
//
//                    @Override
//                    public void onDrawerClosed(View drawerView) {
//                        mFilterDrawer.getDrawerLayout().setDrawerLockMode(
//                                DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
//                    }
//
//                    @Override
//                    public void onDrawerSlide(View drawerView, float slideOffset) {
//                    }
//                })
                .build();
    }

    private void setupMainDrawerListener(){

        // Setup the listener for the Main Drawer
        mMainDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
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
                        Intent intSettings = new Intent(mActivity.getApplicationContext(), Preferences.class);
                        mActivity.startActivity(intSettings);
                        break;

                    case Constants.CONTACT:
                        contactByEmail();
                        break;

                    case Constants.COLLAPSABLE_TASK_LIST:
                        // Populate or remove the Task lists
                        switchExpandableTaskListContent();
                        break;

                    case Constants.ADD_TASK_LIST:
                        break;

                    default:
                        // Calculate the position according to the Task List identifier.
                        int index = TaskController.getTaskListPositionById(identifier);

                        // Set the view pager on the correct list
                        if (index != -1)
                            mViewPager.setCurrentItem(index, true);
                        break;
                }
                // Do not close the drawer at Task List click
                if (identifier != Constants.COLLAPSABLE_TASK_LIST)
                    mMainDrawer.closeDrawer();
                return true;
            }
        });
    }

    private void contactByEmail(){
        Resources resources= mActivity.getResources();

        // Create a custom intent for Emails
        Intent intentEmail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                resources.getString(R.string.mailto),
                resources.getString(R.string.developer_email), null));

        //Populate the fields by default
        intentEmail.putExtra(Intent.EXTRA_SUBJECT,
                resources.getString(R.string.subject_email));

        // Create a dialog only for Email clients
        if (intentEmail.resolveActivity(mActivity.getPackageManager()) != null)
            mActivity.startActivity(Intent.createChooser(intentEmail,
                    resources.getString(R.string.send_email)));
    }

    private void setupExpandableTaskList(){
        // if Task List is expanded populate the Navigation Drawer
        if(mExpandedTaskList) {
            addTaskListToMainDrawer(TaskController.sTaskLists);
            toggleTaskListExpandable(false);
        }else
            toggleTaskListExpandable(true);
    }

    private void switchExpandableTaskListContent(){
        // Get current status for the Task List and in this case switch it.
        Boolean status = mExpandedTaskList;
        mExpandedTaskList = !mExpandedTaskList;

        // If it opened, remove all items, if closed, populate the drawer
        if(status) {
            toggleTaskListExpandable(true);
            for (int i = 0; i < mTaskListIds.size(); i++)
                mMainDrawer.removeItem(mTaskListIds.get(i));
        } else{
            toggleTaskListExpandable(false);
            addTaskListToMainDrawer(TaskController.sTaskLists);
        }
    }

    private void toggleTaskListExpandable(boolean expand){
        // Switch the badge between expand and collapse icons
        if(expand)
            mTaskListCollapsableMain.withBadgeStyle(mBadgeStyleExpand);
        else
            mTaskListCollapsableMain.withBadgeStyle(mBadgeStyleCollapse);

        mMainDrawer.updateItem(mTaskListCollapsableMain);
    }

    // ------------------------ Filter Drawer ------------------------

    private void setupFilterDrawer(){

        // Create both separators
        SectionDrawerItem mainSection = new SectionDrawerItem()
                .withName(R.string.main_filters);
        SectionDrawerItem listSection = new SectionDrawerItem()
                .withName(R.string.list_filters);

        // Create main items
        int color = R.color.colorAccent;
        PrimaryDrawerItem starred = new PrimaryDrawerItem()
                .withName(R.string.starred)
                .withIcon(R.drawable.ic_flag)
                .withIconTintingEnabled(true)
                .withIconColorRes(color)
                .withSelectedIconColorRes(color)
                .withSelectedTextColorRes(color)
                .withIdentifier(Constants.STARRED)
                .withSelectable(true)
                .withBadge("");
        color = R.color.amberDate;
        PrimaryDrawerItem today = new PrimaryDrawerItem()
                .withName(R.string.dueToday)
                .withIcon(R.drawable.ic_calendar_today)
                .withIconColorRes(color)
                .withSelectedIconColorRes(color)
                .withSelectedTextColorRes(color)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.DUE_TODAY)
                .withSelectable(true)
                .withBadge("");
        color = R.color.colorPrimary;
        PrimaryDrawerItem thisWeek = new PrimaryDrawerItem()
                .withName(R.string.dueThisWeek)
                .withIcon(R.drawable.ic_calendar_list)
                .withIconColorRes(color)
                .withSelectedIconColorRes(color)
                .withSelectedTextColorRes(color)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.DUE_THIS_WEEK)
                .withSelectable(true)
                .withBadge("");

        // Create expandable and collapsable items
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

        // Create Filter Drawer
        mFilterDrawer = new DrawerBuilder()
                .withActivity(mActivity)
                .withDisplayBelowStatusBar(true)
                .withCloseOnClick(false)
                .addDrawerItems(
                        mainSection,
                        starred, today, thisWeek,
                        listSection,
                        mLabelListCollapsable, mLocationListCollapsable, mTaskListCollapsable
                )
                .withDrawerWidthRes(R.dimen.filter_drawer_width)
                .withDrawerGravity(Gravity.END)
                .append(mMainDrawer);

//      TODO 2 de 2 - descomentando esto tienes drawer solo en el click y puedes cerrarlo a mano :D
//        mFilterDrawer.getDrawerLayout().setDrawerLockMode(
//                DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);

        // We want the labels opened by default
        addLabelsToFilterDrawer(TaskController.sLabels);
        mExpandedLabelListFilter = true;
    }

    private void setupFilterDrawerListener(){
        // Setup the listener for the Filter Drawer
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
                        if (mExpandedTaskListFilter) {
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
                        if (mExpandedLabelListFilter) {
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
                        if (mExpandedLocationListFilter) {
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
//                        int index = TaskController.getTaskListPositionById(identifier);
//                        if (index != -1)
//                            mViewPager.setCurrentItem(index, true);
                        break;
                }
                return true;
            }
        });
    }

    // --------------------- Add List To Drawer ----------------------

    private void addTaskListToFilterDrawer(ArrayList<TaskList> listTaskList){
        addItemListToDrawer(new ArrayList<BasicType>(listTaskList), mFilterDrawer,
                false, R.drawable.ic_done_all, Constants.COLLAPSABLE_TASK_LIST);
    }
    private void addTaskListToMainDrawer(ArrayList<TaskList> listTaskList){
        addItemListToDrawer(new ArrayList<BasicType>(listTaskList), mMainDrawer,
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

    private void addItemListToDrawer(ArrayList<BasicType> taskLists, final Drawer drawer,
                                     boolean main, int iconRes, int identifier){
        List<Integer>addedIds = new ArrayList<>();

        // Get the position for the item in the drawer, in order to add its children (+1)
        Integer position = drawer.getPosition(identifier) +1;

        // Add the Task Lists to the drawer by order at the right position
        for (int i = 0; i < taskLists.size(); i++) {

            // In case of the Label we customize the color, else, we use the default
            int color = R.color.secondaryText;
            switch (identifier){
                case Constants.COLLAPSABLE_LABEL_LIST:
                    color = ((Label) taskLists.get(i)).getColorRes();
                    break;
                case Constants.COLLAPSABLE_LOCATION_LIST:
                    color = R.color.tealLocation;
                    break;
                case Constants.COLLAPSABLE_TASK_LIST:
                    color = R.color.colorPrimary;
                    break;
            }

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
            mMainDrawer.addItem(new SecondaryDrawerItem().withIdentifier(Constants.ADD_TASK_LIST)
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

        // Scroll to the header of the list
//        drawer.getRecyclerView().scrollToPosition(6);
//        drawer.getRecyclerView().getLayoutManager().scrollToPosition(identifier);    //   ESTO DA EL APAÑO, pero no funciona para By task list
//        drawer.getRecyclerView().getChildAdapterPosition(mLabelListCollapsable.generateView(getApplication()));
//        drawer.getRecyclerView().
//        drawer.getRecyclerView().getLayoutManager().sc
    }
}