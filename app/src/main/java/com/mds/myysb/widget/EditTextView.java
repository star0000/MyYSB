package com.mds.myysb.widget;

/*
 * 自定义输入框
 * 1,input_icon:输入框前面的图标
 * 2,input_hint:输入框的提示内容
 * 3,is_password:输入的内容是否需要以密文的形式展示
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mds.myysb.R;

public class EditTextView extends FrameLayout {

    private int inputIcon;
    private String inputHint;
    private boolean isPassword;

    private View mView;
    private ImageView mImgIcon;
    private EditText mEtInput;

    public EditTextView(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public EditTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public EditTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(attrs == null){
            return;
        }
        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.editTextView);
        inputIcon = typedArray.getResourceId(R.styleable.editTextView_input_icon,R.mipmap.ic_launcher);
        inputHint = typedArray.getString(R.styleable.editTextView_input_hint);
        isPassword = typedArray.getBoolean(R.styleable.editTextView_is_password,false);
        typedArray.recycle();
        //绑定layout布局
        mView = LayoutInflater.from(context).inflate(R.layout.edittextview,this,false);
        mImgIcon = mView.findViewById(R.id.img_Icon1);
        mEtInput = mView.findViewById(R.id.et_input);
        //布局关联属性
        mImgIcon.setImageResource(inputIcon);
        mEtInput.setHint(inputHint);
        mEtInput.setInputType(isPassword ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_PHONE);
        addView(mView);
    }

    /*
     * 返回输入的内容
     */
    public String getINputStr(){
        return mEtInput.getText().toString().trim();
    }

}
