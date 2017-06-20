package com.tpyzq.mobile.pangu.util.imageUtil;

import android.graphics.Bitmap;
import android.util.Log;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;

public class BitmapCache {

	private final String TAG	= "BitmapCache";
	public static final long MAX_CACHE_SIZE	= Runtime.getRuntime().maxMemory() >> 2;

	private static BitmapCache self;

	private Hashtable<String, BitmapRef> mRecycledRefs;
	private ReferenceQueue<Bitmap> mRecycledQueue;
	private BimapCacheMap mCache;

	// The ref class
	private class BitmapRef extends SoftReference<Bitmap> {
		private String mKey = "";

		public BitmapRef(String path, Bitmap bmp, ReferenceQueue<Bitmap> q) {
			super(bmp, q);
			mKey = path;
		}
	}

	//仅允许最多8M的图片缓存
	final private class BimapCacheMap extends LinkedHashMap<String, Bitmap> {
		private long mCurrentSize;
		/**
		 * generate a serialversion UID
		 */
		private static final long serialVersionUID = 877587823304246314L;

		BimapCacheMap() {
			super(5, 0.75f, true);
		}

		protected final boolean removeEldestEntry(
				Entry<String, Bitmap> paramEntry) {
			if (mCurrentSize <= MAX_CACHE_SIZE || paramEntry == null) {
				Log.i(TAG, "Cache file size:" + size());
				return false;
			}

			Bitmap bmp = paramEntry.getValue();
			if (bmp == null)
				return true;

			mCurrentSize -= ImageUtil.getBitmapSize(bmp);
			return true;
		}

		@Override
		public Bitmap put(String key, Bitmap value) {
			if (key == null || value == null)
				return value;

			mCurrentSize += ImageUtil.getBitmapSize(value);
			return super.put(key, value);
		}

		@Override
		public void clear() {
			for (Entry<String, Bitmap> item : entrySet()) {
				String key = item.getKey();
				Bitmap bmp = item.getValue();
				if (bmp == null)
					continue;

				BitmapRef ref = new BitmapRef(key, bmp, mRecycledQueue);
				mRecycledRefs.put(key, ref);
			}
			mCurrentSize = 0;
			super.clear();
		}

		public void prepareSpace(long nSize) {
			long needSize = mCurrentSize + nSize;
			if (needSize <= MAX_CACHE_SIZE)
				return;

			ArrayList<String> keys = new ArrayList<String>();
			for (Entry<String, Bitmap> item : mCache.entrySet()) {
				long itemSize = ImageUtil.getBitmapSize(item.getValue());
				mCurrentSize -= itemSize;
				needSize -= itemSize;
				keys.add(item.getKey());
				if (needSize <= MAX_CACHE_SIZE)
					break;
			}

			for (String k : keys) {
				mCache.remove(k);
			}

			System.gc();
			System.runFinalization();
		}
	}

	private BitmapCache() {
		mCache = new BimapCacheMap();
		mRecycledRefs = new Hashtable<String, BitmapRef>();
		mRecycledQueue = new ReferenceQueue<Bitmap>();
	}

	public static synchronized BitmapCache getInstance() {
		if (self == null) {
			self = new BitmapCache();
		}

		return self;
	}

	// 以软引用的方式对一个Employee对象的实例进行引用并保存该引用
	public void cacheBitmap(String path, Bitmap bmp) {
		if (path == null || bmp == null)
			return;

		cleanCache();// 清除垃圾引用
		BitmapRef ref = new BitmapRef(path, bmp, mRecycledQueue);
		mRecycledRefs.put(path, ref);

		synchronized(mCache) {
			mCache.put(path, bmp);
		}
	}

	// 依据所指定的ID号，重新获取相应Employee对象的实例
	public Bitmap getBitmap(String key, boolean isDecode) {
		if (key == null)
			return null;

		Bitmap bmp = null;
		// 缓存中是否有该Employee实例的软引用，如果有，从软引用中取得。
		synchronized (mCache) {
			if (mCache.containsKey(key)) {
				bmp = mCache.get(key);
				if (bmp != null) {
					return bmp;
				}
				mCache.remove(key);
			}
		}

		if (bmp == null && isDecode) {
			bmp = ImageUtil.decodeBitmap(key);
		}

		return bmp;
	}

	public void prepareCacheSpace(long nSize) {
		synchronized(mCache) {
			mCache.prepareSpace(nSize);
		}
	}

	public void deleteCache(String key) {
		synchronized(mCache) {
			mCache.remove(key);
		}
	}

	private void cleanCache() {
		BitmapRef ref = null;
		while ((ref = (BitmapRef) mRecycledQueue.poll()) != null) {
			Log.i(TAG, "Remove " + ref.mKey);
			mRecycledRefs.remove(ref.mKey);
		}
	}

	// 清除Cache内的全部内容
	public void clearCache() {
		synchronized (mCache) {
			mCache.clear();
			mCache.mCurrentSize = 0;
		}
		cleanCache();
		mRecycledRefs.clear();
		System.gc();
		System.runFinalization();
	}
}
