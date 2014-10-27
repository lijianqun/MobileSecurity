package com.npp.MobileSecurity.adapter;

import java.util.ArrayList;
import java.util.List;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * ViewPager閫傞厤鍣�
 * 
 */
public class ViewPagerAdapter extends PagerAdapter {
	private List<View> list;

	public ViewPagerAdapter(List<View> list) {
		if (list != null) {
			this.list = list;
		} else {
			this.list = new ArrayList<View>();
		}
	}

	// 閿�姣乸osition浣嶇疆鐨勭晫闈�
	@Override
	public void destroyItem(View view, int position, Object obj) {
		((ViewPager) view).removeView(list.get(position));
	}

	// 鑾峰彇褰撳墠绐椾綋鐣岄潰鏁�
	@Override
	public int getCount() {
		return list.size();
	}

	// 鍒濆鍖杙osition浣嶇疆鐨勭晫闈�
	@Override
	public Object instantiateItem(View view, int position) {
		((ViewPager) view).addView(list.get(position));
		return list.get(position);
	}

	// 鍒ゆ柇View鍜屽璞℃槸鍚︿负鍚屼竴涓猇iew
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}


	@Override
	public void startUpdate(View arg0) {

	}

	@Override
	public void finishUpdate(View arg0) {

	}

}
