package com.sorry.model;

import java.util.List;

/**
 * Created by sorry on 9/1/16.
 */
public class PostData {
    private String account;
    private String title;
    private String content;
    private List<SignData> signDataList;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostData(String account, String title, String content, List<SignData> signDataList) {
        this.account = account;
        this.title = title;
        this.content = content;
        this.signDataList = signDataList;
    }

    public PostData() {
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SignData> getSignDataList() {
        return signDataList;
    }

    public void setSignDataList(List<SignData> signDataList) {
        this.signDataList = signDataList;
    }




}
