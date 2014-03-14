package com.lvgou.jj.adapter;

import java.util.List;

import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.packet.DiscoverItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lvgou.jj.R;

public class RoomAdapter extends BaseAdapter {

	private Context context;
//	private List<HostedRoom> rooms;
	private List<DiscoverItems.Item> rooms;

	public RoomAdapter(List<DiscoverItems.Item> rooms, Context context) {
		this.rooms = rooms;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return rooms.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.room_item,
					parent, false);
			holder = new Holder();
			holder.roomname = (TextView) convertView
					.findViewById(R.id.roomname);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.roomname.setText(rooms.get(position).getName());
		return convertView;
	}

	class Holder {
		TextView roomname;
	}
}
