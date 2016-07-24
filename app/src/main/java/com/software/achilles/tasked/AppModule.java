package com.software.achilles.tasked;

import android.content.Context;

import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class AppModule {

    @Provides
    FastItemAdapter<IItem> fastItemAdapterProvider(){
        return new FastItemAdapter<>();
    }

    @Provides
    Context contextProvider(){
        return App.getInstance();
    }

    @Provides
    Realm realmProvider(){
        return Realm.getDefaultInstance();
    }
}