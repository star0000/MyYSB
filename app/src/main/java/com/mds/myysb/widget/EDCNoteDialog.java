package com.mds.myysb.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mds.myysb.R;

public class EDCNoteDialog implements View.OnClickListener{

    private Context mContext;
    private boolean mCanDialogShow;
    private Dialog eDCNoteDialog;
    private TextView tvEDCNotedialog;


    public EDCNoteDialog(Context context) {
        if (context == null) {
            mCanDialogShow = false;
            return;
        }
        mContext = context;
        initView();
        mCanDialogShow = true;
    }

    private void initView() {
        eDCNoteDialog = new Dialog(mContext, R.style.date_picker_dialog);
        eDCNoteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        eDCNoteDialog.setContentView(R.layout.dialog_edc_note);

        Window window = eDCNoteDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }

        eDCNoteDialog.findViewById(R.id.btn_sure).setOnClickListener(this);
        eDCNoteDialog.findViewById(R.id.date_choose_back).setOnClickListener(this);
        tvEDCNotedialog = (TextView) eDCNoteDialog.findViewById(R.id.tv_EDCNotedialog);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                if (eDCNoteDialog != null && eDCNoteDialog.isShowing()) {
                    eDCNoteDialog.dismiss();
                }
                break;
            case R.id.date_choose_back:
                if (eDCNoteDialog != null && eDCNoteDialog.isShowing()) {
                    eDCNoteDialog.dismiss();
                }
                break;
        }
    }


    public void show() {
        if (!canShow()){
            return;
        }
        eDCNoteDialog.show();
    }

    private boolean canShow() {
        return mCanDialogShow && eDCNoteDialog != null;
    }

    /**
     * 设置是否允许点击屏幕或物理返回键关闭
     */
    public void setCancelable(boolean cancelable) {
        if (!canShow()) return;

        eDCNoteDialog.setCancelable(cancelable);
    }


    /**
     * 销毁弹窗
     */
    public void onDestroy() {
        if (eDCNoteDialog != null) {
            eDCNoteDialog.dismiss();
            eDCNoteDialog = null;
        }
    }


}
