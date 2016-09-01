package com.sorry.band.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sorry.band.R;
import com.sorry.band.data.BodyInfor;
import com.sorry.band.widget.PublishToolBar;
import com.sorry.core.ActionCallbackListener;
import com.sorry.core.AppAction;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends BaseActivity {
    private PublishToolBar titlebar;
    private ImageButton backButton;
    private ImageButton postButton;
    private EditText titleView;
    private EditText contentView;
    private CheckBox addInforBox;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_activity);
        titlebar = (PublishToolBar) findViewById(R.id.publishToolBar);
        backButton = titlebar.getBackButton();
        postButton = titlebar.getPublishButton();
        titleView = (EditText) findViewById(R.id.titleEditView);
        contentView = (EditText) findViewById(R.id.contentEditView);
        addInforBox = (CheckBox) findViewById(R.id.addInforBox);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostActivity.this.finish();
            }
        });
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });
    }


    private void post(){
        String title = titleView.getText().toString();
        String content = contentView.getText().toString();
        if(title.equals("")){
            Toast.makeText(PostActivity.this, "请输入标题！", Toast.LENGTH_SHORT).show();
        }
        if(content.equals("")){
            Toast.makeText(PostActivity.this, "请输入内容！", Toast.LENGTH_SHORT).show();
        }

        appAction.pushPost(application.getAccount(), title, content, addInforBox.isChecked(), new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                appAction.showToast("发布成功", Toast.LENGTH_LONG, toastHanlder);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                Log.i("PostError", errorEvent+message);
            }
        });
    }
}
