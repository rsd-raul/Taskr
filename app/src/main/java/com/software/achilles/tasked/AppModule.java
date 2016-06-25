package com.software.achilles.tasked;

import android.app.Application;
import dagger.Module;

@Module
public class AppModule {
    private Application app;

    public AppModule(Application app) {
        this.app = app;
    }
}