package com.tpyzq.mobile.pangu.base;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

public class ActivityCollection extends Application {
	private List<Activity> activityList = new LinkedList<Activity>();
	private static ActivityCollection instance;

	private ActivityCollection() {
	}

	public static ActivityCollection getInstance() {
		if (null == instance) {
			instance = new ActivityCollection();
		}
		return instance;

	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}


	public void finish() {
		for (Activity activity : activityList) {
			activity.finish();
		}
	}
}
