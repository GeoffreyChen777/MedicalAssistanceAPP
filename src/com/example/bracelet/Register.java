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
	
	        // �ֻ��������
			private EditText inputPhoneEt;
			//���������
			private EditText editPassword1;
			//�����ٴ������
			private EditText editPassword2;
			

			// ��֤�������
			private EditText inputCodeEt;

			// ��ȡ��֤�밴ť
			private Button requestCodeBtn;

			// ע�ᰴť
			private Button commitBtn;
			//ȡ����ť
			private Button registerBtn;
			
			//��ʾ���������ص�����
			private TextView textview5;
			//
			int i = 30;
			
			
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register); 
        
     
        
        init();
   	}
   	
   	
   	
   	private void init() {
   		   		
   		
   	 //ͨ������findViewById()��ȡ���ʵ��
   		inputPhoneEt = (EditText) findViewById(R.id.login_input_phone_et);// �ֻ��������
   		editPassword1 = (EditText) findViewById(R.id.editPassword1); //���������
   		editPassword2 = (EditText) findViewById(R.id.editPassword2);  //�����ٴ������
		inputCodeEt = (EditText) findViewById(R.id.login_input_code_et); // ��֤�������
		requestCodeBtn = (Button) findViewById(R.id.login_request_code_btn);// ��ȡ��֤�밴ť
		commitBtn = (Button) findViewById(R.id.login_commit_btn);// ע�ᰴť
		registerBtn = (Button) findViewById(R.id.registerbutton2);// ȡ����ť
		textview5 = (TextView)findViewById(R.id.textview5);
		requestCodeBtn.setOnClickListener(this);
		commitBtn.setOnClickListener(this);
        // ����������֤sdk
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
        			//ע��ص������ӿ�
        			SMSSDK.registerEventHandler(eventHandler);
        		}
   	
   	

   @Override
   public void onClick(View v) {
	   	   
	   
   	String phoneNums = inputPhoneEt.getText().toString();
   	switch (v.getId()) {
   	case R.id.login_request_code_btn:
   		// 1. ͨ�������ж��ֻ���
   		if (!judgePhoneNums(phoneNums)) {
   			return;
   		} // 2. ͨ��sdk���Ͷ�����֤
   		SMSSDK.getVerificationCode("86", phoneNums);

   		// 3. �Ѱ�ť��ɲ��ɵ����������ʾ����ʱ�����ڻ�ȡ��
   		requestCodeBtn.setClickable(false);
   		requestCodeBtn.setText("���·���(" + i + ")");
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
   			requestCodeBtn.setText("���·���(" + i + ")");
   		} else if (msg.what == -8) {
   			requestCodeBtn.setText("��ȡ��֤��");
   			requestCodeBtn.setClickable(true);
   			i = 30;
   		} else {
   			int event = msg.arg1;
   			int result = msg.arg2;
   			Object data = msg.obj;
   			Log.e("event", "event=" + event);
   			if (result == SMSSDK.RESULT_COMPLETE) {
   				// ����ע��ɹ��󣬷���succcess,Ȼ����ʾ
   				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// �ύ��֤��ɹ�
   					Toast.makeText(getApplicationContext(), "�ύ��֤��ɹ�",Toast.LENGTH_SHORT).show();
   					
    	//������Ϣ��������
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
   			 		        map.put("password", makeMD5(editPassword1.getText().toString())); //���������MD5����
   			 		        return map;  
   			 		    }  
   			 		};
   			 		  queue.add(stringRequest);   					
   					
   					
   					Intent intent = new Intent();
   		    		intent.setClass(Register.this,Success.class);
   		    		startActivity(intent);
   				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
   					Toast.makeText(getApplicationContext(), "��֤���Ѿ�����",Toast.LENGTH_SHORT).show();
   					
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
    	    // ����һ��MD5���ܼ���ժҪ
    	    md = MessageDigest.getInstance("MD5");
    	    // ����md5����
    	    md.update(password.getBytes());
    	    // digest()���ȷ������md5 hashֵ������ֵΪ8Ϊ�ַ�������Ϊmd5 hashֵ��16λ��hexֵ��ʵ���Ͼ���8λ���ַ�
    	    // BigInteger������8λ���ַ���ת����16λhexֵ�����ַ�������ʾ���õ��ַ�����ʽ��hashֵ
    	    String pwd = new BigInteger(1, md.digest()).toString(16);
    	    System.err.println(pwd);
    	    return pwd;
    	   } catch (Exception e) {
    	    e.printStackTrace();
    	   }
    	   return password;
    	}


   /**
    * �ж��ֻ������Ƿ����
    * 
    * @param phoneNums
    */
   private boolean judgePhoneNums(String phoneNums) {
   	if (isMatchLength(phoneNums, 11)
   			&& isMobileNO(phoneNums)) {
   		return true;
   	}
   	Toast.makeText(this, "�ֻ�������������",Toast.LENGTH_SHORT).show();
   	return false;
   }

   /**
    * �ж�һ���ַ�����λ��
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
    * ��֤�ֻ���ʽ
    */
   public static boolean isMobileNO(String mobileNums) {
   	/*
   	 * �ƶ���134��135��136��137��138��139��150��151��157(TD)��158��159��187��188
   	 * ��ͨ��130��131��132��152��155��156��185��186 ���ţ�133��153��180��189����1349��ͨ��
   	 * �ܽ��������ǵ�һλ�ض�Ϊ1���ڶ�λ�ض�Ϊ3��5��8������λ�õĿ���Ϊ0-9
   	 */
   	String telRegex = "[1][358]\\d{9}";// "[1]"�����1λΪ����1��"[358]"����ڶ�λ����Ϊ3��5��8�е�һ����"\\d{9}"��������ǿ�����0��9�����֣���9λ��
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


	
