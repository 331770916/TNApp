package com.tpyzq.mobile.pangu.view.spinner;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.ArrayList;

public class CustomSpinner implements OnItemClickListener {
	private View                 mView;
	private PopupWindow          mPopupWindow = null;
	private ArrayList<String> mDatas;
	private CustomSpinnerAdapter mAdapter;
	private ChooseItemListener   mListener;
	private Context              mContext;
//	private TextView             mShowView;
	private int                  mDefaultWidth  = 200;
	private String               mTitle;
	public static boolean ISDROP = true;
	private boolean mHasPic;
	private ArrayList<Integer> imgIds = null;
//	private int dip2px(float dipValue) {
//		final float scale = mContext.getDisplayMetrics().density;
//		return (int) (dipValue * scale + 0.5f);
//	}
	public CustomSpinner(Context context, ArrayList<String> list, boolean hasPic, ChooseItemListener listener) {
		mDatas     = list;
		mListener  = listener;
		mContext   = context;
		mHasPic= hasPic;
		initData();
	}
	public void setImgIds(ArrayList<Integer> ids){
		mHasPic = true;
		imgIds = ids;
		mAdapter.setHasPic(mHasPic);
	}
	private void initData() {
		mView = LayoutInflater.from(mContext).inflate(R.layout.popwindow, null);
		measureHeadView(mView);
		ListView listView = (ListView) mView.findViewById(R.id.poplist);
		mAdapter = new CustomSpinnerAdapter(mContext, mDatas);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
	}

	public void setDefaultWidth(int width) {
		mDefaultWidth = width;
	}

	/**
	 * show the popwindow
	 * @param viewGroup  It is one necessary param, don't set null
	 */
	public void show(View viewGroup) {
		TextView mTitleView = (TextView) mView.findViewById(R.id.title);
		if (mTitle == null) {
			mTitleView.setVisibility(View.GONE);
		} else {
			mTitleView.setVisibility(View.VISIBLE);
			mTitleView.setText(mTitle);
		}
		
//		mShowView = textView;
		if (mPopupWindow  == null) {
			initPopwindow();
		}

		if (ISDROP) {
			mPopupWindow.showAsDropDown(viewGroup); //在控件下方
		} else {
			//在控件上方
			showPopUp(viewGroup);
		}
	}
	public void showAtlocal2(View viewGroup,int pos) {
		TextView mTitleView = (TextView) mView.findViewById(R.id.title);
		mTitleView.setVisibility(View.GONE);
		if (mPopupWindow  == null) {
			initPopwindow();
		}
		mPopupWindow.showAtLocation(viewGroup,Gravity.RIGHT|Gravity.TOP,-100,pos); //
	}
	public void showAtlocal(View viewGroup) {
		TextView mTitleView = (TextView) mView.findViewById(R.id.title);
		if (mTitle == null) {
			mTitleView.setVisibility(View.GONE);
		} else {
			mTitleView.setVisibility(View.VISIBLE);
			mTitleView.setText(mTitle);
		}

		if (mPopupWindow  == null) {
			initPopwindow();
		}
		mPopupWindow.showAtLocation(viewGroup,Gravity.RIGHT,0,-10); //在控件下方
	}
	private void showPopUp(View v) {
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		int top = location[1];
		int height = mPopupWindow.getContentView().getMeasuredHeight();
		mPopupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], top - v.getHeight()/2 - mDatas.size() * height);
	}

	private void initPopwindow() {
		int width = Helper.dip2px(mContext, mDefaultWidth);
		mPopupWindow = new PopupWindow(mView, width, LayoutParams.WRAP_CONTENT);

		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
	}

	/**
	 * 通知父布局占用多大
	 * @param view
	 */
	private void measureHeadView(View view) {
		LayoutParams params = view.getLayoutParams();
		if (params == null) {
			params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		}

		int width = ViewGroup.getChildMeasureSpec(0, 0, params.width);
		int height;

		int tempHeight = params.height;

		if (tempHeight > 0) {
			height = View.MeasureSpec.makeMeasureSpec(tempHeight, View.MeasureSpec.EXACTLY);
		} else {
			height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		}

		view.measure(width, height);
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public interface ChooseItemListener {
		void onChooseItem(int position, String view);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mListener.onChooseItem(position, mDatas.get(position).toString());

		if(mPopupWindow != null){
			mPopupWindow.setFocusable(false);
			mPopupWindow.dismiss();
		}
	}
	
    class CustomSpinnerAdapter extends BaseAdapter {
		private Context mContext;
		private ArrayList<String> mlist;
		private boolean hasPic;

		public CustomSpinnerAdapter(Context context, ArrayList<String> list) {
			mContext = context;
			mlist    = list;
		}
		public void setHasPic(boolean flag){
			this.hasPic = flag;
		}
		@Override
		public int getCount() {
			if(mlist != null && mlist.size() > 0){
				return mlist.size();
			}
			
			return 1;
		}

		@Override
		public Object getItem(int position) {
			if(mlist != null && mlist.size() > 0){
				return mlist.get(position);
			}
			
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.popwindow_item, null);
			}
			updateItemUI(position, convertView);
			
			return convertView;
		}

		private void updateItemUI(int position, View convertView) {
			TextView textView = (TextView) convertView.findViewById(R.id.tv);
			ImageView imageView = (ImageView) convertView.findViewById(R.id.img);
			textView.setText(mlist.get(position));
			if (hasPic) {
				imageView.setVisibility(View.VISIBLE);
				imageView.setImageResource(imgIds.get(position));
			}else{
				imageView.setVisibility(View.GONE);
			}
		}
	}

}
