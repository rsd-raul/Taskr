package com.software.achilles.tasked;

import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.software.achilles.tasked.view.adapters.TaskAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    FastItemAdapter<TaskAdapter> fastItemAdapterTaskProvider(){
        return new FastItemAdapter<>();
    }
}