package com.software.achilles.tasked.listeners;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.software.achilles.tasked.R;

public class FloatingActionMenuConfigurator {

    private Activity activity;
    private final FloatingActionMenu fam;

    public FloatingActionMenuConfigurator(Activity activity) {
        this.activity = activity;
        fam = (FloatingActionMenu) activity.findViewById(R.id.menuFAB);

        configureMenu();
        setOnScrollReaction();

        configureChildren();
    }

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

        // On click outside
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

    private void setOnScrollReaction(){

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
                if (scrollY-oldScrollY > 0)
                    fam.hideMenu(true);
                else
                    fam.showMenu(true);
            }
        });
    }

    private void configureChildren(){

        FloatingActionButton share = (FloatingActionButton) activity.findViewById(R.id.shareListFAB);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        FloatingActionButton addMultiple = (FloatingActionButton) activity.findViewById(R.id.addMultipleFAB);
        addMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        FloatingActionButton add = (FloatingActionButton) activity.findViewById(R.id.addFAB);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}