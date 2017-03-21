package org.latefire.deals.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import org.latefire.deals.R;

/**
 * Created by Ken on 2/2/2017.
 */

public class AlertDialogUtils {
  public static void showError(Context context, String message) {
    new AlertDialog.Builder(context).setTitle(R.string.app_name)
        .setMessage(message)
        .setIcon(R.mipmap.ic_launcher)
        .setNegativeButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
        .create()
        .show();
  }

  public static void showError(Context context, int message) {
    new AlertDialog.Builder(context).setTitle(R.string.app_name)
        .setMessage(message)
        .setIcon(R.mipmap.ic_launcher)
        .setNegativeButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
        .create()
        .show();
  }

  public static PopupWindow generatePopupWindow(Context context, RelativeLayout rltLayout,
      ListView listView) {
    PopupWindow popupWindow = new PopupWindow(context);
    popupWindow.setFocusable(true);
    popupWindow.setWidth(rltLayout.getWidth());
    popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
    popupWindow.setContentView(listView);
    return popupWindow;
  }

  public static Dialog showInfo(Context context, String des) {
    return showInfo(context, des, null, null);
  }

  public static Dialog showInfo(Context context, int des) {
    return showInfo(context, context.getString(des));
  }

  public static Dialog showInfo(Context context, int des, int btn,
      DialogInterface.OnDismissListener dismissListener) {
    return showInfo(context, context.getString(des), context.getString(btn), dismissListener);
  }

  public static Dialog showInfo(Context context, String des, String btn,
      DialogInterface.OnDismissListener dismissListener) {
    AlertDialog.Builder builder = dialogBuilder(context, des);
    builder.setCancelable(true);
    builder.setPositiveButton(btn, null);
    Dialog dialog = builder.show();
    dialog.setCanceledOnTouchOutside(true);
    dialog.setOnDismissListener(dismissListener);
    return dialog;
  }

  public static Dialog showConfirm(Context context, String msg,
      DialogInterface.OnClickListener onYesListener, DialogInterface.OnClickListener onNoListener) {
    AlertDialog.Builder builder = dialogBuilder(context, msg);
    builder.setCancelable(true);
    builder.setPositiveButton(android.R.string.yes, onYesListener);
    builder.setNegativeButton(android.R.string.no, onNoListener);
    Dialog dialog = builder.show();
    dialog.setCanceledOnTouchOutside(true);
    return dialog;
  }

  public static Dialog showConfirmNotCancelable(Context context, String msg,
      DialogInterface.OnClickListener onYesListener, DialogInterface.OnClickListener onNoListener) {
    AlertDialog.Builder builder = dialogBuilder(context, msg);
    builder.setCancelable(false);
    builder.setPositiveButton(android.R.string.yes, onYesListener);
    builder.setNegativeButton(android.R.string.no, onNoListener);
    Dialog dialog = builder.show();
    dialog.setCanceledOnTouchOutside(false);
    return dialog;
  }

  public static Dialog showConfirm(Context context, int msg,
      DialogInterface.OnClickListener onYesListener, DialogInterface.OnClickListener onNoListener) {
    AlertDialog.Builder builder = dialogBuilder(context, msg);
    builder.setCancelable(true);
    builder.setPositiveButton(android.R.string.yes, onYesListener);
    builder.setNegativeButton(android.R.string.no, onNoListener);
    Dialog dialog = builder.show();
    dialog.setCanceledOnTouchOutside(true);
    return dialog;
  }

  public static Dialog showConfirmNotCancelable(Context context, int msg,
      DialogInterface.OnClickListener onYesListener, DialogInterface.OnClickListener onNoListener) {
    AlertDialog.Builder builder = dialogBuilder(context, msg);
    builder.setCancelable(false);
    builder.setPositiveButton(android.R.string.yes, onYesListener);
    builder.setNegativeButton(android.R.string.no, onNoListener);
    Dialog dialog = builder.show();
    dialog.setCanceledOnTouchOutside(false);
    return dialog;
  }

  public static AlertDialog.Builder dialogBuilder(Context context, String msg) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    if (msg != null) {
      builder.setMessage(msg);
    }
    builder.setTitle(R.string.app_name);
    return builder;
  }

  public static AlertDialog.Builder dialogBuilder(Context context, int msg) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setMessage(msg);
    builder.setTitle(R.string.app_name);
    return builder;
  }
}
