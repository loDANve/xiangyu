package com.lvgou.jj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.jj.R;
import com.lvgou.jj.been.POI;
import com.lvgou.jj.been.POIList;
import com.lvgou.jj.utils.Utils;

public class LBSActivity extends Activity {
	// [start] 百度地图相关变量声明及赋值
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyLocationListener();
	private String AK = "2I9N3rfdsSppiP1MPnmGzZwy";
	private boolean checkGPS = true;
	private String coorType = "bd09ll";// 返回的定位结果是百度经纬度,默认值gcj02
	private int scanSpan = 1;// 设置发起定位请求的间隔时间 小于1000为单次定位
	private boolean checkPoiExtraInfo = true; // 是否需要POI的电话和地址等详细信息
	private int poiNumber = 20; // 最多返回POI个数
	private String addrType = "all";// 返回的定位结果包含地址信息
	private int priority = LocationClientOption.GpsFirst;
	// [end]
	@ViewInject(R.id.lbsbtn)
	private Button lbsbtn;
	@ViewInject(R.id.poibtn)
	private Button poibtn;
	@ViewInject(R.id.mapbtn)
	private Button mapbtn;
	@ViewInject(R.id.lbstext)
	private TextView lbstext;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.lbs);
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.setAK(AK);
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		setLocationOption();
		mLocationClient.start();
		lbsbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mLocationClient != null && mLocationClient.isStarted()) {
					setLocationOption();
					mLocationClient.requestLocation();
				} else {
					LogUtils.d("locClient is null or not started");
					LogUtils.d("... mlocBtn onClick... pid=" + Process.myPid());
					LogUtils.d("version:" + mLocationClient.getVersion());
				}
			}
		});

		poibtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setLocationOption();
				mLocationClient.requestPoi();
			}
		});
		
		mapbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LBSActivity.this,MapActivity.class);
				startActivity(intent);
			}
		});
	}

	// 设置相关参数
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(checkGPS); // 打开gps
		option.setCoorType(coorType); // 设置坐标类型
		// option.setServiceName("com.baidu.location.service_v2.9");
		option.setPoiExtraInfo(checkPoiExtraInfo); // 是否需要POI的电话和地址等详细信息
		if (checkPoiExtraInfo) {
			option.setAddrType(addrType);// 返回的定位结果包含地址信息
		}
		option.setScanSpan(scanSpan);// 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
		option.setPriority(priority);// 设置网络优先,不设置，默认是gps优先

		option.setPoiNumber(poiNumber); // 设置最多返回POI个数
		option.disableCache(true);// 禁止启用缓存定位
		mLocationClient.setLocOption(option);
	}

	/**
	 * 显示请求字符串
	 * 
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			// String mData = str;
			if (lbstext != null)
				lbstext.setText(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 监听函数，有更新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				/**
				 * 格式化显示地址信息
				 */
				// sb.append("\n省：");
				// sb.append(location.getProvince());
				// sb.append("\n市：");
				// sb.append(location.getCity());
				// sb.append("\n区/县：");
				// sb.append(location.getDistrict());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			sb.append("\nsdk version : ");
			sb.append(mLocationClient.getVersion());
			sb.append("\nisCellChangeFlag : ");
			sb.append(location.isCellChangeFlag());
			logMsg(sb.toString());
			LogUtils.i(sb.toString());
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			POIList pl = new POIList();
//			List<POI> p = new ArrayList<POI>();
			pl = Utils.json2Object(poiLocation.getPoi().getBytes(), pl);
			
			StringBuffer sb = new StringBuffer(256);
			for(POI poi : pl.getP()){
				sb.append(poi.getName());
				sb.append(" : ");
				sb.append(poi.getAddr());
				sb.append("\n\n");
			}
			logMsg(sb.toString());
			// sb.append("Poi time : ");
			// sb.append(poiLocation.getTime());
			// sb.append("\nerror code : ");
			// sb.append(poiLocation.getLocType());
			// sb.append("\nlatitude : ");
			// sb.append(poiLocation.getLatitude());
			// sb.append("\nlontitude : ");
			// sb.append(poiLocation.getLongitude());
			// sb.append("\nradius : ");
			// sb.append(poiLocation.getRadius());
			// if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
			// sb.append("\naddr : ");
			// sb.append(poiLocation.getAddrStr());
			// LogUtils.e(poiLocation.getAddrStr());
			// }
			// if (poiLocation.hasPoi()) {
			// sb.append("\nPoi:");
			// sb.append(poiLocation.getPoi());
			// LogUtils.e(poiLocation.getPoi());
			// } else {
			// sb.append("noPoi information");
			// }
		}
	}

}
