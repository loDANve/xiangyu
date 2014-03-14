package com.lvgou.jj.adapter;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.lvgou.jj.R;
import com.lvgou.jj.been.Group;

public class FriendAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<Group> group;

	public FriendAdapter(Context context, List<Group> group) {
		super();
		this.context = context;
		this.group = group;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.friend_group, null);
			// aq_group = new AQuery(convertView);
		}
		// aq_group.id(R.id.grouptext).text(group[groupPosition]);

		TextView text = (TextView) convertView.findViewById(R.id.grouptext);
		text.setText(group.get(groupPosition).getGroupName());
		return convertView;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public int getGroupCount() {
		return group.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return group.get(groupPosition);
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		return 0;
	}

	@Override
	public long getCombinedChildId(long groupId, long childId) {
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return group.get(groupPosition).getUsers().size();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.friend_child_item, null);
			// aq_child = new AQuery(convertView);headimg
		}

		// aq_child.id(R.id.orderinfo).text(child[groupPosition][childPosition]);
		TextView text = (TextView) convertView.findViewById(R.id.friendname);
		text.setText(group.get(groupPosition).getUsers().get(childPosition)
				.getName());
		return convertView;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return group.get(groupPosition).getUsers().get(childPosition);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

}
