package org.latefire.deals.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hung.nguyen on 11/14/2016.
 */

public class DeviceUtils {
  public static void pickFromGallery(Activity activity) {
    if (!PermissionHelper.checkPermission(activity, new String[] {
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    })) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PermissionHelper.requestPermission(activity, new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
        }, Constant.PERMISSION_READ_WRITE_STORAGE);
      }
    } else {
      Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
      photoPickerIntent.setType("image/*");
      activity.startActivityForResult(photoPickerIntent, Constant.FROM_GALLERY);
    }
  }

  public static void takePicture(Activity activity, File imageFile) {
    if (!PermissionHelper.checkPermission(activity, new String[] { Manifest.permission.CAMERA })) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PermissionHelper.requestPermission(activity, new String[] { Manifest.permission.CAMERA },
            Constant.PERMISSION_REQUEST_CAMERA);
      }
    } else {
      Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
        Uri photoURI = FileProvider.getUriForFile(activity,
            activity.getApplicationContext().getPackageName() + ".provider", imageFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, /*Uri.fromFile(imageFile)*/photoURI);
        activity.startActivityForResult(takePictureIntent, Constant.TAKE_A_PHOTO);
      }
    }
  }

  public static File getFileForCaptureImage(Activity activity) {
    if (!PermissionHelper.checkPermission(activity,
        new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE })) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PermissionHelper.requestPermission(activity,
            new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
            Constant.PERMISSION_CREATE_FILE);
      }
    } else {
      try {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */);
        takePicture(activity, imageFile);
        return imageFile;
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }
    return null;
  }

  public static void actionShare(Activity activity, String title, String content) {
    Intent sendIntent = new Intent();
    sendIntent.setAction(Intent.ACTION_SEND);
    sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
    sendIntent.putExtra(Intent.EXTRA_TEXT, content);
    sendIntent.setType("text/plain");
    activity.startActivity(sendIntent);
  }
}
