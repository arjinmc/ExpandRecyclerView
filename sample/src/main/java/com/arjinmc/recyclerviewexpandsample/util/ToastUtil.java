package com.arjinmc.recyclerviewexpandsample.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Eminem Lo on 2018/4/9.
 * email: arjinmc@hotmail.com
 */
public final class ToastUtil {

    private static Toast mToast;

    public static void show(Context context, String msg) {
        if (mToast == null) {
            mToast = new Toast(context);
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }
}
