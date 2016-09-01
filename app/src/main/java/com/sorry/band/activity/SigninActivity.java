package com.sorry.band.activity;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
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
import com.sorry.core.ActionCallbackListener;
import com.sorry.core.AppAction;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SigninActivity extends BaseActivity {

	private OkHttpClient client;
	private ImageButton signin;
	private ImageButton signup;
	private RelativeLayout signinLayout;
	private RelativeLayout signupLayout;
	private EditText accountEdit;
	private EditText pwdEdit;
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
		initView();
		//generateTestData();
        
	}

	private void selectAllData(){
		appAction.getAllBodyData(new ActionCallbackListener<Cursor>() {
			@Override
			public void onSuccess(Cursor data) {
				Log.i("DBCOUNT",data.getCount()+"");
				while (data.moveToNext()) {
					Log.i("Date",data.getString(data.getColumnIndex("date")));
					Log.i("step",data.getDouble(data.getColumnIndex("step"))+"");
					Log.i("heart",data.getDouble(data.getColumnIndex("heartrate"))+"");
				}

			}

			@Override
			public void onFailure(String errorEvent, String message) {

			}
		});
	}

	private void generateTestData(){
		List<ContentValues> contentValuesList = new ArrayList<>();
		for(int i = 1; i <= 5; i++) {
			ContentValues cValue = new ContentValues();
			cValue.put("date", "08.2"+i);
			cValue.put("heartrate", "8"+i);
			cValue.put("step", "1088");
			contentValuesList.add(cValue);
		}
		appAction.deleteAlldata("BodyData");
		appAction.deleteAlldata("PerdayHeartRateData");
		appAction.deleteAlldata("PerdayStepData");
		appAction.insertIntoAllBodyData(contentValuesList);
		appAction.exec("insert into PerdayStepData (time, step) values('08.31','1024');");
		appAction.exec("insert into PerdayHeartRateData (time, heartrate) values('08.31 08:31','65');");
		appAction.exec("insert into PerdayHeartRateData (time, heartrate) values('08.31 12:31','100');");
		appAction.exec("insert into PerdayHeartRateData (time, heartrate) values('08.31 20:31','80');");
	}

	private void initView(){

		signin = (ImageButton) findViewById(R.id.signinButton);
		signup = (ImageButton) findViewById(R.id.signupButton);

		signinLayout = (RelativeLayout) findViewById(R.id.signinLayout);
		signupLayout = (RelativeLayout) findViewById(R.id.signupLayout);

		signinButton = (ImageButton) findViewById(R.id.signinConfirmButton);
		signupButton = (ImageButton) findViewById(R.id.signupConfirmButton);
		requestCodeButton = (ImageButton) findViewById(R.id.requestCodeButton);
		accountEdit = (EditText) findViewById(R.id.edit_account_signin);
		pwdEdit = (EditText) findViewById(R.id.edit_pwd_signin);
		signupAccountView = (EditText) findViewById(R.id.signupAccountEditView);
		signupPwdView = (EditText) findViewById(R.id.signupPasswordEditView);
		signupCaptchaView = (EditText) findViewById(R.id.signupCaptchaEditView);

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

	public void toSignup(View view){
		final String signupName = signupAccountView.getText().toString();
		final String password = signupPwdView.getText().toString();
		String code = signupCaptchaView.getText().toString();
		signupButton.setEnabled(false);

		this.appAction.verifySmsCode(signupName, code, password, new ActionCallbackListener<Integer>() {
					//验证码验证回调
					@Override
					public void onSuccess(Integer data) {
							appAction.showToast(getString(R.string.toast_verifySMSCode_success), Toast.LENGTH_SHORT, toastHanlder);
                            appAction.register(signupName, password, new ActionCallbackListener<Void>() {
                                @Override
                                public void onSuccess(Void data) {
                                    appAction.showToast(getString(R.string.toast_signup_success), Toast.LENGTH_SHORT, toastHanlder);
                                    application.setAccount(signupName);
                                    application.setPwd(password);
                                    Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(String errorEvent, String message) {
                                    appAction.showToast(message, Toast.LENGTH_SHORT, toastHanlder);
                                    uiAction.changeClickable(signupButton);
                                }
                            });
						}

					@Override
					public void onFailure(String errorEvent, String message) {
						appAction.showToast(message, Toast.LENGTH_SHORT, toastHanlder);
						signupButton.setEnabled(true);
					}
				});
	}

    public void toSignin(View view){
		final String signinName = accountEdit.getText().toString();
        final String password = pwdEdit.getText().toString();
        signinButton.setEnabled(false);

        this.appAction.login(signinName, password, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
				appAction.showToast(getString(R.string.toast_login_success), Toast.LENGTH_SHORT, toastHanlder);
                application.setAccount(signinName);
                application.setPwd(password);
                Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                uiAction.changeClickable(signinButton);
				appAction.showToast(message, Toast.LENGTH_SHORT, toastHanlder);
            }
        });
	}

	public void toSendMessageCode(View view){
		String phoneNums = signupAccountView.getText().toString();
		requestCodeButton.setClickable(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (; countdownTime > 0; countdownTime--) {
					if (countdownTime <= 0) {
						countdownTime = 60;
                        uiAction.changeClickable(requestCodeButton);
						break;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		this.appAction.sendSmsCode(phoneNums, new ActionCallbackListener<Integer>() {
			@Override
			public void onSuccess(Integer data) {
				if(data == SMSSDK.EVENT_GET_VERIFICATION_CODE){
					appAction.showToast(getString(R.string.toast_sendSMSCode_success), Toast.LENGTH_SHORT, toastHanlder);
				}
			}

			@Override
			public void onFailure(String errorEvent, String message) {
				appAction.showToast(message, Toast.LENGTH_SHORT, toastHanlder);
			}
		});
	}
}
	
	
