package com.example.bracelet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Login extends Activity {

	private Button loginButton1;  //ȷ�ϵ�¼
	private Button loginButton2; //ȡ����¼
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); 
        //ͨ������findViewById()��ȡ���ʵ��
        loginButton1=(Button)findViewById(R.id.loginbutton1);
        loginButton2=(Button)findViewById(R.id.loginbutton2);
        
        loginButton2.setOnClickListener(new OnClickListener() {
         	@Override
    			public void onClick(View v) {
    				Intent intent = new Intent();
    				intent.setClass(Login.this, MainActivity.class);
    		    	startActivity(intent);
    		
    			
    			}
          });
       
   
	}

		
}
