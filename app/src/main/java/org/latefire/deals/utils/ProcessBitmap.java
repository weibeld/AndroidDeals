package org.latefire.deals.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import org.latefire.deals.MyApplication;

public class ProcessBitmap {
  /**
   *
   */
  protected ProcessBitmap() {

  }

  /**
   * Corner Bitmap (Bo góc tròn cho bipmap)
   *
   * @return Bitmap
   */
  public static Bitmap roundBitmap(Bitmap bitmap, int radius) {
    if (bitmap == null) {
      return null;
    }

    Paint paintForRound = new Paint();
    paintForRound.setAntiAlias(true);
    paintForRound.setColor(0xff424242);
    paintForRound.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
    Canvas canvas = new Canvas(output);

    final Rect RECT = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    final RectF RECTF = new RectF(RECT);

    canvas.drawARGB(0, 0, 0, 0);
    paintForRound.setXfermode(null);

    canvas.drawRoundRect(RECTF, radius, radius, paintForRound);

    paintForRound.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
    canvas.drawBitmap(bitmap, RECT, RECT, paintForRound);

    return output;
  }

  /**
   * Resize bitmap
   *
   * @return Bitmap
   */
  public static Bitmap decodeFile(Context context, Uri uri, int reqWidth, int reqHeight) {
    Bitmap bitmap = null;
    String path = null;
    try {
      if (uri != null) {
        path = getRealPathFromURI(context, uri);
      }

      if (path == null || path.length() <= 0) {
        bitmap = MediaStore.Images.Media.getBitmap(MyApplication.getInstance().getContentResolver(),
            uri);
      }

      // decode image size
      BitmapFactory.Options o = new BitmapFactory.Options();
      o.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(path, o);

      // decode with inSampleSize
      o.inSampleSize = calculateInSampleSize(o, null, reqWidth, reqHeight);
      o.inJustDecodeBounds = false;

      if (path != null) {
        bitmap = BitmapFactory.decodeFile(path, o);
      } else {
        return Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, false);
      }

      if (bitmap != null) {
        ExifInterface exif = new ExifInterface(path);
        int tag = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        int degree = 0;
        if ((tag == ExifInterface.ORIENTATION_ROTATE_180)) {
          degree = 180;
        } else if (tag == ExifInterface.ORIENTATION_ROTATE_90) {
          degree = 90;
        } else if (tag == ExifInterface.ORIENTATION_ROTATE_270) {
          degree = 270;
        }

        Bitmap temp = null;
        if (degree != 0) {
          Matrix matrix = new Matrix();
          matrix.setRotate(degree);

          temp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
              true);
        }

        if (temp != null) {
          bitmap.recycle();
          bitmap = null;

          bitmap = temp;
        }
        temp = null;
      }
    } catch (Exception ex) {
      bitmap = null;
      ex.printStackTrace();
    }

    return bitmap;
  }

  /**
   * Resize bitmap
   *
   * @return Bitmap
   */
  public static Bitmap decodeFile(Bitmap aBitmap, int reqWidth, int reqHeight) {
    Bitmap bitmap = null;

    try {
      bitmap = aBitmap;
      if (bitmap == null) {
        return bitmap;
      }

      // decode with inSampleSize
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inSampleSize = calculateInSampleSize(null, bitmap, reqWidth, reqHeight);

      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
      byte[] byteArray = stream.toByteArray();
      bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

      options = null;
      byteArray = null;
    } catch (Exception ex) {
      bitmap = null;
    } catch (OutOfMemoryError oome) {
      bitmap = null;
    }

    return bitmap;
  }

  /**
   * Resize bitmap
   *
   * @param f File
   * @param reqWidth int
   * @param reqHeight int
   * @return Bitmap
   */
  public static Bitmap decodeFile(File f, int reqWidth, int reqHeight) {
    Bitmap bitmap = null;

    try {
      // decode image size
      BitmapFactory.Options o = new BitmapFactory.Options();
      o.inJustDecodeBounds = true;
      BitmapFactory.decodeStream(new FileInputStream(f), null, o);

      // decode with inSampleSize
      BitmapFactory.Options o2 = new BitmapFactory.Options();
      o2.inSampleSize = calculateInSampleSize(o, null, reqWidth, reqHeight);
      bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
      String path = f.getPath();
      if (bitmap != null) {
        ExifInterface exif = new ExifInterface(path);
        int tag = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        int degree = 0;
        if ((tag == ExifInterface.ORIENTATION_ROTATE_180)) {
          degree = 180;
        } else if (tag == ExifInterface.ORIENTATION_ROTATE_90) {
          degree = 90;
        } else if (tag == ExifInterface.ORIENTATION_ROTATE_270) {
          degree = 270;
        }

        Bitmap temp = null;
        if (degree != 0) {
          Matrix matrix = new Matrix();
          matrix.setRotate(degree);

          temp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
              true);
        }

        if (temp != null) {
          bitmap.recycle();

          bitmap = temp;
        }
        temp = null;
      }
    } catch (Exception ex) {
      bitmap = null;
      ex.printStackTrace();
    }
    return bitmap;
  }

  /**
   * Tính tỉ lệ scale Có thể truyền vào BitmapFactiry.Option hoặc bitmap để
   * lấy chiều cao của ảnh
   *
   * @return int
   */
  public static int calculateInSampleSize(BitmapFactory.Options option, Bitmap bitmap, int reqWidth,
      int reqHeight) {
    int height = 0;
    int width = 0;

    if (option != null) {
      height = option.outWidth;
      width = option.outHeight;
    } else {
      height = bitmap.getHeight();
      width = bitmap.getWidth();
    }

    if (height == 0 || width == 0) {
      return 0;
    }

    int inSampleSize = 1;
    if (height > reqHeight || width > reqWidth) {
      if (width > height) {
        inSampleSize = Math.round((float) height / (float) reqHeight);
      } else {
        inSampleSize = Math.round((float) width / (float) reqWidth);
      }
    }

    return inSampleSize;
  }

  /**
   * Lấy đường dẫn ảnh từ URI
   *
   * @return String
   */
  public static String getRealPathFromURI(Context context, Uri contentUri) {
    String result = null;

    try {
      String[] proj = { MediaStore.Images.Media.DATA };
      //            Cursor cursor = ((Activity) context).managedQuery(contentUri, proj,
      //                    null, null, null);
      ContentResolver cr = context.getContentResolver();
      Cursor cursor = cr.query(contentUri, proj, null, null, null);
      if (cursor != null && !cursor.isClosed()) {
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        result = cursor.getString(columnIndex);
        if (Integer.parseInt(Build.VERSION.SDK) < 14) {
          cursor.close();
        }
      }
      cursor = null;
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return result;
  }
}