package com.example.bracelet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Signin extends Activity {

	private Button button1;
	private Button button2;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin); 
        //通过方法findViewById()获取组件实例
        button1 = (Button) findViewById(R.id.signin);
        button2 = (Button) findViewById(R.id.exit1);
        
        button1.setOnClickListener(new OnClickListener() {
         	@Override
    			public void onClick(View v) {
    				Intent intent = new Intent();
    				intent.setClass(Signin.this, Id.class);
    		    	startActivity(intent);
    		
    			
    			}
          });
       
       button2.setOnClickListener(new OnClickListener() {
     	@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Signin.this, MainActivity.class);
		    	startActivity(intent);
		
			
			}
      });
	}

		
}
