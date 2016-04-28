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
	private Button loginButton1;  //确认登录
	private Button loginButton2; //取消登录
	private EditText loginname;  //用户名
	private EditText loginPassword; //密码
	private TextView textview5;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); 
        //通过方法findViewById()获取组件实例
        queue = Volley.newRequestQueue(Login.this);
        loginButton1=(Button)findViewById(R.id.loginbutton1);
        loginButton2=(Button)findViewById(R.id.loginbutton2);
        loginname=(EditText)findViewById(R.id.loginname);
        loginPassword=(EditText)findViewById(R.id.loginPassword);
        loginButton1.setOnClickListener(new OnClickListener() {
         	@Override
    			public void onClick(View v) {
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
	 		        map.put("name", loginname.getText().toString());
	 		       // map.put("password", makeMD5(editPassword1.getText().toString())); //将密码进行MD5加密
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


		
}
