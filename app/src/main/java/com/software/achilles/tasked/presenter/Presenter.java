package com.software.achilles.tasked.presenter;

public interface Presenter<V,T> {

    T attachView(V view);

//    void destroyPresenter();
}
