package com.example.bracelet;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;

public class Register extends Activity implements OnClickListener {
	        private RequestQueue queue;

	
	        private Button exit;
	
	        // 手机号输入框
			private EditText inputPhoneEt;
			//密码输入框
			private EditText editPassword1;
			//密码再次输入框
			private EditText editPassword2;
			

			// 验证码输入框
			private EditText inputCodeEt;

			// 获取验证码按钮
			private Button requestCodeBtn;

			// 注册按钮
			private Button commitBtn;
			//取消按钮
			private Button registerBtn;
			
			//显示服务器返回的内容
			private TextView textview5;
			//
			int i = 30;
			
			
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register); 
        
     
        
        init();
   	}
   	
   	
   	
   	private void init() {
   		   		
   		
   	 //通过方法findViewById()获取组件实例
   		inputPhoneEt = (EditText) findViewById(R.id.login_input_phone_et);// 手机号输入框
   		editPassword1 = (EditText) findViewById(R.id.editPassword1); //密码输入框
   		editPassword2 = (EditText) findViewById(R.id.editPassword2);  //密码再次输入框
		inputCodeEt = (EditText) findViewById(R.id.login_input_code_et); // 验证码输入框
		requestCodeBtn = (Button) findViewById(R.id.login_request_code_btn);// 获取验证码按钮
		commitBtn = (Button) findViewById(R.id.login_commit_btn);// 注册按钮
		registerBtn = (Button) findViewById(R.id.registerbutton2);// 取消按钮
		textview5 = (TextView)findViewById(R.id.textview5);
		requestCodeBtn.setOnClickListener(this);
		commitBtn.setOnClickListener(this);
        // 启动短信验证sdk
        			SMSSDK.initSDK(this, "1194d856fdc9c", "7ec15549f13496d57ce142c7fe5e1176");
        			EventHandler eventHandler = new EventHandler(){
        				@Override
        				public void afterEvent(int event, int result, Object data) {
        					Message msg = new Message();
        					msg.arg1 = event;
        					msg.arg2 = result;
        					msg.obj = data;
        					handler.sendMessage(msg);
        				}
        			};
        			//注册回调监听接口
        			SMSSDK.registerEventHandler(eventHandler);
        		}
   	
   	

   @Override
   public void onClick(View v) {
	   	   
	   
   	String phoneNums = inputPhoneEt.getText().toString();
   	switch (v.getId()) {
   	case R.id.login_request_code_btn:
   		// 1. 通过规则判断手机号
   		if (!judgePhoneNums(phoneNums)) {
   			return;
   		} // 2. 通过sdk发送短信验证
   		SMSSDK.getVerificationCode("86", phoneNums);

   		// 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
   		requestCodeBtn.setClickable(false);
   		requestCodeBtn.setText("重新发送(" + i + ")");
   		new Thread(new Runnable() {
   			@Override
   			public void run() {
   				for (; i > 0; i--) {
   					handler.sendEmptyMessage(-9);
   					if (i <= 0) {
   						break;
   					}
   					try {
   						Thread.sleep(1000);
   					} catch (InterruptedException e) {
   						e.printStackTrace();
   					}
   				}
   				handler.sendEmptyMessage(-8);
   			}
   		}).start();
   		break;

   	case R.id.login_commit_btn:
   		
   		SMSSDK.submitVerificationCode("86", phoneNums, inputCodeEt.getText().toString());
   		createProgressBar();
   		
	   		
   		
   		break;
   	}
   }

   /**
    * 
    */
   Handler handler = new Handler() {
   	public void handleMessage(Message msg) {
   		if (msg.what == -9) {
   			requestCodeBtn.setText("重新发送(" + i + ")");
   		} else if (msg.what == -8) {
   			requestCodeBtn.setText("获取验证码");
   			requestCodeBtn.setClickable(true);
   			i = 30;
   		} else {
   			int event = msg.arg1;
   			int result = msg.arg2;
   			Object data = msg.obj;
   			Log.e("event", "event=" + event);
   			if (result == SMSSDK.RESULT_COMPLETE) {
   				// 短信注册成功后，返回succcess,然后提示
   				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
   					Toast.makeText(getApplicationContext(), "提交验证码成功",Toast.LENGTH_SHORT).show();
   					
    	//发送信息到服务器
   			   		StringRequest stringRequest = new StringRequest(Method.POST,"http://115.159.200.151/php.php", new Response.Listener<String>() {  
   			            @Override  
   			            public void onResponse(String response) {  
   			                Log.d("TAG", response);  
   			                textview5.setText(response);
   			            }  
   			        }, new Response.ErrorListener() {  
   			            @Override  
   			            public void onErrorResponse(VolleyError error) {  
   			                Log.e("TAG", error.getMessage(), error);  
   			            }  
   			        }) {  
   			 		    @Override  
   			 		    protected Map<String, String> getParams() throws AuthFailureError {  
   			 		        Map<String, String> map = new HashMap<String, String>();  
   			 		        map.put("name", inputPhoneEt.getText().toString());
   			 		        map.put("password", makeMD5(editPassword1.getText().toString())); //将密码进行MD5加密
   			 		        return map;  
   			 		    }  
   			 		};
   			 		  queue.add(stringRequest);   					
   					
   					
   					Intent intent = new Intent();
   		    		intent.setClass(Register.this,Success.class);
   		    		startActivity(intent);
   				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
   					Toast.makeText(getApplicationContext(), "验证码已经发送",Toast.LENGTH_SHORT).show();
   					
   				} else {
   					((Throwable) data).printStackTrace();
   				}
   			}
   		}
   	}
  	
   };
	public String makeMD5(String password) {
    	MessageDigest md;
    	   try {
    	    // 生成一个MD5加密计算摘要
    	    md = MessageDigest.getInstance("MD5");
    	    // 计算md5函数
    	    md.update(password.getBytes());
    	    // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
    	    // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
    	    String pwd = new BigInteger(1, md.digest()).toString(16);
    	    System.err.println(pwd);
    	    return pwd;
    	   } catch (Exception e) {
    	    e.printStackTrace();
    	   }
    	   return password;
    	}


   /**
    * 判断手机号码是否合理
    * 
    * @param phoneNums
    */
   private boolean judgePhoneNums(String phoneNums) {
   	if (isMatchLength(phoneNums, 11)
   			&& isMobileNO(phoneNums)) {
   		return true;
   	}
   	Toast.makeText(this, "手机号码输入有误！",Toast.LENGTH_SHORT).show();
   	return false;
   }

   /**
    * 判断一个字符串的位数
    * @param str
    * @param length
    * @return
    */
   public static boolean isMatchLength(String str, int length) {
   	if (str.isEmpty()) {
   		return false;
   	} else {
   		return str.length() == length ? true : false;
   	}
   }

   /**
    * 验证手机格式
    */
   public static boolean isMobileNO(String mobileNums) {
   	/*
   	 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
   	 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
   	 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
   	 */
   	String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
   	if (TextUtils.isEmpty(mobileNums))
   		return false;
   	else
   		return mobileNums.matches(telRegex);
   }
   /**
    * progressbar
    */
   private void createProgressBar() {
   	FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
   	FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
   			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
   	layoutParams.gravity = Gravity.CENTER;
   	ProgressBar mProBar = new ProgressBar(this);
   	mProBar.setLayoutParams(layoutParams);
   	mProBar.setVisibility(View.VISIBLE);   
   	layout.addView(mProBar);
   }

   @Override
   protected void onDestroy() {
   	SMSSDK.unregisterAllEventHandler();
   	super.onDestroy();
   }
}


	
