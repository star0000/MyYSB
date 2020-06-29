package com.mds.myysb.ui.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.mds.myysb.R;

/**
 * 胎心监测主页面
 */

public class FetalHeartMonitoringFragment extends Fragment {

    private View view;

    private TextView title_left;
    private TextView title_right;
    private FrameLayout frameLayout;

    private Activity context;

    private final String[] TITLE = new String[] {"实时监测","胎心记录"}; // Tab的标题
    private FragmentManager fm;


    public static FetalHeartMonitoringFragment newInstance() {
        FetalHeartMonitoringFragment fragment = new FetalHeartMonitoringFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fetalheartmonitoring, container, false);
        initView(view);
        context = getActivity();
        return view;
    }

    private void initView(View view) {
        title_left= (TextView) view.findViewById(R.id.jiance_tv_title_left);
        title_right= (TextView) view.findViewById(R.id.jiance_tv_title_right);
        frameLayout= (FrameLayout) view.findViewById(R.id.frameLayout);
    }

}
