package com.mds.myysb.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mds.myysb.R;

/**
 * 体重管理页面
 */

public class WeightManagementFragment extends Fragment {

    private View view;

    public static WeightManagementFragment newInstance() {
        WeightManagementFragment fragment = new WeightManagementFragment();
        return fragment;
    }

    public WeightManagementFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weightmanagement, container, false);
        return view;
    }

}
