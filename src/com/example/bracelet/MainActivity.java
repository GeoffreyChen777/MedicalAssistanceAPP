package com.example.bracelet;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity {

	private Button login1; //��ҳ�е�¼��ť
	private Button register;//��ҳ�е�ע�ᰴť
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
        //ͨ������findViewById()��ȡ���ʵ��
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
	
	
