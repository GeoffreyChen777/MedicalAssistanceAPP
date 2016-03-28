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
        //通过方法findViewById()获取组件实例
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
	
	
