package com.mds.myysb.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.mds.myysb.R;

public class MyDialog {

    public final static int PICK_AVATAR = 1;
    private LinearLayout llBack;
    private WebView webViewTutorial;
    // private ImageView imgBack;
    public View mView;
    public Dialog mDialog;
    private String uri = "http://114.215.142.138:8093//kuailefenxiang/yingyang/19619614851.html";

    public MyDialog(Context context, int type) {

        if(PICK_AVATAR==type) {
            LayoutInflater inflater = LayoutInflater.from(context);
            mView = inflater.inflate(R.layout.dialog_taixin_tishi, null);
            mDialog = new Dialog(context, R.style.dialog);
            mDialog.setContentView(mView);

            Window dialogWindow = mDialog.getWindow();
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); // 添加动画
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            lp.width = context.getResources().getDisplayMetrics().widthPixels; // 宽度
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
            dialogWindow.setAttributes(lp);

            mDialog.setCanceledOnTouchOutside(false);
            // imgBack = mView.findViewById(R.id.img_back);
            llBack = mView.findViewById(R.id.ll_back);
            webViewTutorial = mView.findViewById(R.id.webView_tutorial);
            webViewTutorial.loadUrl(uri);
            llBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }
    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

}
