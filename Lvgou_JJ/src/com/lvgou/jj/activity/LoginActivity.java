package com.lvgou.jj.activity;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.jj.Constants;
import com.lvgou.jj.R;
import com.lvgou.jj.utils.Utils;
import com.lvgou.jj.xmpp.XMPPHelper;

public class LoginActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.username)
	private EditText username;
	@ViewInject(R.id.password)
	private EditText password;
	@ViewInject(R.id.loginbtn)
	private Button loginbtn;
	@ViewInject(R.id.regbtn)
	private Button regbtn;

	private String name;
	private String pwd;

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (LoginActivity.this.isFinishing() || null == LoginActivity.this) {
				return;
			}
			switch (msg.what) {
			case 1:
				// 登录成功
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
				Map<String, String> map = new HashMap<String, String>();
				map.put(Constants.XMPP_USERNAME, name);
				map.put(Constants.XMPP_PASSWORD, pwd);
				Utils.editSharedPreferences(LoginActivity.this, map,
						Constants.SHARED_PREFERENCE_NAME);
				finish();
				Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:
				// 注册成功
				Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT)
						.show();
				break;
			case 1001:
				// 登录失败
				Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case 1002:
				// 注册失败
				Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case 1003:
				// 链接失败
				Toast.makeText(LoginActivity.this, "链接失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case 1010:
				// 链接失败
				Toast.makeText(LoginActivity.this, "已登录", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.login);
		ViewUtils.inject(this);
		loginbtn.setOnClickListener(this);
		regbtn.setOnClickListener(this);
		Utils.initXmpp(this);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginbtn:
			doLogin();
			break;
		case R.id.regbtn:

			break;
		default:
			break;
		}
	}

	private void doLogin() {
		name = username.getText().toString().trim();
		pwd = password.getText().toString().trim();
		new Thread(new Runnable() {
			@Override
			public void run() {
				XMPPHelper.getInstance().connect(handler, name, pwd);
				Log.i("XMPPClient", "Logged in as "
						+ XMPPHelper.getConnection().getUser());
				// Presence presence = new
				// Presence(Presence.Type.available);
				// XmppTool.getConnection(LoginActivity.this).sendPacket(presence);
			}
		}).start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
