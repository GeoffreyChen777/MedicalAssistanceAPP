package com.example.bracelet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Login extends Activity {

	private Button loginButton1;  //确认登录
	private Button loginButton2; //取消登录
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); 
        //通过方法findViewById()获取组件实例
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
