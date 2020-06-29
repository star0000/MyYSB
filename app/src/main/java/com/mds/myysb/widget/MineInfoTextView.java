package com.mds.myysb.widget;

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
import android.widget.TextView;

import com.mds.myysb.R;

public class MineInfoTextView extends FrameLayout {

    private boolean isShow;
    private String tvString;
    private int isShowEt;

    private View mView;

    private TextView tvName;
    private TextView tvValue;
    private TextView tvIcon;
    private EditText etValue;


    public MineInfoTextView(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public MineInfoTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public MineInfoTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(attrs == null){
            return;
        }
        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.mineInfoTextView);
        isShow = typedArray.getBoolean(R.styleable.mineInfoTextView_isShow,false);
        tvString = typedArray.getString(R.styleable.mineInfoTextView_tv_hint);
        isShowEt = typedArray.getInt(R.styleable.mineInfoTextView_isShowEt,0);
        typedArray.recycle();
        //绑定layout布局
        mView = LayoutInflater.from(context).inflate(R.layout.mineinfotextview,this,false);
        tvIcon = mView.findViewById(R.id.tv_icon);
        tvName = mView.findViewById(R.id.tv_name);
        tvValue = mView.findViewById(R.id.tv_value);
        etValue = mView.findViewById(R.id.et_value);
        //布局关联属性
        tvName.setText(tvString);

        if(isShow){
            tvIcon.setVisibility(VISIBLE);
        }else{
            tvIcon.setVisibility(INVISIBLE);
        }

        if(isShowEt == 0){
            tvValue.setVisibility(VISIBLE);
            etValue.setVisibility(GONE);
        }else{
            tvValue.setVisibility(GONE);
            etValue.setVisibility(VISIBLE);
        }
        addView(mView);
    }

    public void setTvString(String tvString){
        tvValue.setText(tvString);
    }
    /*
     * 返回输入的内容
     */
    public String getTvString(){
        return tvValue.getText().toString().trim();
    }


    public void setEtString(String etString){
        etValue.setText(etString);
    }
    /*
     * 返回输入的内容
     */
    public String getEtString(){
        return etValue.getText().toString().trim();
    }

    public void setIsShowEt(int isShowEt) {
        if(isShowEt == 0){
            tvValue.setVisibility(VISIBLE);
            etValue.setVisibility(GONE);
        }else{
            tvValue.setVisibility(GONE);
            etValue.setVisibility(VISIBLE);
        }
    }

    public void setBackGroundColor(int color) {
        tvValue.setBackgroundColor(color);
        if(etValue != null){
            etValue.setBackgroundColor(color);
        }
    }

}
