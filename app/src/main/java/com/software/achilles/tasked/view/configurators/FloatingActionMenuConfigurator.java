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
        menuCustomizeBehaviour();
        configureChildren();
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
    }

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
        fam.setVisibility( toggle ? View.VISIBLE : View.GONE );
    }
}