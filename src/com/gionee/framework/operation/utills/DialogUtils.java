package com.gionee.framework.operation.utills;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * com.gionee.app_framework.operation.utills.DialogUtils
 * 
 * @author yuwei <br/>
 *         create at 2013-3-18 上午9:37:40 TODO 封装到dialog的操作
 */
public class DialogUtils {
    private static AlertDialog dialog;
    private static ProgressDialog loadingDialog;

    public static void showLoading(Context context, String message, boolean canDismiss,
            OnCancelListener listener) {
        loadingDialog = new ProgressDialog(context);
        if (message != null) {
            loadingDialog.setMessage(message);
        } else {
            loadingDialog.setMessage("please wait...");
        }
        loadingDialog.setCancelable(canDismiss);
        loadingDialog.show();
        if (listener != null) {
            loadingDialog.setOnCancelListener(listener);
        }
    }

    public static void closeLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            try {
                loadingDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static AlertDialog showMyDialog(Context context, String title, String message, String leftButton,
            String rightButton, OnClickListener listener) {
        dialog = new AlertDialog.Builder(context).create();
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }
        dialog.setMessage(message);
        dialog.setButton(leftButton, listener);
        dialog.setButton2(rightButton, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

        return dialog;
    }

    public static AlertDialog showMyDialog(Context context, int title, String message, String leftButton,
            String rightButton, OnClickListener listener) {
        dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(leftButton, listener);
        dialog.setButton2(rightButton, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

        return dialog;
    }

    public static AlertDialog showMyInfoDialog(Context context, String title, String message,
            String buttonStr, OnClickListener listener) {
        dialog = new AlertDialog.Builder(context).create();
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }
        dialog.setMessage(message);
        if (listener != null) {
            dialog.setButton(buttonStr, listener);
        } else {
            dialog.setButton(buttonStr, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();

        return dialog;
    }

    public static AlertDialog showMyInfoDialog(Context context, int title, String message, String buttonStr,
            OnClickListener listener) {
        dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        if (listener != null) {
            dialog.setButton(buttonStr, listener);
        } else {
            dialog.setButton(buttonStr, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
        return dialog;
    }

    public static void showToast(Context context, String message) {
        if (context == null) {
            return;
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int id) {
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
    }

    public static void showLoading(Context context, String message, boolean canDismiss) {
        showLoading(context, message, canDismiss, null);
    }

    public static AlertDialog showMyInfoDialog(Context context, int title, String message) {
        dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

        return dialog;
    }

    public static AlertDialog showMyInfoDialog(Context context, String title, String message) {
        dialog = new AlertDialog.Builder(context).create();
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }
        dialog.setMessage(message);
        dialog.setButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

        return dialog;
    }

    public static AlertDialog showMyInfoDialog(Context context, String message) {
        dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("提示信息");
        dialog.setMessage(message);
        dialog.setButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

        return dialog;
    }

    public static AlertDialog showMyInfoDialog(Context context, int message) {
        dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("提示信息");
        dialog.setMessage(context.getResources().getString(message));
        dialog.setButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

        return dialog;
    }

}
