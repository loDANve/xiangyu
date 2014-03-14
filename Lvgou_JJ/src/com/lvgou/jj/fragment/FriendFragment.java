package com.lvgou.jj.fragment;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.jj.R;
import com.lvgou.jj.adapter.FriendAdapter;
import com.lvgou.jj.been.Group;
import com.lvgou.jj.been.User;
import com.lvgou.jj.xmpp.XMPPHelper;

public class FriendFragment extends Fragment {
	@ViewInject(R.id.friend_all)
	private ExpandableListView friend_all;
	private List<RosterGroup> grouplist = new ArrayList<RosterGroup>();
	private FriendAdapter adapter;
	private static FriendFragment instance;

	public FriendFragment() {
		setRetainInstance(true);
	}

	public static FriendFragment getInstance() {
		if (instance == null) {
			instance = new FriendFragment();
		}
		return instance;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.friend, container, false);
		ViewUtils.inject(this, v);
		// friend_all.
		XMPPHelper.getInstance().getGroups(handler);
		return v;
	}

	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			System.out.println("得到好友列表信息");
			grouplist = (List<RosterGroup>) msg.obj; // 获取到所有组信息
			List<Group> groups = new ArrayList<Group>();
			for (int i = 0; i < grouplist.size(); i++) {
				Group group = new Group();
				List<User> users = new ArrayList<User>();
				group.setGroupName(grouplist.get(i).getName());

				for (RosterEntry rosterEnter : grouplist.get(i).getEntries()) {
					// if (rosterEnter.getType().name().equals("both")) { //
					// 双向好友
					// users..add(rosterEnter.getUser());
					User user = new User();
					user.setUser(rosterEnter.getUser());
					user.setName(rosterEnter.getName());
					System.out.println("User------------->"
							+ rosterEnter.getUser());
					System.out.println("Name------------->"
							+ rosterEnter.getName());
					System.out.println(rosterEnter.toString());
					users.add(user);
					// }
				}
				group.setUsers(users);
				groups.add(group);
			}
			adapter = new FriendAdapter(getActivity(), groups);
			friend_all.setAdapter(adapter);
			// adapter = new RefreshExpandableListViewAdapter(getActivity(),
			// groups);
			// refreshExpandableListView.setAdapter(adapter);
		};
	};

}
