package com.software.achilles.tasked;

import android.app.Application;

import com.software.achilles.tasked.view.MainActivity;

import javax.inject.Singleton;
import dagger.Component;

public class App extends Application {

    private AppComponent appComponent;

    @Singleton
    @Component(modules = AppModule.class)
    public interface AppComponent {
        void inject(App application);
        void inject(MainActivity homeActivity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerApp_AppComponent
                .builder()
                .build();
        appComponent.inject(this);
    }

    public AppComponent component() {
        return appComponent;
    }
}
