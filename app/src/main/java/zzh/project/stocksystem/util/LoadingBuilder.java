package zzh.project.stocksystem.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ProgressBar;

import zzh.project.stocksystem.R;

public class LoadingBuilder {
    public static Dialog build(Context context) {
        return build(context, false, null);
    }

    public static Dialog build(Context context, DialogInterface.OnCancelListener listener) {
        return build(context, false, listener);
    }

    public static Dialog build(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        Dialog dialog = new Dialog(context, R.style.LoadingDialog);
        dialog.setContentView(new ProgressBar(context));
        dialog.setCancelable(cancelable);
        if (cancelListener != null) {
            dialog.setOnCancelListener(cancelListener);
        }
        return dialog;
    }
}
