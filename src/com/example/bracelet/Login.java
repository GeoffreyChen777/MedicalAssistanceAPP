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
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity {
    private RequestQueue queue;
	private Button loginButton1;  //ȷ�ϵ�¼
	private Button loginButton2; //ȡ����¼
	private EditText loginname;  //�û���
	private EditText loginPassword; //����
	private TextView textview5;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); 
        //ͨ������findViewById()��ȡ���ʵ��
        queue = Volley.newRequestQueue(Login.this);
        loginButton1=(Button)findViewById(R.id.loginbutton1);
        loginButton2=(Button)findViewById(R.id.loginbutton2);
        loginname=(EditText)findViewById(R.id.loginname);
        loginPassword=(EditText)findViewById(R.id.loginPassword);
        loginButton1.setOnClickListener(new OnClickListener() {
         	@Override
    			public void onClick(View v) {
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
	 		        map.put("name", loginname.getText().toString());
	 		       // map.put("password", makeMD5(editPassword1.getText().toString())); //���������MD5����
	 		        map.put("password", makeMD5(loginPassword.getText().toString()));
	 		        map.put("type","signin");
	 		    return map; 
	 		    }  
	 		};
	 		  queue.add(stringRequest);
         	}
         });
        
        loginButton2.setOnClickListener(new OnClickListener() {
         	@Override
    			public void onClick(View v) {
    				Intent intent = new Intent();
    				intent.setClass(Login.this, MainActivity.class);
    		    	startActivity(intent);
    		
    			
    			}
          });
       
   
	}
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


		
}
