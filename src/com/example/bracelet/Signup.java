package com.example.bracelet;

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

public class Signup extends Activity implements OnClickListener {

	private Button exit;

	// �ֻ��������
	private EditText inputPhoneEt;

	// ��֤�������
	private EditText inputCodeEt;

	// ��ȡ��֤�밴ť
	private Button requestCodeBtn;

	// ע�ᰴť
	private Button commitBtn;

	private EditText inputPasswordEt;
	// ����
	private TextView textView1;

	private RequestQueue queue;

	int i = 60;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		init();
	}

	private void init() {

		// ͨ������findViewById()��ȡ���ʵ��
		inputPhoneEt = (EditText) findViewById(R.id.phone);

		inputCodeEt = (EditText) findViewById(R.id.inputcode);
		requestCodeBtn = (Button) findViewById(R.id.requestcode);
		commitBtn = (Button) findViewById(R.id.commit);

		inputPasswordEt = (EditText) findViewById(R.id.password);
		textView1 = (TextView) findViewById(R.id.TextView1);
		exit = (Button) findViewById(R.id.exit2);
		queue = Volley.newRequestQueue(Signup.this);

		requestCodeBtn.setOnClickListener(this);
		commitBtn.setOnClickListener(this);

		exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Signup.this, MainActivity.class);
				startActivity(intent);

			}
		});

		// ����������֤sdk
		SMSSDK.initSDK(this, "11798364ca66a",
				"b1ce7745656b197a54c496bcb2c4b6a0");
		EventHandler eventHandler = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// ע��ص������ӿ�
		SMSSDK.registerEventHandler(eventHandler);
	}

	@Override
	public void onClick(View v) {
		String phoneNums = inputPhoneEt.getText().toString();
		switch (v.getId()) {
		case R.id.requestcode:
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

		case R.id.commit:
			SMSSDK.submitVerificationCode("86", phoneNums, inputCodeEt
					.getText().toString());
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
				i = 60;
			} else {
				int event = msg.arg1;
				int result = msg.arg2;
				Object data = msg.obj;
				Log.e("event", "event=" + event);
				if (result == SMSSDK.RESULT_COMPLETE) {
					// ����ע��ɹ��󣬷���ID,Ȼ����ʾ
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// �ύ��֤��ɹ�
						Toast.makeText(getApplicationContext(), "�ύ��֤��ɹ�",
								Toast.LENGTH_SHORT).show();

					commitBtn.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								StringRequest stringRequest = new StringRequest(
										Method.POST,
										"http://115.159.200.151/php.php",
										new Response.Listener<String>() {
											@Override
											public void onResponse(
													String response) {
												Log.d("TAG", response);
												textView1.setText(response);

											}
										}, new Response.ErrorListener() {
											@Override
											public void onErrorResponse(
													VolleyError error) {
												Log.e("TAG",
														error.getMessage(),
														error);
											}
										}) {
									@Override
									protected Map<String, String> getParams()
											throws AuthFailureError {
										Map<String, String> map = new HashMap<String, String>();

										map.put("name", inputPhoneEt.getText()
												.toString());

										map.put("password", MD5(inputPasswordEt
												.getText().toString()));

										return map;
									}
								};

								queue.add(stringRequest);

							}
						});

						Intent intent = new Intent();
						intent.setClass(Signup.this, Id.class);
						startActivity(intent);

					} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
						Toast.makeText(getApplicationContext(), "��֤���Ѿ�����",
								Toast.LENGTH_SHORT).show();
					} else {
						((Throwable) data).printStackTrace();
					}
				}
			}
		}
	};

	/**
	 * �ж��ֻ������Ƿ����
	 * 
	 * @param phoneNums
	 */
	private boolean judgePhoneNums(String phoneNums) {
		if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
			return true;
		}
		Toast.makeText(this, "�ֻ�������������", Toast.LENGTH_SHORT).show();
		return false;
	}

	/**
	 * �ж�һ���ַ�����λ��
	 * 
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
		String telRegex = "[1][3578]\\d{9}";// "[1]"�����1λΪ����1��"[358]"����ڶ�λ����Ϊ3��5��8�е�һ����"\\d{9}"��������ǿ�����0��9�����֣���9λ��
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

}
