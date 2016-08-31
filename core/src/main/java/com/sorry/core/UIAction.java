package com.sorry.core;

import android.view.View;
import android.widget.TextView;

import com.sorry.model.ViewMessage;


public interface UIAction {
    //更改可选性
    public void changeClickable(View obj);
    //更改TextView显示文字
    public void setText(ViewMessage<TextView ,String> obj);
    //更改组件可见性
    public void changeVisiable(View obj);
}
