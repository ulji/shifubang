package com.photo.choosephotos.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

/**
 * ͼƬ����Ĺ�����
 * 
 * @author Lih
 * 
 */
public class PictureManageUtil {

	/**
	 * ����ͼƬ��С
	 * 
	 * @param bm
	 *            ��ԭͼ
	 * @param newWidth
	 *            ������ϣ���õ��Ŀ�
	 * @param newHeight
	 *            ������ϣ���õ��ĸ�
	 */
	public static Bitmap resizeBitmap(Bitmap bm, int newWidth, int newHeight) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		// �������ű���
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// ȡ����Ҫ���ŵ�matrix����
		Matrix matrix = new Matrix();
		float scale = (scaleWidth <= scaleHeight) ? scaleWidth : scaleHeight;
		matrix.postScale(scale, scale);
		// ���ô�С
		return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
	}

	/**
	 * ����ת�ģ�����ͼƬ���ᰴ�� ������ű� ��С��ֵ��������
	 * 
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @param rotate
	 *            ��ת����
	 * @return
	 */
	public static Bitmap resizeBitmap(Bitmap bm, int newWidth, int newHeight,
			int rotate) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		// �������ű���
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// ȡ����Ҫ���ŵ�matrix����
		Matrix matrix = new Matrix();
		float scale = (scaleWidth <= scaleHeight) ? scaleWidth : scaleHeight;
		matrix.postScale(scale, scale);
		matrix.postRotate(rotate);
		// ���ô�С
		return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
	}

	/**
	 * ��ȡ��ת����
	 * 
	 * @param imagePath
	 * @return
	 */
	public static int getCameraPhotoOrientation(String imagePath) {
		int rotate = 0;
		try {
			File imageFile = new File(imagePath);
			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			}

			// Log.v(TAG, "Exif orientation: " + orientation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rotate;
	}

	/**
	 * ��ͼƬ���տ���бȽ�С�Ĳü���������
	 * 
	 * @param bm
	 * @return
	 */
	public static Bitmap cropBitmap(Bitmap bm) {
		int height = bm.getHeight();
		int width = bm.getWidth();
		if (height > width) {
			return Bitmap.createBitmap(bm, 0, (height - width) / 4, width,
					width);
		} else {
			return Bitmap.createBitmap(bm, (width - height) / 2, 0, height,
					height);
		}
	}


	/**
	 * ��ָ��·����ͼƬ����ָ���������(֧�ִ�ͼƬ)
	 * 
	 * @param filePath
	 * @param width
	 * @return
	 */
	public static Bitmap getMicroImage(String filePath, int width) {
		// ��ȡ�ļ�������
		InputStream is = null;
		Bitmap bitmap = null;
		try {
			File f = new File(filePath);
			is = new FileInputStream(f);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			// ��ȡ�������options
			BitmapFactory.decodeFile(filePath, options);
			int height = options.outHeight * width / options.outWidth;
			if (is != null) {
				int isSize = is.available();
				// ����1M=1048576�ֽڣ�����Ϊ��ͼ
				int base = 309600;
				if (isSize > base) {
					options.inSampleSize = 10; // width��hight��Ϊԭ����ʮ��һ
				} else if (isSize <= 409600 && isSize > 104800) {
					options.inSampleSize = 4;
				} else if (isSize <= 104800 && isSize > 60) {
					options.inSampleSize = 2;
				} else {
					options.inSampleSize = 1;
				}
			}
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeStream(is, null, options);
			int rotate = PictureManageUtil.getCameraPhotoOrientation(filePath);
			bitmap = PictureManageUtil.resizeBitmap(bitmap, width, height,
					rotate);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	/**
	 * ����·������ʧ��ѹ��
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getCompressBm(String filePath) {
		Bitmap bm = null;
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 500, 500);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(filePath, options);
		return bm;
	}

	/**
	 * ����ѹ������
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}

	/**
	 * ��תͼƬ
	 * 
	 * @param bitmap
	 * @param rotate
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
		if (bitmap == null)
			return null;

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		// Setting post rotate to 90
		Matrix mtx = new Matrix();
		mtx.postRotate(rotate);
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}

}
