package com.sorry.model;

import android.view.View;


public class ViewMessage<T, V> {

    private T view;
    private V message;

    public ViewMessage(T view, V message){
        this.view = view;
        this.message = message;
    }

    public T getView() {
        return view;
    }

    public void setView(T view) {
        this.view = view;
    }

    public V getMessage() {
        return message;
    }

    public void setMessage(V message) {
        this.message = message;
    }


}
