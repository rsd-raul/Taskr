package com.software.achilles.tasked.presenter;

import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.MainActivity;

public class MainPresenter implements Presenter<MainActivity, MainPresenter> {

    private MainActivity mActivity;
    private static MainPresenter instance;

    // ------------------------- Constructor -------------------------

    public static MainPresenter getInstance() {
        if(instance == null)
            instance = new MainPresenter();
        return instance;
    }

    // ------------------------- Life Cycle --------------------------

    @Override
    public MainPresenter attachView(MainActivity view) {
        mActivity = view;
        return instance;
    }

    public static void destroyPresenter() {
        if(instance == null)
            return;

        instance.mActivity = null;
        instance = null;

//      Un-subscribe from the thread?
//        if (subscription != null) subscription.unsubscribe();
    }

    public void backToBack(){
        mActivity.removeAddTask();
    }

    public void deployLayout(int key){
        switch (key) {
            case Constants.ADD_TASK:
                mActivity.deployAddTask();
                break;
            case Constants.ADD_LABEL:
                break;
            case Constants.ADD_TASK_LIST:
                break;
        }
    }
}
