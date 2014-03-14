package com.lvgou.jj.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lvgou.jj.R;

public class LatelyFragment extends Fragment {
	private static LatelyFragment instance;

	public static LatelyFragment getInstance() {
		if (instance == null) {
			instance = new LatelyFragment();
		}
		return instance;
	}
	
	public LatelyFragment() {
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main_frame, container, false);
		ViewUtils.inject(this, v);
		return v;
	}

}
