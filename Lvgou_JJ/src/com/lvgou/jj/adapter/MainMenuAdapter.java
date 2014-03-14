package com.lvgou.jj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lvgou.jj.R;

public class MainMenuAdapter extends BaseAdapter {

	String[] menus;
	Context context;

	public MainMenuAdapter(String[] menus, Context context) {
		this.menus = menus;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return menus.length;
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.drawer_list_item, parent, false);
			holder = new Holder();
			holder.mainmenutext = (TextView) convertView
					.findViewById(R.id.mainmenutext);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.mainmenutext.setText(menus[position]);
		return convertView;
	}

	class Holder {
		TextView mainmenutext;
	}
}
