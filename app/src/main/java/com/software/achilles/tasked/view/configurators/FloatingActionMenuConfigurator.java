package com.software.achilles.tasked.view.configurators;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.software.achilles.tasked.presenter.MainPresenter;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.MainActivity;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.controllers.TaskController;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.util.extras.FloatingActionMenuBehavior;
import com.software.achilles.tasked.view.fragments.DashboardFragment;

public class FloatingActionMenuConfigurator {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    //TODO Cambiado a public para poder usar traductores en el domain
    public static MainActivity activity;
    private static FloatingActionMenu fam;

    // ------------------------- Constructor -------------------------

    public FloatingActionMenuConfigurator(MainActivity activity) {
        FloatingActionMenuConfigurator.activity = activity;
        fam = (FloatingActionMenu) activity.findViewById(R.id.menuFAB);

        configureMenu();
//        setMenuOnScrollReaction();
        menuCustomizeBehaviour();
        configureChildren();
        setMenuOnScrollReaction();
    }

    // -------------------------- Use Cases --------------------------

    // -------------------------- FAB menu ---------------------------

    private void configureMenu(){

        // Change the background depending on the fabMenu Status
        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            Context context = activity.getApplicationContext();
            int fromColor = ContextCompat.getColor(context, R.color.transparent);
            int toColor = ContextCompat.getColor(context, R.color.background);

            // Creation of animator to transition between the transparent color and other
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



        // TODO onLongClick => Desplegar añadir tarea
//        fam.setOnLongClickListener(new FloatingActionMenu.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                NestedScrollView scrollView = (NestedScrollView) activity.findViewById(R.id.scrollView);
//                Snackbar.make(scrollView, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//                return true;
//            }
//        });
    }

//    public void closeFabMenu(){
//        fam.close(true);
//    }

    public static void setMenuOnScrollReaction(){

        // prepare animations
        Animation fab_slide_down = AnimationUtils.loadAnimation(activity, R.anim.fab_slide_down);
        fab_slide_down.setInterpolator(new AccelerateInterpolator());

        Animation fab_slide_up = AnimationUtils.loadAnimation(activity, R.anim.fab_slide_up);
        fab_slide_up.setInterpolator(new AccelerateInterpolator());

        // Set animations
        fam.setMenuButtonHideAnimation(fab_slide_down);
        fam.setMenuButtonShowAnimation(fab_slide_up);

        // Control the behaviour when scrolling
        // TODO Controlar el FAM y la barra cuando se detecta scroll
        // TODO Si no escondes FAM tienes que dar extra padding/margin a el viewpager (o el boton tapa las acciones)
//        activity.getSupportActionBar().hide();
//        activity.getSupportActionBar().show();

//        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerview);
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

//        NestedScrollView scrollView = (NestedScrollView) activity.findViewById(R.id.scrollView);
//        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

        // ---------------------- Marshmallow Only -----------------------

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        ViewPager viewPager = (ViewPager) activity.findViewById(R.id.viewpager);
        viewPager.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY,
                                       int oldScrollX, int oldScrollY) {
                if (scrollY - oldScrollY > 0)
                    fam.hideMenu(true);
                else
                    fam.showMenu(true);
            }
        });
    }

    // Make the menu react to external stimuli such as a Snackbar
    private void menuCustomizeBehaviour(){
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fam.getLayoutParams();
        params.setBehavior(new FloatingActionMenuBehavior());
        fam.requestLayout();
    }

    // -------------------------- FAB child --------------------------

    private void configureChildren(){

        final FloatingActionButton share = (FloatingActionButton) activity.findViewById(R.id.shareListFAB);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieving the current Task and extract data for intent
                int positionOnViewPager = DashboardFragment.mViewPager.getCurrentItem();
                TaskList taskList = TaskController.sTaskLists.get(positionOnViewPager);
                String title = activity.getString(R.string.shareList) +": "+taskList.getTitle();

                // Create the Intent and put the info to share
                Intent shareIntent = ShareCompat.IntentBuilder
                        .from(activity)
                        .setType("text/plain")          // Set the MIME type to filter the apps
                        .setText(taskList.toString())   // Translate the TaskList to String
                        .setChooserTitle(title)         // Set a custom title for the chooser
                        .createChooserIntent();         // Build a custom dialog, not use defaults

                // Avoid ActivityNotFoundException
                if(shareIntent.resolveActivity(activity.getPackageManager()) != null)
                    activity.startActivity(shareIntent);
            }
        });

        FloatingActionButton addTask = (FloatingActionButton) activity.findViewById(R.id.addTaskFAB);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fam.close(false);
                MainPresenter.getInstance().deployLayout(Constants.ADD_TASK);
            }
        });

        FloatingActionButton addList = (FloatingActionButton) activity.findViewById(R.id.addListFAB);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fam.close(true);
                MainPresenter.getInstance().deployLayout(Constants.ADD_TASK_LIST);
            }
        });

        FloatingActionButton addLabel = (FloatingActionButton) activity.findViewById(R.id.addLabelFAB);
        addLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fam.close(true);
                MainPresenter.getInstance().deployLayout(Constants.ADD_LABEL);
            }
        });
    }

    // -------------------------- Use Cases --------------------------

    public void famVisibility(boolean toggle){
//        if(toggle)                // Fails to show the menu properly
//            fam.showMenu(true);
//        else
//            fam.hideMenu(true);
        fam.setVisibility( toggle ? View.VISIBLE : View.GONE );
    }
}