package com.sorry.core;

import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.sorry.model.ViewMessage;

/**
 * Created by sorry on 8/31/16.
 */
public class UIActionImpl implements UIAction{
    private UIHanlder uiHanlder;

    public UIActionImpl(UIHanlder uiHanlder){
        this.uiHanlder = uiHanlder;
    }

    @Override
    public void changeClickable(View obj){
        Message msg = new Message();
        msg.what = UIHanlder.CHANGE_CLICKABLE;
        msg.obj = obj;
        this.uiHanlder.sendMessage(msg);
    }

    @Override
    public void setText(ViewMessage<TextView, String> viewMessage) {
        Message msg = new Message();
        msg.what = UIHanlder.SET_TEXT;
        msg.obj = viewMessage;
        this.uiHanlder.sendMessage(msg);
    }

    @Override
    public void changeVisiable(View obj) {
        Message msg = new Message();
        msg.what = UIHanlder.CHANGE_VISIABLE;
        msg.obj = obj;
        this.uiHanlder.sendMessage(msg);
    }
}
