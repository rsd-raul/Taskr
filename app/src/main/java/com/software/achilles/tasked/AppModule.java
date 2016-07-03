package com.software.achilles.tasked;

import android.content.Context;

import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.software.achilles.tasked.view.adapters.TaskAdapter;
import com.software.achilles.tasked.view.adapters.TaskDetailAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    FastItemAdapter<TaskAdapter> fastItemAdapterTaskProvider(){
        return new FastItemAdapter<>();
    }

    @Provides
    FastItemAdapter<TaskDetailAdapter> fastItemAdapterTaskDetailProvider(){
        return new FastItemAdapter<>();
    }

    @Provides
    Context contextProvider(){
        return App.getInstance();
    }
}