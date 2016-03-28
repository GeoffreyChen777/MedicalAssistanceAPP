package com.example.bracelet;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity {

	private Button signin;
	private Button signup;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); 
        //ͨ������findViewById()��ȡ���ʵ��
        signin = (Button) findViewById(R.id.signin);
        signup = (Button) findViewById(R.id.signup);
       
        signin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, signin.class);
				startActivity(intent);
				finish();
				
			}
        });
	}
	
	
}
	
	
