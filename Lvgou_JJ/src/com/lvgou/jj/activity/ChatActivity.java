package com.lvgou.jj.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.jj.Constants;
import com.lvgou.jj.R;
import com.lvgou.jj.adapter.ChatMsgViewAdapter;
import com.lvgou.jj.been.ChatMessage;
import com.lvgou.jj.utils.Utils;
import com.lvgou.jj.xmpp.XMPPHelper;

public class ChatActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.titletext)
	private TextView titletext;
	@ViewInject(R.id.formclient_listview)
	private ListView formclient_listview;
	@ViewInject(R.id.formclient_btsend)
	private Button formclient_btsend;
	@ViewInject(R.id.formclient_text)
	private EditText formclient_text;

	private MultiUserChat muc;
	private List<ChatMessage> list;
	private ChatMsgViewAdapter adapter;
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.chat);
		ViewUtils.inject(this);
		Intent intent = getIntent();
		if (intent != null) {
			String title = intent.getStringExtra("title");
			titletext.setText(title);
			String jid = intent.getStringExtra("jid");
			XMPPHelper.getInstance().addRoom(Utils.getUserName(this), jid,
					handler_add, handler_chat);
		}

		// muc = XMPPHelper.muc;
		initView();
		// if (muc != null) {
		// titletext.setText(muc.getRoom());
		// }

		username = Utils.getUserName(this);
		super.onCreate(savedInstanceState);
	}

	private void initView() {
		list = new ArrayList<ChatMessage>();
		adapter = new ChatMsgViewAdapter(this, list);
		formclient_listview.setAdapter(adapter);
		formclient_btsend.setOnClickListener(this);
	}

	private Handler handler_chat = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			ChatMessage cm = (ChatMessage) msg.obj;
			switch (msg.what) {
			case Constants.RECEIVEMSG:
				if (username.equals(cm.getFrom())) {
					cm.setIssend(true);
				} else {
					cm.setIssend(false);
				}
				list.add(cm);
				adapter.notifyDataSetChanged();
				formclient_listview
						.setSelection(formclient_listview.getCount() - 1);
				break;
			case Constants.SENDMSG:
				try {
					XMPPHelper.getInstance();
					muc.sendMessage(cm.getBody());
				} catch (XMPPException e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
				formclient_listview
						.setSelection(formclient_listview.getCount() - 1);
				formclient_text.setText("");
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	private Handler handler_add = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				muc = (MultiUserChat) msg.obj;
				Toast.makeText(getApplicationContext(), "进入房间成功",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "进入房间失败",
						Toast.LENGTH_SHORT).show();
				finish();
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.formclient_btsend:
			String body = formclient_text.getText().toString().trim();
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			ChatMessage cm = new ChatMessage();
			cm.setBody(body);
			cm.setDate(format.format(new Date()));
			Message msg = new Message();
			msg.obj = cm;
			msg.what = Constants.SENDMSG;
			handler_chat.sendMessage(msg);
			break;
		case R.id.regbtn:

			break;
		default:
			break;
		}
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
