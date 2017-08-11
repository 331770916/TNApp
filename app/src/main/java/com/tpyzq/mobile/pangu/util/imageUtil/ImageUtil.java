package com.tpyzq.mobile.pangu.util.imageUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.CentreToast;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {

	/**
	 * To get the memory size of the bimap.
	 * @param bmp the bitmap to check memory size
	 * @return the size in BYTE
	 */
	public static long getBitmapSize(Bitmap bmp) {
		if (bmp == null)
			return 0;

		return getBitmapSize(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
	}

	/**
	 * To get the memory size of the bimap by params.
	 * @param nWidth the bitmap width.
	 * @param nHeight the bitmap height.
	 * @param config the config of the bitmap
	 * @return the size in BYTE
	 */
	public static long getBitmapSize(int nWidth, int nHeight, Bitmap.Config config) {
		long nSize = nWidth * nHeight;
		if (config == null) {
			return nSize *= 2;
		}

		switch (config) {
			case RGB_565:
				nSize *= 2;
				break;
			case ARGB_8888:
				nSize *= 4;
				break;
			case ALPHA_8:
				break;
			default:
				break;
		}

		return nSize;
	}

	/**
	 *  Resize the bitmap with ratio of equality.
	 *
	 *  @param bitmap The source bitmap.
	 *  @param resizeSize The max width size you want to resize to.
	 *
	 *  @return resizeBitmap The new bitmap which have resize size.
	 */
	public static Bitmap resizeBitmap(Bitmap bitmap, float resizeSize) {
		if (bitmap == null)
			return null;

		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();
		if (resizeSize >= bitmapWidth && resizeSize >= bitmapHeight)
			return bitmap;

		float scale;
		long nBitmapSize = 0;
		if (bitmapWidth > bitmapHeight) {
			scale = resizeSize / bitmapWidth;
			nBitmapSize = (long)(resizeSize * scale * bitmapHeight * 2);
		} else {
			scale = resizeSize / bitmapHeight;
			nBitmapSize = (long)(resizeSize * scale * bitmapWidth * 2);
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		BitmapCache.getInstance().prepareCacheSpace(nBitmapSize);
		Bitmap ret = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
		return ret;
	}

	public static Bitmap decodeBitmap(String filePath) {
		return decodeBitmap(filePath, BitmapAstrict.BITMAP_MAX_SIZE);
	}

	/**
	 *  Get a subsize bitmap form specified file path.
	 *
	 *  @param filePath file path.
	 *  @param maxSize the bitmap max size allowed. 
	 *
	 *  @return resizeBitmap The new bitmap which have resize size.
	 */
	public static Bitmap decodeBitmap(String filePath, double maxSize) {
		if (filePath == null)
			return null;

		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return null;
		}

		Bitmap bmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 1;
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);
		int sourceSize = opts.outWidth * opts.outHeight * 2;
		if (maxSize >= 0 && sourceSize > maxSize) {
			opts.inSampleSize = (int)Math.round(Math.sqrt(sourceSize / maxSize));
			if (opts.inSampleSize > 1) {
				//取小于该奇数的偶数，提升性能
				opts.inSampleSize >>= 1;
				opts.inSampleSize <<= 1;
				BitmapFactory.decodeFile(filePath, opts);
				sourceSize = opts.outWidth * opts.outHeight * 2;
			}
		}
		BitmapCache.getInstance().prepareCacheSpace(sourceSize);

		try {
			opts.inJustDecodeBounds = false;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			bmp = BitmapFactory.decodeFile(filePath, opts);
			bmp = resizeBitmap(bmp, BitmapAstrict.BITMAP_MAX_SIDELEN);
			BitmapCache.getInstance().cacheBitmap(filePath, bmp);
		} catch (OutOfMemoryError e) {
			BitmapCache.getInstance().clearCache();
			return decodeBitmap(filePath, maxSize);
		}
		return bmp;
	}

	public static boolean saveBitmap(Bitmap bitmap, String path, int pictureTotal) {
		File file = Helper.getExternalFileDir(CustomApplication.getContext(), "ImageView");
		File [] files = file.listFiles();

		if (files != null && files.length> 0) {

			for (int i =0; i< files.length; i++) {
				if (files.length > pictureTotal && i < pictureTotal) {
					files[i].delete();
				}
			}

		}


		return saveBitmap(bitmap, path);
	}

	/**
	 * To save the given bitmap into the given absolute file path.
	 * @param bitmap the bitmap to save.
	 * @param path the absolute file path where to store the given bitmap.
	 * @return
	 */
	public static boolean saveBitmap(Bitmap bitmap, String path) {
		boolean bRet = false;
		if (bitmap == null || path == null)
			return bRet;

		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}

		int nMaxSize = BitmapAstrict.FILE_MAX_SIZE;
		FileOutputStream outputStream = null;
		ByteArrayOutputStream bos = null;
		try {
			int quality = 100;
			bos = new ByteArrayOutputStream();
			do {
				bos.reset();
				bRet = bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
			} while (bRet && bos.size() > nMaxSize && quality > 0);

			//Don't save it if can't be compressed!
			if (bRet && bos.size() <= nMaxSize) {
				outputStream = new FileOutputStream(path);
				bos.writeTo(outputStream);
				BitmapCache.getInstance().cacheBitmap(path, bitmap);
			} else {
				CentreToast.showText(CustomApplication.getContext(), "亲~ 你有图片保存失败，有空换一张吧!");

			}
		} catch (IOException e) {
			Log.e("ImageUtil", e.toString());
		} catch (OutOfMemoryError e) {
			Log.e("ImageUtil", e.toString());
			BitmapCache.getInstance().clearCache();
			return saveBitmap(bitmap, path);
		} finally {
			if (bos != null) {
				try { bos.close(); } catch (IOException e) {}
			}
			if (outputStream != null) {
				try {outputStream.close(); } catch (IOException e) { e.printStackTrace();}
			}
		}

		return bRet;
	}
	/**
	 * @Title: zoomImageTo
	 * @说       明: 将图片缩放到指定大小
	 * @throws
	 */
	public static Bitmap zoomImageTo(Bitmap bitmap, float newWidth, float newHeight) {
		return zoomImage(bitmap, newWidth / bitmap.getWidth(), newHeight / bitmap.getHeight());
	}

	/**
	 * @Title: zoomImage
	 * @说       明: 图片缩放到指定倍数
	 * @throws
	 */
	public static Bitmap zoomImage(Bitmap bitmap, float scaleWidth, float scaleHeight) {
		if(null == bitmap) {
			return bitmap;
		}

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}

	/**
	 *  Resize the bitmap with ratio of equality, and cut out a square thumbnail to return.
	 *
	 *  @param path the path of the thumbnail, must be unique to make it in the cache.
	 *  @param bitmap The source bitmap.
	 *  @return thumbnail a square thumbnail which have resize size.
	 */
	public static Bitmap getFoursquareThumbnail(String path, Bitmap bitmap) {
		if (bitmap == null)
			return null;

		float sideLength = BitmapAstrict.THUMBNAIL_SIDELEN;
		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();
		if (bitmapWidth <= sideLength &&
				bitmapHeight <= sideLength &&
				Math.abs(bitmapWidth - bitmapHeight) < 5)
			return bitmap;

		int startWidth = 0, endWidth = bitmapWidth, startHeight = 0, endHeight = bitmapHeight;
		float scale;
		if (bitmapWidth > bitmapHeight) {
			scale = sideLength / bitmapHeight;
			startWidth = (bitmapWidth - bitmapHeight) / 2;
			endWidth = bitmapHeight;
		} else {
			scale = sideLength / bitmapWidth;
			startHeight = (bitmapHeight - bitmapWidth) / 2;
			endHeight = bitmapWidth;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap ret = Bitmap.createBitmap(bitmap, startWidth, startHeight, endWidth, endHeight, matrix, false);
		BitmapCache.getInstance().cacheBitmap(path, ret);
		return ret;
	}

	/**
	 * 把图片按照旋转角度
	 * @param path 图片的路径，主要目的是用来定位缓存图片，一免缓存中的图片得不到更新
	 * @param bitmap 需要旋转的原位图
	 * @param rotate 需要旋转的角度
	 * @return 旋转后的位图
	 */
	public static Bitmap rotateBitmap(String path, Bitmap bitmap, int rotate) {
		if (path == null || bitmap == null || rotate == 0)
			return bitmap;

		Matrix matrix = new Matrix();
		matrix.setRotate(rotate);
		BitmapCache.getInstance().prepareCacheSpace(bitmap.getWidth() * bitmap.getHeight() * 2);
		Bitmap rotateBitmap = Bitmap.createBitmap(
				bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		BitmapCache.getInstance().cacheBitmap(path, rotateBitmap);
		return rotateBitmap;
	}


	/**
	 *  Compute the size of specified folder.
	 *
	 *  @param dir The specified folder file.
	 *
	 *  @return size the size of specified folder.
	 */
	public static long getFileSize(File dir) {
		if (dir == null) {
			return 0;
		}

		File flist[] = dir.listFiles();
		if (null == flist) {
			return 0;
		}

		long size = 0;
		final int fCount = flist.length;
		for (int i = 0; i < fCount; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}


	/**
	 * base64转为bitmap
	 * @param base64Data
	 * @return
	 */
	public static Bitmap base64ToBitmap(String base64Data) {

		Bitmap bitmap = null;
		try {
			byte[] bytes = Base64.decodeBase64(base64Data.getBytes());
			bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return  bitmap;
	}

	/**
	 * Byte[] to Bitmap
	 * @param imageStr：Byte[] data
	 * @param path：save Bitmap path
	 * @throws Exception
	 */
	public static void StringToImage(String imageStr, String path) {
		if (TextUtils.isEmpty(imageStr)) {
			return;
		}

		byte[] b = Base64.decodeBase64(new String(imageStr).getBytes());
		File apple = new File(path);
		if (apple.exists()) {
			apple.delete();
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(apple);
			fos.write(b);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getBitmapData(String path) {
		String uploadString = null;
		try {
			File file = new File(path);
			FileInputStream fis = new FileInputStream(file);
			byte[] b = new byte[fis.available()];
			fis.read(b);
			uploadString = new String(Base64.encodeBase64(b)); // Base64 encoder
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
			return uploadString;
		}

		return uploadString;
	}

	/**
	 * 当size= 1000时图片压缩后小于100KB，失真度不明显
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Bitmap revitionBitmapSize(String path, int size) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= size)&& (options.outHeight >> i <= size)) {
				in = new BufferedInputStream(new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}

			i += 1;
		}

		return bitmap;
	}

	public static void saveBitmapForAlbum(Bitmap bm, String path) {
		if (bm == null || TextUtils.isEmpty(path)) {
			return;
		}

		try {
			File f = new File(path);
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 95, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			File file = new File(path);
			if (!file.exists()) {
				return -1 ;
			}

			ExifInterface exifInterface =  new ExifInterface(file.getPath());
			int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);
			int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);

			Log.i("", "width:" + width + "height:" + height);

			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
//    	switch (orientation) {
//    	case ExifInterface.ORIENTATION_ROTATE_90:
//    	degree = 90;
//    	break;
//    	case ExifInterface.ORIENTATION_ROTATE_180:
//    	degree = 180;
//    	break;
//    	case ExifInterface.ORIENTATION_ROTATE_270:
//    	degree = 270;
//    	break;
//    	}

			if (height > width) {
				degree = 1;//手机竖向拍
			} else {
				degree = 2; //手机横向拍
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public static Bitmap rotate(Bitmap b, int degrees) {
		if(degrees==0){
			return b;
		}
		if (degrees != 0 && b != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) b.getWidth() ,
					(float) b.getHeight() );
			try {
				Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
						b.getHeight(), m, true);
				if (b != b2) {
					b.recycle(); // Android开发网再次提示Bitmap操作完应该显示的释放
					b = b2;
				}
			} catch (OutOfMemoryError ex) {
				// Android123建议大家如何出现了内存不足异常，最好return 原始的bitmap对象。.
				ex.printStackTrace();
			}
		}
		return b;
	}

	/**
	 *  处理图片
	 * @param bm 所要转换的bitmap
	 * @return 指定宽高的bitmap
	 */
	public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		return newbm;
	}
}
