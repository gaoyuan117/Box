package com.xzwzz.orange.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;
import com.xzwzz.orange.glide.GlideApp;

/**
 * 对话框辅助类
 */
public class DialogHelp {

    /***
     * 获取一个dialog
     * @param context
     * @return
     */
    public static AlertDialog.Builder getDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder;
    }

    public static void showMainTainDialog(Context context, String content) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_show_shoprtc);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        ((TextView) dialog.findViewById(R.id.tv_content)).setText(content);
        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /***
     * 获取一个耗时等待对话框
     * @param context
     * @param message
     * @return
     */
    public static ProgressDialog getWaitDialog(Context context, String message) {
        ProgressDialog waitDialog = new ProgressDialog(context);
        if (!TextUtils.isEmpty(message)) {
            waitDialog.setMessage(message);
        }
        return waitDialog;
    }

    /***
     * 获取一个信息对话框，注意需要自己手动调用show方法显示
     * @param context
     * @param message
     * @param onClickListener
     * @return
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String message, android.content.DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(message);
        builder.setPositiveButton("确定", onClickListener);
        return builder;
    }

    public static AlertDialog.Builder getMessageDialog(Context context, String message) {
        return getMessageDialog(context, message, null);
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message, android.content.DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        return builder;
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message, android.content.DialogInterface.OnClickListener onOkClickListener, android.content.DialogInterface.OnClickListener onCancleClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(message);
        builder.setPositiveButton("确定", onOkClickListener);
        builder.setNegativeButton("取消", onCancleClickListener);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String title, String[] arrays, android.content.DialogInterface.OnClickListener onClickListener) {
        return getSelectDialog(context, title, null, arrays, onClickListener);
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String title, String message, String[] arrays, android.content.DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        builder.setPositiveButton("取消", null);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String[] arrays, android.content.DialogInterface.OnClickListener onClickListener) {
        return getSelectDialog(context, "", arrays, onClickListener);
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String title, String[] arrays, int selectIndex, android.content.DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setNegativeButton("取消", null);
        return builder;
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String[] arrays, int selectIndex, android.content.DialogInterface.OnClickListener onClickListener) {
        return getSingleChoiceDialog(context, "", arrays, selectIndex, onClickListener);
    }

    public static void showInputContentDialog(Context context, String title, String content, final DialogInterface dialogInterface) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_input);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        ((TextView) dialog.findViewById(R.id.tv_title)).setText(title);
        ((EditText) dialog.findViewById(R.id.et_input)).setHint(content);
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(view -> dialogInterface.cancelDialog(view, dialog));
        dialog.findViewById(R.id.btn_confirm).setOnClickListener(view -> dialogInterface.determineDialog(view, dialog));
    }


    public interface DialogInterface {
        void cancelDialog(View v, Dialog d);

        void determineDialog(View v, Dialog d);
    }


    public static Dialog showfreetimeOutDialog(final Activity context, String message, View.OnClickListener buyvip) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_show_freetimeout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(dialog1 -> context.finish());
        TextView messView = dialog.findViewById(R.id.dialog_freetime_message);
        messView.setText(message);
        dialog.findViewById(R.id.dialog_freetime_close).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.dialog_freetime_buyvip).setOnClickListener(buyvip);
        return dialog;
    }

    public static Dialog showMainDialog(final Activity context, String message, View.OnClickListener buyvip) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_qq);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(dialog1 -> context.finish());
        TextView messView = dialog.findViewById(R.id.dialog_freetime_message);
        messView.setText(Html.fromHtml(message));

        dialog.findViewById(R.id.dialog_freetime_close).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.dialog_freetime_buyvip).setOnClickListener(buyvip);
        return dialog;
    }


    public static void showQrCodeDialog(final Context context, String imgUrl) {
        final Dialog dialog = new Dialog(context, R.style.dialog_gift);
        View view = View.inflate(context, R.layout.dialog_show_qrcode, null);
        dialog.setContentView(view);
        ImageView iv = view.findViewById(R.id.iv_showqrcode);
        TextView tv = view.findViewById(R.id.tv_code);
        tv.setText("邀请码:"+  AppContext.getInstance().getLoginUser().id);
        ImageView close = view.findViewById(R.id.qrcode_close);
        close.setOnClickListener(v -> dialog.dismiss());
        GlideApp.with(context).load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(iv);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }
}
