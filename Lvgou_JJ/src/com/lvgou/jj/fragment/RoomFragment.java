package com.lvgou.jj.fragment;

import java.util.List;

import org.jivesoftware.smackx.packet.DiscoverItems;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.jj.R;
import com.lvgou.jj.activity.ChatActivity;
import com.lvgou.jj.adapter.RoomAdapter;
import com.lvgou.jj.utils.Utils;
import com.lvgou.jj.xmpp.XMPPHelper;

public class RoomFragment extends Fragment {
	@ViewInject(R.id.room_list)
	private ListView room_list;
	private static RoomFragment instance;
	private List<DiscoverItems.Item> list;
	private RoomAdapter adapter;
	private DiscoverItems.Item item;

	public static RoomFragment getInstance() {
		if (instance == null) {
			instance = new RoomFragment();
		}
		return instance;
	}

	public RoomFragment() {
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.room, container, false);
		ViewUtils.inject(this, v);
		XMPPHelper.getInstance().getRooms(handler);
		room_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				item = list.get(pos);
				Intent intent = new Intent(getActivity(), ChatActivity.class);
				intent.putExtra("jid", item.getEntityID());
				intent.putExtra("title", item.getName());
				getActivity().startActivity(intent);
			}
		});
		return v;
	}

	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// rooms = (List<HostedRoom>) msg.obj;
			list = (List<DiscoverItems.Item>) msg.obj;
			adapter = new RoomAdapter(list, getActivity());
			room_list.setAdapter(adapter);
			super.handleMessage(msg);
		}

	};

}
