package com.sorry.core;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sorry.model.ViewMessage;


public class UIHanlder extends Handler {
    public final static int CHANGE_CLICKABLE = 0X90;
    public final static int SET_TEXT = 0X91;
    public final static int CHANGE_VISIABLE = 0X92;


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case CHANGE_CLICKABLE:{
                ((View) msg.obj).setEnabled(!((ImageButton) msg.obj).isEnabled());
                break;
            }
            case SET_TEXT:{
                ViewMessage<TextView, String> viewMessage = (ViewMessage<TextView, String>)msg.obj;
                viewMessage.getView().setText(viewMessage.getMessage());
                break;
            }
            case CHANGE_VISIABLE:{
                if(((View) msg.obj).getVisibility() == View.VISIBLE) {
                    ((View) msg.obj).setVisibility(View.INVISIBLE);
                }
                else {
                    ((View) msg.obj).setVisibility(View.VISIBLE);
                }
                break;
            }
        }
    }
}
