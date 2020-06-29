package com.mds.myysb.ui.activity;


import android.os.Bundle;

import android.view.KeyEvent;

import android.widget.Button;

import com.mds.myysb.R;
import com.mds.myysb.base.BaseActivity;

public class SystemActivity extends BaseActivity {

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);
        btn = findViewById(R.id.btn_details);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
