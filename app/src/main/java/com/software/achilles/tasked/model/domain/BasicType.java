package com.software.achilles.tasked.model.domain;

import android.support.annotation.NonNull;

public interface BasicType{

    long getId();
    void setId(long id);

    String getTitle();
    void setTitle(@NonNull String title);
}
