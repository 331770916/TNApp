package com.tpyzq.mobile.pangu.util.imageUtil;

import android.content.Context;
import android.util.DisplayMetrics;

import com.tpyzq.mobile.pangu.base.CustomApplication;


public class BitmapAstrict {

	/**
	 * 程序中图片允许的最大宽高
	 */
	public final static int BITMAP_MAX_SIDELEN;
	/**
	 * 程序中一个位图元允许的最大内存空间
	 */
	public final static long BITMAP_MAX_SIZE;
	/**
	 * 图片存储时允许的最大文件大小
	 */
	public final static int FILE_MAX_SIZE 		= 1024000;
	/**
	 * 程序中允许的缩略图正方形的边长
	 */
	public final static int THUMBNAIL_SIDELEN;

	private final static int THUMBNAIL_SIZE_WVGA = 80;
	private final static int THUMBNAIL_SIZE_HVGA = 54;
	private final static int THUMBNAIL_SIZE_QVGA = 40;

	static {
		Context c = CustomApplication.getContext();
		if (c == null) {
			BITMAP_MAX_SIDELEN	= 800;
			BITMAP_MAX_SIZE	= 768000; //480X800 RGB565
			THUMBNAIL_SIDELEN = THUMBNAIL_SIZE_WVGA;
		} else {
			DisplayMetrics dm = c.getResources().getDisplayMetrics();
			BITMAP_MAX_SIDELEN	= Math.max(Math.max(dm.widthPixels, dm.heightPixels), 800);
			int nRGB565Size = (BITMAP_MAX_SIDELEN * BITMAP_MAX_SIDELEN) << 1;
			BITMAP_MAX_SIZE	= Math.min(Math.max(768000, nRGB565Size), BitmapCache.MAX_CACHE_SIZE>>3);

			if (dm.heightPixels <= 240 || dm.widthPixels <= 240) {
				THUMBNAIL_SIDELEN = THUMBNAIL_SIZE_QVGA;
			} else if (dm.heightPixels <= 320 || dm.widthPixels <= 320) {
				THUMBNAIL_SIDELEN = THUMBNAIL_SIZE_HVGA;
			} else {
				THUMBNAIL_SIDELEN = THUMBNAIL_SIZE_WVGA;
			}
		}

	}
}
