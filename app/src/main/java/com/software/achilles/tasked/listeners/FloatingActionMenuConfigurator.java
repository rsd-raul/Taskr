package com.software.achilles.tasked.listeners;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.software.achilles.tasked.DashboardActivity;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.extras.FloatingActionMenuBehavior;

public class FloatingActionMenuConfigurator {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private static DashboardActivity activity;
    private static FloatingActionMenu fam;

    // ------------------------- Constructor -------------------------

    public FloatingActionMenuConfigurator(DashboardActivity activity) {
        this.activity = activity;
        fam = (FloatingActionMenu) activity.findViewById(R.id.menuFAB);

        configureMenu();
//        setMenuOnScrollReaction();
        menuCustomizeBehaviour();

        configureChildren();


    }

    // -------------------------- Use Cases --------------------------

    // -------------------------- FAB menu ---------------------------

    private void configureMenu(){

        // Change the background depending on the fabMenu Status
        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            Context context = activity.getApplicationContext();
            int fromColor = ContextCompat.getColor(context, R.color.transparent);
            int toColor = ContextCompat.getColor(context, R.color.background);

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

    public void closeFabMenu(){
        fam.close(true);
    }

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
        NestedScrollView scrollView = (NestedScrollView) activity.findViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY,
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

        FloatingActionButton share = (FloatingActionButton) activity.findViewById(R.id.shareListFAB);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        FloatingActionButton addList = (FloatingActionButton) activity.findViewById(R.id.addListFAB);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton addTask = (FloatingActionButton) activity.findViewById(R.id.addTaskFAB);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeFabMenu();

                deployAddTaskLayout();
            }
        });
    }

    // -------------------------- Use Cases --------------------------


    private void deployAddTaskLayout(){

    }

    private void retractAddTaskLayout(){

    }
}