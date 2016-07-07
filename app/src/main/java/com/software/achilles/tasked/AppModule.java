package com.software.achilles.tasked;

import android.content.Context;

import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.software.achilles.tasked.view.adapters.TaskAdapter;
import com.software.achilles.tasked.view.adapters.TaskDetailAdapter;

import dagger.Module;
import dagger.Provides;

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
}