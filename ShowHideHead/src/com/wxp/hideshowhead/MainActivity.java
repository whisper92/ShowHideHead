package com.wxp.hideshowhead;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView mLv = null;
	private List<String> mDataList = null;
	private ArrayAdapter<String> mAdapter = null;
	private RelativeLayout mHeadContainer = null;
	int mLastItem = 0;
	int mCurrentItem = 0;
	
	float mLastY = 0;
	boolean mHeadShow = true;
	VelocityTracker mVelocityTracker = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLv = (ListView) findViewById(R.id.id_lv);
		mHeadContainer = (RelativeLayout) findViewById(R.id.id_head_container);
		mDataList = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			mDataList.add("item : "+i);
		}
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mDataList);
	    mLv.setAdapter(mAdapter);
	    
	    mLv.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mVelocityTracker = VelocityTracker.obtain();
				mVelocityTracker.addMovement(event);
				mVelocityTracker.computeCurrentVelocity(1000);
				float yV = mVelocityTracker.getYVelocity();
				
				Log.e("wxphead", "y 轴加速度 : "+yV);
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					float y = event.getRawY();
					
					if (y - mLastY > 80) {
						onShow(); 
					} else if (y-mLastY < -80) {
						mLv.setPadding(0, 0, 0, 0);
						onHide();
					}
				}
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mLastY = event.getRawY();
				}

				return false;
			}
		});

	    
	    mLv.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//Log.e("wxphead", "firstVisibleItem : "+firstVisibleItem);
				if (firstVisibleItem == 0 && mHeadShow == false) {
					onShow();
				}
				
			}
		});
	
	}

	public void onShow() {
		mHeadShow = true;
		mHeadContainer.animate().translationY(0).setDuration(600).setInterpolator(new DecelerateInterpolator()).start();
	}
	
	public void onHide() {
		mHeadShow = false;
		mHeadContainer.animate().translationY(-mHeadContainer.getHeight()).setDuration(600).setInterpolator(new AccelerateDecelerateInterpolator()).start();
	}	

}
