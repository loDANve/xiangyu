package com.lvgou.jj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lvgou.jj.R;
import com.lvgou.jj.client.ServiceManager;
import com.lvgou.jj.fragment.RoomFragment;

public class MainActivity extends FragmentActivity {
	private Fragment contentFragment;
	private float density;
	private int widthPixels;
	private int heightPixels;
	private ServiceManager serviceManager;
	SlidingMenu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {// == null的时候新建Fragment1
			contentFragment = new RoomFragment();
		} else {// 不等于null，直接get出来
			// 不等于null，找出之前保存的当前Activity显示的Fragment
			contentFragment = getSupportFragmentManager().getFragment(
					savedInstanceState, "contentFragment");
		}
		setContentView(R.layout.main);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_content, contentFragment).commit();
		ViewUtils.inject(this);
		init();
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// menu.setShadowDrawable(R.drawable.shadow);
		// menu.setShadowWidthRes(R.dimen.shadow_width);
		// menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setBehindWidth(widthPixels*8/10);// 设置SlidingMenu菜单的宽度
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//		serviceManager = new ServiceManager(this);
//		serviceManager.startService();
		LogUtils.i("MainActivity  onCreate ");
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		if(null != intent){
			LogUtils.e(intent.getAction());
		}
	}

	private void init() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		widthPixels = dm.widthPixels;
		heightPixels = dm.heightPixels;
		density = dm.density;
	}

	public void switchContent(Fragment fragment) {
		contentFragment = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_content, fragment).commit();
		menu.showContent();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "contentFragment",
				contentFragment);
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
