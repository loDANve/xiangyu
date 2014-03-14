package com.lvgou.jj.adapter;

import java.util.List;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lvgou.jj.R;
import com.lvgou.jj.been.ChatMessage;

public class ChatMsgViewAdapter extends BaseAdapter {

	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	private List<ChatMessage> coll;

	private LayoutInflater mInflater;
	private MediaPlayer mMediaPlayer = new MediaPlayer();

	public ChatMsgViewAdapter(Context context, List<ChatMessage> coll) {
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		ChatMessage entity = coll.get(position);

		if (entity.isIssend()) {
			return IMsgViewType.IMVT_TO_MSG;
		} else {
			return IMsgViewType.IMVT_COM_MSG;
		}

	}

	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		final ChatMessage entity = coll.get(position);
		boolean isComMsg = entity.isIssend();

		ViewHolder viewHolder = null;
		if (convertView == null) {
			if (isComMsg) {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_right, null);
			} else {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_left, null);
			}

			viewHolder = new ViewHolder();
			viewHolder.tvSendTime = (TextView) convertView
					.findViewById(R.id.tv_sendtime);
			viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tv_username);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_chatcontent);
			viewHolder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.isComMsg = isComMsg;

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvSendTime.setText(entity.getDate());

		if (entity.getBody() != null) {
			if (entity.getBody().contains(".amr")) {
				// viewHolder.tvContent.setText("");
				// viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0,
				// 0, R.drawable.chatto_voice_playing, 0);
				// viewHolder.tvTime.setText(entity.getTime());
			} else {
				viewHolder.tvContent.setText(entity.getBody());
				viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0,
						0, 0, 0);
				viewHolder.tvTime.setText("");
			}
		}

//		viewHolder.tvContent.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				if (entity.getText().contains(".amr")) {
//					playMusic(android.os.Environment
//							.getExternalStorageDirectory()
//							+ "/"
//							+ entity.getText());
//				}
//			}
//		});
		viewHolder.tvUserName.setText(entity.getFrom());

		return convertView;
	}

	static class ViewHolder {
		public TextView tvSendTime;
		public TextView tvUserName;
		public TextView tvContent;
		public TextView tvTime;
		public boolean isComMsg = true;
	}

	/**
	 * @Description
	 * @param name
	 */
	private void playMusic(String name) {
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(name);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
