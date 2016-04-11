package com.example.bracelet;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity {

	private Button login1; //首页中登录按钮
	private Button register;//首页中的注册按钮
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
        //通过方法findViewById()获取组件实例
        login1 = (Button) findViewById(R.id.login1);
        register = (Button) findViewById(R.id.register);
       
        login1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this,Login.class);
				startActivity(intent);
				
				
			}
        });
        
        register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, Register.class);
				startActivity(intent);
				
			}
        });
        
	}
	
	
}
	
	
