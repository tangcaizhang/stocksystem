package zzh.project.stocksystem.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ProgressBar;

import zzh.project.stocksystem.R;

public class LoadingBuilder {
    public static Dialog build(Context context) {
        return build(context, true, null);
    }

    public static Dialog build(Context context, DialogInterface.OnCancelListener listener) {
        return build(context, true, listener);
    }

    public static Dialog build(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        Dialog dialog = new Dialog(context, R.style.LoadingDialog);
        dialog.setContentView(new ProgressBar(context));
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        return dialog;
    }
}
