package com.software.achilles.tasked;

import android.app.Application;

import com.software.achilles.tasked.view.MainActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Application app;

    public AppModule(Application app) {
        this.app = app;
    }
}