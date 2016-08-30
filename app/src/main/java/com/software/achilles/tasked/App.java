package com.software.achilles.tasked;

import android.app.Application;

import com.software.achilles.tasked.model.factories.PrimaryKeyFactory;
import com.software.achilles.tasked.util.helpers.MigrationHelper;
import com.software.achilles.tasked.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    private AppComponent appComponent;

    private static App instance;

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


        // Set the RealmConfiguration and PrimaryKeyFactory for Realm usage
        Realm.setDefaultConfiguration(
                new RealmConfiguration.Builder(this)
                        .schemaVersion(0)
                        .migration(new MigrationHelper())
                        .build());
        PrimaryKeyFactory.initialize(Realm.getDefaultInstance());

        // Hack to access context related functionality from TaskDetailFAItem
        instance = this;
    }

    public AppComponent component() {
        return appComponent;
    }

    public static App getInstance(){
        return instance;
    }
}
