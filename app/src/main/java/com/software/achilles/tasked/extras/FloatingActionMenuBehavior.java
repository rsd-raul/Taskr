package com.software.achilles.tasked.extras;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;

// Original author: Grzesiek Gajewski - Base Lab - https://github.com/ggajews

public class FloatingActionMenuBehavior extends CoordinatorLayout.Behavior<FloatingActionMenu>{

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionMenu child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionMenu child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }
}
