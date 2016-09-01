package com.sorry.model;


public class MibandMessage<T, V, K> {

    private K miBand;
    private T userInfo;
    private V listener;

    public MibandMessage(T userinfo, V listener, K miBand){
        if(userinfo != null) {
            this.userInfo = userinfo;
        }
        if(listener != null) {
            this.listener = listener;
        }
        if(miBand != null) {
            this.miBand = miBand;
        }
    }

    public K getMiBand() {
        return miBand;
    }

    public void setMiBand(K miBand) {
        this.miBand = miBand;
    }

    public V getListener() {
        return listener;
    }

    public void setListener(V listener) {
        this.listener = listener;
    }

    public T getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(T userInfo) {
        this.userInfo = userInfo;
    }


}
