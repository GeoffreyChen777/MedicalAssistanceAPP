package com.sorry.band.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sorry.band.R;
import com.sorry.band.data.BodyInfor;
import com.sorry.band.widget.PublishToolBar;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends Activity {
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
        if(addInforBox.isChecked()){
            /*Cursor cursor = Database.query("select * from BodyData");
            List<BodyInfor> bodyInfors = new ArrayList<>();
            for(int i =0; i < cursor.getColumnCount(); i++) {
                cursor.moveToLast();
                BodyInfor bi = new BodyInfor();
                bi.setStep(cursor.getString(2));
                bi.setHeartRate(cursor.getString(1));
                bi.setDate(cursor.getString(0));
                bodyInfors.add(bi);
            }*/
            //String jsonStr = Json.encodeList(bodyInfors);
            //Http.get("http://115.159.200.151/post.php?value=" + jsonStr, null);
        }
    }
}
