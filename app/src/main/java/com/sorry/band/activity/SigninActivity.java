package com.sorry.band.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.sorry.band.BandApplication;
import com.sorry.band.R;

import java.io.IOException;
import java.security.MessageDigest;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SigninActivity extends Activity {

	private OkHttpClient client;
	private ImageButton signin;
	private ImageButton signup;
	private RelativeLayout signinLayout;
	private RelativeLayout signupLayout;
	private EditText accountView;
	private EditText pwdView;
	private EditText signupAccountView;
	private EditText signupPwdView;
	private EditText signupCaptchaView;

	private ImageButton signinButton;
	private ImageButton signupButton;
	private ImageButton requestCodeButton;

	private final int LOGINRESPONSE = 0x40;
	private final int SIGNUPRESPONSE = 0x41;
	private int countdownTime = 60;

	private AnimationSet toTopAnimations;
	private AnimationSet toButtomAnimations;
	private TranslateAnimation toTopAnimation;
	private TranslateAnimation toButtomAnimation;
	private AlphaAnimation toTransparentAnimation;
	private AlphaAnimation toOpacityAnimation;

	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity);

		client = ((BandApplication)getApplication()).getOkHttpClient();

        signin = (ImageButton) findViewById(R.id.signinButton);
        signup = (ImageButton) findViewById(R.id.signupButton);

		signinLayout = (RelativeLayout) findViewById(R.id.signinLayout);
		signupLayout = (RelativeLayout) findViewById(R.id.signupLayout);

		signinButton = (ImageButton) findViewById(R.id.signinConfirmButton);
		signupButton = (ImageButton) findViewById(R.id.signupConfirmButton);
		requestCodeButton = (ImageButton) findViewById(R.id.requestCodeButton);
		accountView = (EditText) findViewById(R.id.accountEditView);
		pwdView = (EditText) findViewById(R.id.passwordEditView);
		signupAccountView = (EditText) findViewById(R.id.signupAccountEditView);
		signupPwdView = (EditText) findViewById(R.id.signupPasswordEditView);
		signupCaptchaView = (EditText) findViewById(R.id.signupCaptchaEditView);

		SMSSDK.initSDK(this, "11798364ca66a",
				"b1ce7745656b197a54c496bcb2c4b6a0");
		EventHandler eventHandler = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				loginHandler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);

		toTopAnimations = new AnimationSet(true);
		toButtomAnimations = new AnimationSet(true);
		toTopAnimation = new TranslateAnimation(0, 0, 0, -300);
		toButtomAnimation = new TranslateAnimation(0, 0, 0, 300);
		toTransparentAnimation = new AlphaAnimation(1, 0);
		toOpacityAnimation = new AlphaAnimation(0, 1);

		toTopAnimation.setDuration(200);
		toButtomAnimation.setDuration(200);
		toTransparentAnimation.setDuration(200);
		toOpacityAnimation.setDuration(200);

		toTopAnimations.addAnimation(toTopAnimation);
		toTopAnimations.addAnimation(toTransparentAnimation);
		toButtomAnimations.addAnimation(toButtomAnimation);
		toButtomAnimations.addAnimation(toTransparentAnimation);

        signin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				signin.startAnimation(toTopAnimations);
				signup.startAnimation(toButtomAnimations);
				signin.setVisibility(View.INVISIBLE);
				signup.setVisibility(View.INVISIBLE);
				signinLayout.setVisibility(View.VISIBLE);
				signinLayout.startAnimation(toOpacityAnimation);
			}
        });
        
        signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				signin.startAnimation(toTopAnimations);
				signup.startAnimation(toButtomAnimations);
				signin.setVisibility(View.INVISIBLE);
				signup.setVisibility(View.INVISIBLE);
				signupLayout.setVisibility(View.VISIBLE);
				signupLayout.startAnimation(toOpacityAnimation);
			}
        });

		signinButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				signin();
			}
		});

		signupButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				verifyMessageCode();
			}
		});

		requestCodeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getMessageCode();
			}
		});
		Log.i("MD5",MD5("123456"));
        
	}

	@Override
	public void onBackPressed() {

		if(signupLayout.getVisibility() == View.VISIBLE) {
			signupLayout.startAnimation(toTransparentAnimation);
			signupLayout.setVisibility(View.INVISIBLE);
		}else if(signinLayout.getVisibility() == View.VISIBLE) {
			signinLayout.startAnimation(toTransparentAnimation);
			signinLayout.setVisibility(View.INVISIBLE);
		}
		signin.startAnimation(toOpacityAnimation);
		signup.startAnimation(toOpacityAnimation);
		signin.setVisibility(View.VISIBLE);
		signup.setVisibility(View.VISIBLE);

	}

	private void verifyMessageCode(){
		SMSSDK.submitVerificationCode("86", signupAccountView.getText().toString(),
				signupCaptchaView.getText().toString());
	}

	private void signup(){
		String url = "http://115.159.200.151/login.php";
		Log.i("PWD",MD5(pwdView.getText().toString()));
		RequestBody formBody = new FormBody.Builder()
				.add("type", "signup")
				.add("password", MD5(signupPwdView.getText().toString()))
				.add("name", signupAccountView.getText().toString())
				.build();
		Request request = new Request.Builder()
				.url(url)
				.post(formBody)
				.build();

		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Log.i("Error","error");
			}

			@Override
			public void onResponse(Call call, final Response response) throws IOException {
				if (!response.isSuccessful()) {
					Log.i("Error","Unexpected code " + response);
					throw new IOException("Unexpected code " + response);

				}
				if (response.isSuccessful()) {
					String res = response.body().string();
					Log.i("Response",res);
					JSONObject result = JSONObject.parseObject(res);
					String code = result.getString("code");
					Message message = new Message();
					message.what = SIGNUPRESPONSE;
					message.obj = code;
					loginHandler.sendMessage(message);
				}
			}
		});
	}

	private void signin(){
		String url = "http://115.159.200.151/login.php";
		Log.i("PWD",MD5(pwdView.getText().toString()));
		RequestBody formBody = new FormBody.Builder()
				.add("type", "signin")
				.add("password", MD5(pwdView.getText().toString()))
				.add("name", accountView.getText().toString())
				.build();
		Request request = new Request.Builder()
				.url(url)
				.post(formBody)
				.build();

		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Log.i("Error","error");
			}

			@Override
			public void onResponse(Call call, final Response response) throws IOException {
				if (!response.isSuccessful()) {
					Log.i("Error","Unexpected code " + response);
					throw new IOException("Unexpected code " + response);

				}
				if (response.isSuccessful()) {
					String res = response.body().string();
					Log.i("Response",res);
					JSONObject result = JSONObject.parseObject(res);
					String code = result.getString("code");
					Message message = new Message();
					message.what = LOGINRESPONSE;
					message.obj = code;
					loginHandler.sendMessage(message);
				}
			}
		});
	}

	private void getMessageCode(){
		String phoneNums = signupAccountView.getText().toString();
		if (!judgePhoneNums(phoneNums)) {
			return;
		}
		SMSSDK.getVerificationCode("86", phoneNums);
		requestCodeButton.setClickable(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (; countdownTime > 0; countdownTime--) {
					loginHandler.sendEmptyMessage(-9);
					if (countdownTime <= 0) {
						break;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				loginHandler.sendEmptyMessage(-8);
			}
		}).start();

			/*case R.id.commit:
				SMSSDK.submitVerificationCode("86", phoneNums, inputCodeEt
						.getText().toString());
				createProgressBar();
				break;
		}*/
	}

	private void signinResponseWith(String code){
		Log.i("Code",code);
		if(code.equals("200")){
			Toast.makeText(
					getApplicationContext(),
					"Sign in Successfully",
					Toast.LENGTH_SHORT)
					.show();
			((BandApplication)getApplication()).setAccount(accountView.getText().toString());
			((BandApplication)getApplication()).setPwd(pwdView.getText().toString());
			Intent intent = new Intent();
			intent.setClass(SigninActivity.this,MainActivity.class);
			startActivity(intent);
		}
		else{
			Toast.makeText(
					getApplicationContext(),
					"Sign in Unsuccessfully",
					Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void signupResponseWith(String code){
		Log.i("Code",code);
		if(code.equals("200")){
			Toast.makeText(
					getApplicationContext(),
					"Sign up Successfully",
					Toast.LENGTH_SHORT)
					.show();
			((BandApplication)getApplication()).setAccount(signupAccountView.getText().toString());
			((BandApplication)getApplication()).setPwd(signupPwdView.getText().toString());
			Intent intent = new Intent();
			intent.setClass(SigninActivity.this,MainActivity.class);
			startActivity(intent);
		}
		else{
			Toast.makeText(
					getApplicationContext(),
					"Sign up Unsuccessfully",
					Toast.LENGTH_SHORT)
					.show();
		}
	}

	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	private boolean judgePhoneNums(String phoneNums) {
		if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
			return true;
		}
		Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
		return false;
	}

	public static boolean isMatchLength(String str, int length) {
		if (str.isEmpty()) {
			return false;
		} else {
			return str.length() == length ? true : false;
		}
	}

	public static boolean isMobileNO(String mobileNums) {

		String telRegex = "[1][3578]\\d{9}";
		if (TextUtils.isEmpty(mobileNums))
			return false;
		else
			return mobileNums.matches(telRegex);
	}

	Handler loginHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what){
				case LOGINRESPONSE:{
					signinResponseWith((String)msg.obj);
					break;
				}
				case SIGNUPRESPONSE:{
					signupResponseWith((String)msg.obj);
					break;
				}
				case -8:{
					requestCodeButton.setClickable(true);
					countdownTime = 60;
					break;
				}
				case -9:{
					break;
				}
				default:{
					int event = msg.arg1;
					int result = msg.arg2;
					Object data = msg.obj;
					Log.e("event", "event=" + event+result);
					if (result == SMSSDK.RESULT_COMPLETE) {
						if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
							signup();
						}else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
							Toast.makeText(getApplicationContext(), "验证码已经发送",
									Toast.LENGTH_SHORT).show();
						} else {
							((Throwable) data).printStackTrace();
						}
					}
				}
			}
			super.handleMessage(msg);
		}
	};

}
	
	
