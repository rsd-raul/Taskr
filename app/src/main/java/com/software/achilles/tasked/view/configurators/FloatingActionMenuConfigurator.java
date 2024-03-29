package com.software.achilles.tasked.view.configurators;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.presenter.MainPresenter;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.util.extras.FloatingActionMenuBehavior;
import com.software.achilles.tasked.util.helpers.LocalisationHelper;
import com.software.achilles.tasked.view.MainActivity;
import com.software.achilles.tasked.view.fragments.DashboardFragment;

import javax.inject.Inject;

public class FloatingActionMenuConfigurator {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private MainActivity mainActivity;
    private FloatingActionMenu fam;

    // -------------------------- Injected ---------------------------

    MainPresenter mainPresenter;
    DataManager dataManager;

    // ------------------------- Constructor -------------------------

    @Inject
    public FloatingActionMenuConfigurator(MainPresenter mainPresenter, DataManager dataManager) {
        this.mainPresenter = mainPresenter;
        this.dataManager = dataManager;
    }

    // -------------------------- Use Cases --------------------------

    public void configure(MainActivity activity){
        mainActivity = activity;
        fam = (FloatingActionMenu) activity.findViewById(R.id.menuFAB);

        configureMenu();
//        setMenuOnScrollReaction();
        menuCustomizeBehaviour();
        configureChildren();
//        setMenuOnScrollReaction();
    }

    // -------------------------- Use Cases --------------------------

    // -------------------------- FAB menu ---------------------------

    private void configureMenu(){

        // Change the background depending on the fabMenu Status
        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            int fromColor = ContextCompat.getColor(mainActivity, R.color.transparent);
            int toColor = ContextCompat.getColor(mainActivity, R.color.background);

            // Creation of animator to transition between the transparent color and the other one
            final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(fam,
                    "backgroundColor", new ArgbEvaluator(), fromColor, toColor).setDuration(100);

            @Override
            public void onMenuToggle(boolean opened) {
                if (opened)
                    backgroundColorAnimator.start();
                else
                    backgroundColorAnimator.reverse();
            }
        });

        // On click outside close the menu
        fam.setClosedOnTouchOutside(true);

//        // FIXME onLongClick => Desplegar añadir tarea
//        fam.setOnLongClickListener(new FloatingActionMenu.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                mainPresenter.deployLayout(Constants.ADD_TASK);
//                return true;
//            }
//        });
    }

//    public void closeFabMenu(){
//        fam.close(true);
//    }

    // FIXME 1/2 Controlar el FAM y la barra cuando se detecta scroll
    // FIXME 2/2 Si no escondes FAM tienes que dar extra padding/margin a el viewpager (o el boton tapa las acciones)
//    public static void setMenuOnScrollReaction(){

//        // prepare animations
//        Animation fab_slide_down = AnimationUtils.loadAnimation(mainActivity, R.anim.fab_slide_down);
//        fab_slide_down.setInterpolator(new AccelerateInterpolator());
//
//        Animation fab_slide_up = AnimationUtils.loadAnimation(mainActivity, R.anim.fab_slide_up);
//        fab_slide_up.setInterpolator(new AccelerateInterpolator());
//
//        // Set animations
//        fam.setMenuButtonHideAnimation(fab_slide_down);
//        fam.setMenuButtonShowAnimation(fab_slide_up);

        // Control the behaviour when scrolling

//        mainActivity.getSupportActionBar().hide();
//        mainActivity.getSupportActionBar().show();

//        RecyclerView recyclerView = (RecyclerView) mainActivity.findViewById(R.id.recyclerview);
//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (dy > 0 && fam.getVisibility() == View.VISIBLE)
//                    // User scrolled down and the FAB is currently visible -> hide the FAB
//                    fam.hideMenu(true);
//                 else if (dy < 0 && fam.getVisibility() != View.VISIBLE)
//                    // User scrolled up and the FAB is currently not visible -> show the FAB
//                    fam.showMenu(true);
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });

//        NestedScrollView scrollView = (NestedScrollView) mainActivity.findViewById(R.id.scrollView);
//        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

        // ---------------------- Marshmallow Only -----------------------

        //FIXME - NOT WORKING on MARSHMALLOW -> BUG

//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
//            return;
//
//        ViewPager viewPager = (ViewPager) mainActivity.findViewById(R.id.viewpager);
//        viewPager.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY,
//                                       int oldScrollX, int oldScrollY) {
//                if (scrollY - oldScrollY > 0)
//                    fam.hideMenu(true);
//                else
//                    fam.showMenu(true);
//            }
//        });



    // Make the menu react to external stimuli such as a Snackbar
    private void menuCustomizeBehaviour(){
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fam.getLayoutParams();
        params.setBehavior(new FloatingActionMenuBehavior());
        fam.requestLayout();
    }

    // -------------------------- FAB child --------------------------

    private void configureChildren(){

        final FloatingActionButton share = (FloatingActionButton) mainActivity.findViewById(R.id.shareListFAB);
        if(share !=null)
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Retrieving the current Task and extract data for intent
                    int posOnPager = DashboardFragment.mViewPager.getCurrentItem();
                    TaskList taskList = dataManager.findTaskListByPosition(posOnPager);
                    String title = mainActivity.getString(R.string.shareList) +": "+taskList.getTitle();

                    // Create the Intent and put the info to share
                    Intent shareIntent = ShareCompat.IntentBuilder
                            .from(mainActivity)
                            .setType("text/plain")          // Set the MIME type to filter the apps
                            .setText(LocalisationHelper.TaskListToString(taskList, mainActivity))   // Translate the TaskList to String
                            .setChooserTitle(title)         // Set a custom title for the chooser
                            .createChooserIntent();         // Build a custom dialog, not use defaults

                    // Avoid ActivityNotFoundException
                    if(shareIntent.resolveActivity(mainActivity.getPackageManager()) != null)
                        mainActivity.startActivity(shareIntent);
                }
            });

        FloatingActionButton addTask = (FloatingActionButton) mainActivity.findViewById(R.id.addTaskFAB);
        if(addTask != null)
            addTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fam.close(false);
                    mainPresenter.deployLayout(Constants.ADD_TASK);
                }
            });

        FloatingActionButton addList = (FloatingActionButton) mainActivity.findViewById(R.id.addListFAB);
        if(addList != null)
            addList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fam.close(true);
                    mainPresenter.deployLayout(Constants.ADD_TASK_LIST);
                }
            });

        FloatingActionButton addLabel = (FloatingActionButton) mainActivity.findViewById(R.id.addLabelFAB);
        if(addLabel!=null)
            addLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fam.close(true);
                    mainPresenter.deployLayout(Constants.ADD_LABEL);
                }
            });
    }

    // -------------------------- Use Cases --------------------------

    public void famVisibility(boolean toggle){
//        fam.showMenu(toggle);               // TODO Fails to show the menu properly

        fam.setVisibility( toggle ? View.VISIBLE : View.GONE );
    }
}