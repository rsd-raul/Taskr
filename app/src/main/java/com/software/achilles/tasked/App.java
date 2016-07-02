package com.software.achilles.tasked;

import android.app.Application;

import com.software.achilles.tasked.presenter.TaskCreationPresenter;
import com.software.achilles.tasked.view.MainActivity;
import com.software.achilles.tasked.view.adapters.TaskAdapter;
import com.software.achilles.tasked.view.fragments.DashboardFragment;
import com.software.achilles.tasked.view.fragments.DashboardListFragment;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;

import javax.inject.Singleton;
import dagger.Component;

public class App extends Application {

    private AppComponent appComponent;

    @Singleton
    @Component(modules = AppModule.class)
    public interface AppComponent {
        void inject(App application);
        void inject(MainActivity mainActivity);
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
