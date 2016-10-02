package com.software.achilles.tasked.presenter;

public interface Presenter<V> {

    void attachView(V view);

    //    void destroyPresenter();
}
