package com.lvgou.jj.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.jj.R;
import com.lvgou.jj.activity.MainActivity;

public class MainFragment extends Fragment implements OnClickListener {
	private Fragment fragment;
	private static MainFragment instance;

	public static MainFragment getInstance() {
		if (instance == null) {
			instance = new MainFragment();
		}
		return instance;
	}

	public MainFragment() {
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@ViewInject(R.id.lately)
	private TextView lately;
	@ViewInject(R.id.friend)
	private TextView friend;
	@ViewInject(R.id.group)
	private TextView group;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main_frame, container, false);
		ViewUtils.inject(this, v);
		lately.setOnClickListener(this);
		friend.setOnClickListener(this);
		group.setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lately:
			setCurPoint(0);
			fragment = LatelyFragment.getInstance();
			switchFragment(fragment);
			break;
		case R.id.friend:
			setCurPoint(1);
			fragment = FriendFragment.getInstance();
			switchFragment(fragment);
			break;
		case R.id.group:
			setCurPoint(2);
			fragment = RoomFragment.getInstance();
			switchFragment(fragment);
			break;
		default:
			break;
		}

	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MainActivity) {
			MainActivity main = (MainActivity) getActivity();
			main.switchContent(fragment);
		}
	}

	private void setCurPoint(int index) {
		if (index == 0) {
			lately.setTextColor(0xff228B22);
			friend.setTextColor(Color.BLACK);
			group.setTextColor(Color.BLACK);
		} else if (index == 1) {
			lately.setTextColor(Color.BLACK);
			friend.setTextColor(0xff228B22);
			group.setTextColor(Color.BLACK);
		} else {
			lately.setTextColor(Color.BLACK);
			friend.setTextColor(Color.BLACK);
			group.setTextColor(0xff228B22);
		}
	}

}
