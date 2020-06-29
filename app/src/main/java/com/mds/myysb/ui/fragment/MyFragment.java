package com.mds.myysb.ui.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.mds.myysb.R;
import com.mds.myysb.bean.CityEntity;
import com.mds.myysb.bean.GetAccountBean;
import com.mds.myysb.bean.GetCaptchaStatusBean;
import com.mds.myysb.bean.GetUserInfoBean;
import com.mds.myysb.bean.SetUserPicBean;
import com.mds.myysb.bean.UserIdBean;
import com.mds.myysb.net.BaseObserver;
import com.mds.myysb.net.BaseReponse;
import com.mds.myysb.net.BaseRequest;
import com.mds.myysb.net.MyConstant;
import com.mds.myysb.ui.activity.SystemActivity;
import com.mds.myysb.utils.ChangerDateUtil;
import com.mds.myysb.utils.CityManager;
import com.mds.myysb.utils.CountDownTimerUtils;
import com.mds.myysb.utils.FileUtils;
import com.mds.myysb.utils.GetImagePath;
import com.mds.myysb.utils.HttpsURLConnectionUtil;
import com.mds.myysb.utils.MobileNOUtil;
import com.mds.myysb.utils.NetworkUtils;
import com.mds.myysb.utils.SelectDialog;
import com.mds.myysb.utils.SharedPrefUtil;
import com.mds.myysb.utils.StringUtil;
import com.mds.myysb.utils.ToastUtil;
import com.mds.myysb.widget.EditTextView;
import com.mds.myysb.widget.MineInfoTextView;
import com.mds.myysb.widget.RoundImageView;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 我的页面
 */

public class MyFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;

    private RelativeLayout rlRegister;
    private LinearLayout llPersonal;

    //登录注册页面控件
    private EditTextView et_phone;
    private EditText et_verificationCode;
    private Button btn_verificationCode;
    private RelativeLayout rl_protocol2;
    private Button btn_login;
    //基本信息页面控件
    private ImageView self_iv_system;
    private RoundImageView img_icon;
    private TextView tv_muserName;
    private TextView tv_userEDC_icon;
    private TextView tv_userEDC;
    //基本信息页面医生,基本,账户控件
    private TextView tv_doctor_info;
    private View iv_doctor_info_sel;
    private TextView tv_basic_info;
    private View iv_basic_info_sel;
    private TextView tv_account_info;
    private View iv_account_info_sel;
    //基本信息页面基本信息模块控件
    private LinearLayout ll_personal_info;
    private MineInfoTextView tv_registrationID;
    private MineInfoTextView tv_userName;
    private MineInfoTextView tv_birthday;
    private MineInfoTextView tv_EDC;
    private MineInfoTextView tv_CASRN;
    private MineInfoTextView tv_fileCreat;
    private MineInfoTextView tv_Location;
    //基本信息编辑按钮
    private LinearLayout ll_basicinfoNo;
    private ImageView img_basicinfoNo;
    private ImageView img_basicinfo;
    //基本信息页面医生信息模块控件
    private LinearLayout ll_doctor_info;
    private MineInfoTextView zhidingdoctor;
    private MineInfoTextView tv_hospital;
    private MineInfoTextView tv_doctor;
    //基本信息页面账户信息模块控件
    private LinearLayout ll_account_info;
    private MineInfoTextView tv_nowForegift;
    private MineInfoTextView tv_nowPay;
    private MineInfoTextView tv_nowPrize;
    private MineInfoTextView tv_nowBalance;
    private Button btn_details;

    //获取验证码计时器
    private CountDownTimerUtils countDownTimerUtils;
    private String sessionId;

    private File file;
    private CityManager mCityManager;

    private boolean isEditting = false;

    //生日,预产期,建档期,所在地选择
    private DatePickerDialog dpdBirthday;
    private int BirthdayYear = 2000, BirthdayMonth, BirthdayDay;
    private DatePickerDialog dpdEDC;
    private int EDCYear = 2020, EDCMonth, EDCDay;
    private DatePickerDialog dpdFileCreat;
    private int FileCreatYear = 2020, FileCreatMonth, FileCreatDay;
    private OptionsPickerView LocationOptions;
    private List<List<String>> districtsMap;
    private List<String> districtMap;
    //	省份
    List<String> sheng = new ArrayList<>();
    //	城市
    List<String> cities;
    List<List<String>> shi = new ArrayList<>();
    //	区县
    List<String> district;
    List<List<String>> districts;
    List<List<List<String>>> xian = new ArrayList<>();
    List<List<List<String>>> xianMap = new ArrayList<>();

    private GetUserInfoBean getUserInfoBean;


    private File mCameraFile;
    public static final String TMP_PATH = "temp.png";

    private final int CAMERA_WITH_DATA = 1992;
    private final int SELECT_PIC_KITKAT = 1648;
    private final int IMAGE_REQUEST_CODE = 1759;
    /**
     * 截取结束标志
     */
    private static final int FLAG_MODIFY_FINISH = 1994;


    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rlRegister = (RelativeLayout) view.findViewById(R.id.rl_register);
        llPersonal = (LinearLayout) view.findViewById(R.id.ll_personal);

        et_phone = (EditTextView) view.findViewById(R.id.et_phone);
        et_verificationCode = (EditText) view.findViewById(R.id.et_verificationCode);
        btn_verificationCode = (Button) view.findViewById(R.id.btn_verificationCode);
        rl_protocol2 = (RelativeLayout) view.findViewById(R.id.rl_protocol2);
        btn_login = (Button) view.findViewById(R.id.btn_login);

        self_iv_system = (ImageView) view.findViewById(R.id.self_iv_system);
        img_icon = (RoundImageView) view.findViewById(R.id.img_icon);
        tv_muserName = (TextView) view.findViewById(R.id.tv_muserName);
        tv_userEDC_icon = (TextView) view.findViewById(R.id.tv_userEDC_icon);
        tv_userEDC = (TextView) view.findViewById(R.id.tv_userEDC);

        tv_doctor_info = (TextView) view.findViewById(R.id.tv_doctor_info);
        iv_doctor_info_sel = (View) view.findViewById(R.id.iv_doctor_info_sel);
        tv_basic_info = (TextView) view.findViewById(R.id.tv_basic_info);
        iv_basic_info_sel = (View) view.findViewById(R.id.iv_basic_info_sel);
        tv_account_info = (TextView) view.findViewById(R.id.tv_account_info);
        iv_account_info_sel = (View) view.findViewById(R.id.iv_account_info_sel);

        ll_personal_info = (LinearLayout) view.findViewById(R.id.ll_personal_info);
        tv_registrationID = (MineInfoTextView) view.findViewById(R.id.tv_registrationID);
        tv_userName = (MineInfoTextView) view.findViewById(R.id.tv_userName);
        tv_birthday = (MineInfoTextView) view.findViewById(R.id.tv_birthday);
        tv_EDC = (MineInfoTextView) view.findViewById(R.id.tv_EDC);
        tv_CASRN = (MineInfoTextView) view.findViewById(R.id.tv_CASRN);
        tv_fileCreat = (MineInfoTextView) view.findViewById(R.id.tv_fileCreat);
        tv_Location = (MineInfoTextView) view.findViewById(R.id.tv_Location);

        ll_basicinfoNo = (LinearLayout) view.findViewById(R.id.ll_basicinfoNo);
        img_basicinfoNo = (ImageView) view.findViewById(R.id.img_basicinfoNo);
        img_basicinfo = (ImageView) view.findViewById(R.id.img_basicinfo);

        ll_doctor_info = (LinearLayout) view.findViewById(R.id.ll_doctor_info);
        zhidingdoctor = (MineInfoTextView) view.findViewById(R.id.zhidingdoctor);
        tv_hospital = (MineInfoTextView) view.findViewById(R.id.tv_hospital);
        tv_doctor = (MineInfoTextView) view.findViewById(R.id.tv_doctor);

        ll_account_info = (LinearLayout) view.findViewById(R.id.ll_account_info);
        tv_nowForegift = (MineInfoTextView) view.findViewById(R.id.tv_nowForegift);
        tv_nowPay = (MineInfoTextView) view.findViewById(R.id.tv_nowPay);
        tv_nowPrize = (MineInfoTextView) view.findViewById(R.id.tv_nowPrize);
        tv_nowBalance = (MineInfoTextView) view.findViewById(R.id.tv_nowBalance);
        btn_details = (Button) view.findViewById(R.id.btn_details);

        //注册登录相关的按钮监听事件
        btn_verificationCode.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        //系统页面按钮监听事件
        self_iv_system.setOnClickListener(this);
        //头像按钮监听事件
        img_icon.setOnClickListener(this);
        //医生,基本,账户信息按钮监听事件
        tv_doctor_info.setOnClickListener(this);
        tv_basic_info.setOnClickListener(this);
        tv_account_info.setOnClickListener(this);
        //编辑按钮监听事件
        img_basicinfo.setOnClickListener(this);
        img_basicinfoNo.setOnClickListener(this);
        //生日,预产期,建档期,所在地按钮监听事件
        tv_birthday.setOnClickListener(this);
        tv_EDC.setOnClickListener(this);
        tv_fileCreat.setOnClickListener(this);
        tv_Location.setOnClickListener(this);

        //初始化获取验证码计时器
        countDownTimerUtils = new CountDownTimerUtils(btn_verificationCode, 60 * 1000, 1000);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        sessionId = SharedPrefUtil.getSessionId(context);
        registerView();
        initData();
    }

    private void initData() {
        getPersonalBasicInfo();
        getAccountInfo();
    }

    private void registerView() {
        if (!StringUtil.isStrNull(sessionId)) {
            rlRegister.setVisibility(View.GONE);
            llPersonal.setVisibility(View.VISIBLE);
        } else {
            rlRegister.setVisibility(View.VISIBLE);
            llPersonal.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.self_iv_system:
                startActivity(new Intent(getActivity(), SystemActivity.class));
                break;
            case R.id.btn_verificationCode:
                showSoftInputFromWindow(et_verificationCode);
                if (!NetworkUtils.isNetWorkAvailable(getActivity())) {
                    ToastUtil.showToast(context,getString(R.string.show_NetWork));
                    return;
                }
                String phoneNum = et_phone.getINputStr();
                if(!MobileNOUtil.isMobileNO(phoneNum)){
                    ToastUtil.showToast(context,getString(R.string.is_phone));
                    return;
                }
                countDownTimerUtils.start();
                getVerificationCode();
                break;
            case R.id.btn_login:
                String phoneNum1 = et_phone.getINputStr();
                String verificationCode = et_verificationCode.getText().toString().trim();
                if (!MobileNOUtil.isMobileNO(phoneNum1)) {
                    // 手机号为空or手机号格式不对
                    ToastUtil.showToast(context,getString(R.string.is_phone));
                }else if(StringUtil.isStrNull(verificationCode)) {
                    // 验证码为空
                    ToastUtil.showToast(context,getString(R.string.verificationCode));
                }else if(!NetworkUtils.isNetWorkAvailable(context)){
                    // 判断是否打开网络
                    ToastUtil.showToast(context,getString(R.string.show_NetWork));
                } else {
                    registerOrLogin();
                }
                break;
            case R.id.img_icon:
                if (!NetworkUtils.isNetWorkAvailable(context)) {
                    Toast.makeText(context, getString(R.string.show_NetWork), Toast.LENGTH_SHORT).show();
                    return;
                }
//				//点击头像，相片上传
                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
            case R.id.tv_doctor_info:
                ll_basicinfoNo.setVisibility(View.GONE);

                tv_doctor_info.setTextColor(Color.parseColor("#ff89a8"));
                iv_doctor_info_sel.setVisibility(View.VISIBLE);
                tv_basic_info.setTextColor(Color.parseColor("#2b2b2b"));
                iv_basic_info_sel.setVisibility(View.INVISIBLE);
                tv_account_info.setTextColor(Color.parseColor("#2b2b2b"));
                iv_account_info_sel.setVisibility(View.INVISIBLE);

                ll_doctor_info.setVisibility(View.VISIBLE);
                ll_personal_info.setVisibility(View.GONE);
                ll_account_info.setVisibility(View.GONE);
                break;
            case R.id.tv_basic_info:
                ll_basicinfoNo.setVisibility(View.VISIBLE);

                tv_doctor_info.setTextColor(Color.parseColor("#2b2b2b"));
                iv_doctor_info_sel.setVisibility(View.INVISIBLE);
                tv_basic_info.setTextColor(Color.parseColor("#ff89a8"));
                iv_basic_info_sel.setVisibility(View.VISIBLE);
                tv_account_info.setTextColor(Color.parseColor("#2b2b2b"));
                iv_account_info_sel.setVisibility(View.INVISIBLE);

                ll_doctor_info.setVisibility(View.GONE);
                ll_personal_info.setVisibility(View.VISIBLE);
                ll_account_info.setVisibility(View.GONE);
                break;
            case R.id.tv_account_info:
                ll_basicinfoNo.setVisibility(View.GONE);

                tv_doctor_info.setTextColor(Color.parseColor("#2b2b2b"));
                iv_doctor_info_sel.setVisibility(View.INVISIBLE);
                tv_basic_info.setTextColor(Color.parseColor("#2b2b2b"));
                iv_basic_info_sel.setVisibility(View.INVISIBLE);
                tv_account_info.setTextColor(Color.parseColor("#ff89a8"));
                iv_account_info_sel.setVisibility(View.VISIBLE);

                ll_doctor_info.setVisibility(View.GONE);
                ll_personal_info.setVisibility(View.GONE);
                ll_account_info.setVisibility(View.VISIBLE);
                break;
            case R.id.img_basicinfo:
                //点击编辑个人信息按钮
                if (isEditting) {
                    if(!StringUtil.isStrNull(tv_userName.getEtString())&&!StringUtil.isStrNull(tv_birthday.getTvString())&&!StringUtil.isStrNull(tv_EDC.getTvString())&&!StringUtil.isStrNull(tv_Location.getTvString())){
                        isEditting = false;
                        img_basicinfo.setImageResource(R.drawable.self_bianji_sel);
                        img_basicinfoNo.setVisibility(View.GONE);

                        tv_userName.setIsShowEt(0);
                        tv_userName.setTvString(tv_userName.getEtString());
                        tv_userName.setBackGroundColor(Color.parseColor("#00595959"));
                        tv_birthday.setBackGroundColor(Color.parseColor("#00595959"));
                        tv_EDC.setBackGroundColor(Color.parseColor("#00595959"));
                        tv_CASRN.setIsShowEt(0);
                        tv_CASRN.setTvString(tv_CASRN.getEtString());
                        tv_CASRN.setBackGroundColor(Color.parseColor("#00595959"));
                        tv_fileCreat.setBackGroundColor(Color.parseColor("#00595959"));
                        tv_Location.setBackGroundColor(Color.parseColor("#00595959"));
                        //上传修改的信息
                        keepBasicInfo();
                    }else{
                        ToastUtil.showToast(context,"必填项不能为空!");
                        isEditting = true;
                    }
                } else {
                    isEditting = true;
                    img_basicinfo.setImageResource(R.drawable.self_bianji);
                    img_basicinfoNo.setVisibility(View.VISIBLE);

                    tv_userName.setIsShowEt(1);
                    tv_userName.setEtString(tv_userName.getTvString());
                    tv_userName.setBackGroundColor(Color.parseColor("#11262626"));
                    tv_birthday.setBackGroundColor(Color.parseColor("#11262626"));
                    tv_EDC.setBackGroundColor(Color.parseColor("#11262626"));
                    tv_CASRN.setIsShowEt(1);
                    tv_CASRN.setEtString(tv_CASRN.getTvString());
                    tv_CASRN.setBackGroundColor(Color.parseColor("#11262626"));
                    tv_fileCreat.setBackGroundColor(Color.parseColor("#11262626"));
                    tv_Location.setBackGroundColor(Color.parseColor("#11262626"));
                }
                break;
            case R.id.img_basicinfoNo:
                isEditting = false;
                img_basicinfo.setImageResource(R.drawable.self_bianji_sel);
                img_basicinfoNo.setVisibility(View.GONE);

                tv_userName.setIsShowEt(0);
                tv_userName.setBackGroundColor(Color.parseColor("#00595959"));
                tv_birthday.setBackGroundColor(Color.parseColor("#00595959"));
                tv_EDC.setBackGroundColor(Color.parseColor("#00595959"));
                tv_CASRN.setIsShowEt(0);
                tv_CASRN.setBackGroundColor(Color.parseColor("#00595959"));
                tv_fileCreat.setBackGroundColor(Color.parseColor("#00595959"));
                tv_Location.setBackGroundColor(Color.parseColor("#00595959"));
                if(getUserInfoBean != null){
                    if (getUserInfoBean.getBirthday().length() > 9) {
                        tv_birthday.setTvString(getUserInfoBean.getBirthday().substring(0, getUserInfoBean.getBirthday().indexOf(" ")));
                    }
                    if (getUserInfoBean.getEdc().length() > 9) {
                        tv_EDC.setTvString(getUserInfoBean.getEdc().substring(0, getUserInfoBean.getEdc().indexOf(" 0")));
                    }
                    if(!StringUtil.isStrNull(getUserInfoBean.getFilingTime())){
                        tv_fileCreat.setTvString(getUserInfoBean.getFilingTime().substring(0, getUserInfoBean.getFilingTime().indexOf(" 0")));
                    }
                    if(!StringUtil.isStrNull(getUserInfoBean.getRegionName())){
                        //获得第一个点的位置
                        int index=getUserInfoBean.getRegionName().indexOf(" ");
                        //根据第一个点的位置 获得第二个点的位置
                        int index1=getUserInfoBean.getRegionName().indexOf(" ", index+1);
                        //根据第二个点的位置，截取字符串。得到结果 result
                        String sheng =getUserInfoBean.getRegionName().substring(index1);
                        String shi =getUserInfoBean.getRegionName().substring(index,index1);
                        String xian =getUserInfoBean.getRegionName().substring(0,index);
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append(sheng);
                        stringBuffer.append(shi);
                        stringBuffer.append(" ");
                        stringBuffer.append(xian);
                        tv_Location.setTvString(stringBuffer.toString());
                    }
                }
                break;
            case R.id.tv_birthday:
                //选择生日
                if (isEditting) {
                    if(dpdBirthday == null) {
                        dpdBirthday = new DatePickerDialog(getActivity(), DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {
                                 BirthdayYear = myyear;
                                 BirthdayMonth = monthOfYear;
                                 BirthdayDay = dayOfMonth;
                                Calendar cd = Calendar.getInstance();
                                int i = cd.get(Calendar.YEAR);
                                if(i - BirthdayYear > 10 && i - BirthdayYear <50){
                                    updateDate(BirthdayYear,BirthdayMonth,BirthdayDay);
                                }else{
                                    Toast.makeText(context,"请选择正确的日期!",Toast.LENGTH_SHORT).show();
                                }
                            }
                            private void updateDate(int year,int month,int day) {
                                tv_birthday.setTvString(year + "-" + (month + 1) + "-" + day);
                            }
                        }, BirthdayYear, BirthdayMonth, BirthdayDay);
                    }
                    dpdBirthday.show();
                } else {
                    return;
                }
                break;
            case R.id.tv_EDC:
                //选择预产期
                if (isEditting) {
                    if (dpdEDC == null) {
                        dpdEDC = new DatePickerDialog(getActivity(), DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                            /**
                             * params：view：该事件关联的组件
                             * params：myyear：当前选择的年
                             * params：monthOfYear：当前选择的月
                             * params：dayOfMonth：当前选择的日
                             */
                            @Override
                            public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {
                                //修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
                                EDCYear = myyear;
                                EDCMonth = monthOfYear;
                                EDCDay = dayOfMonth;
                                //更新日期
                                updateDate();
                            }
                            //当DatePickerDialog关闭时，更新日期显示
                            private void updateDate() {
                                String s = EDCYear + "-" + (EDCMonth + 1) + "-" + EDCDay;
                                //获取当前时间
                                Date nowDate = new Date(System.currentTimeMillis());
                                Date dateAfter = ChangerDateUtil.getDateAfter(nowDate, 280);
                                Date date = ChangerDateUtil.StringToDate(s);
                                if (date.getTime() > nowDate.getTime() && date.getTime() <= dateAfter.getTime()) {
                                    //在TextView上显示日期
                                    tv_EDC.setTvString(EDCYear + "-" + (EDCMonth + 1) + "-" + EDCDay);
                                } else {
                                    Toast.makeText(context, "请选择正确的预产期", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, EDCYear, EDCMonth, EDCDay);
                    }
                    dpdEDC.show();//显示DatePickerDialog组件
                }else{
                    return;
                }
                break;
            case R.id.tv_fileCreat:
                //选择建档期
                if (isEditting) {
                    if(dpdFileCreat == null) {
                        dpdFileCreat = new DatePickerDialog(getActivity(), DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {
                                FileCreatYear = myyear;
                                FileCreatMonth = monthOfYear;
                                FileCreatDay = dayOfMonth;
                                updateDate();
                            }
                            private void updateDate() {
                                tv_fileCreat.setTvString(FileCreatYear + "-" + (FileCreatMonth + 1) + "-" + FileCreatDay);
                            }
                        }, FileCreatYear, FileCreatMonth, FileCreatDay);
                    }
                    dpdFileCreat.show();
                } else {
                    return;
                }
                break;
            case R.id.tv_Location:
                //选择地区
                if (isEditting) {
                    if(mCityManager == null){
                        // 先在手机中拷贝一份关于地区的数据库
                        file = new File(Environment.getExternalStorageDirectory()
                                + "/Android/data/" + context.getPackageName() + "/city.db");
                        saveToDatabases();
                        // 转换地区码
                        mCityManager = new CityManager(file.getAbsolutePath());
                    }
                    List<CityEntity> allsheng = mCityManager.getCity("CN");
                    for (int i = 0; i < allsheng.size(); i++) {
                        sheng.add(allsheng.get(i).getName());
                        cities = new ArrayList<>();
                        districts = new ArrayList<>();
                        districtsMap = new ArrayList<>();
                        List<CityEntity> allshi = mCityManager.getCity(allsheng.get(i).getId());
                        for (int j = 0; j < allshi.size(); j++) {
                            cities.add(allshi.get(j).getName());
                            district = new ArrayList<>();
                            districtMap = new ArrayList<>();
                            List<CityEntity> allxian = mCityManager.getCity(allshi.get(j).getId());
                            for (int k = 0; k < allxian.size(); k++) {
                                district.add(allxian.get(k).getName());
                                districtMap.add(allxian.get(k).getId()+allxian.get(k).getName());
                            }
                            districts.add(district);
                            districtsMap.add(districtMap);
                        }
                        shi.add(cities);
                        xian.add(districts);
                        xianMap.add(districtsMap);
                    }
                    if(LocationOptions == null) {
                        LocationOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                            @Override
                            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                                SharedPrefUtil.putArea(context,xianMap.get(options1).get(option2).get(options3).substring(0,6));
                                tv_Location.setTvString(sheng.get(options1) + " " + shi.get(options1).get(option2) + " " + xian.get(options1).get(option2).get(options3));
                            }
                        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                            @Override
                            public void onOptionsSelectChanged(int options1, int options2, int options3) {

                            }
                        })
                        .setSubmitText("确定")//确定按钮文字
                        .setCancelText("取消")//取消按钮文字
                        .setTitleText("城市选择")//标题
                        .setSubCalSize(18)//确定和取消文字大小
                        .setTitleSize(20)//标题文字大小
                        .setTitleColor(Color.WHITE)//标题文字颜色
                        .setSubmitColor(Color.WHITE)//确定按钮文字颜色
                        .setCancelColor(Color.WHITE)//取消按钮文字颜色
                        .setTitleBgColor(0xFFff89a8)//标题背景颜色 Night mode
                        .setBgColor(0xFFffffff)//滚轮背景颜色 Night mode
                        .setContentTextSize(18)//滚轮文字大小
                        //.setLinkage(false)//设置是否联动，默认true
                        //.setLabels("省", "市", "区")//设置选择的三级单位
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .setCyclic(false, false, false)//循环与否
                        .setSelectOptions(1, 1, 1)  //设置默认选中项
                        .setOutSideCancelable(false)//点击外部dismiss default true
                        .isDialog(true)//是否显示为对话框样式
                        .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                        .build();
                        LocationOptions.setPicker(sheng, shi, xian);//添加数据源
                    }
                    LocationOptions.show();
                } else {
                    return;
                }
                break;
        }
    }

    /**
     * 获取验证码（登录注册共用一个接口）
     */
    private void getVerificationCode() {
        String phoneNum = et_phone.getINputStr();
        BaseRequest.getInstance()
        .getApiServise()
        .getCaptchaStatus(phoneNum)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BaseObserver<GetCaptchaStatusBean>(context) {
            @Override
            public void onSuccess(BaseReponse<GetCaptchaStatusBean> t) {
                // 保存 用户的注册状态
                SharedPrefUtil.putUserRegisterStatus(context, true);
            }
            @Override
            public void onCodeError(BaseReponse baseReponse) {
                //失败回调方法,可以直接在此更新ui,AndroidSchedulers.mainThread()表示切换到主线程
                ToastUtil.showToast(context,getString(R.string.verificationCode_eeror));
            }
            @Override
            public void onFailure(Throwable e, boolean netWork) throws Exception {

            }
        });
    }

    /**
     * 用户用来注册或者登录
     */
    private void registerOrLogin() {
        String phoneNum = et_phone.getINputStr();
        String verificationCode = et_verificationCode.getText().toString().trim();
        HashMap<String, String> map = new HashMap<>();
        map.put("PhoneNumber", phoneNum);
        map.put("code", verificationCode);
        BaseRequest.getInstance()
        .getApiServise()
        .getUserId(map)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BaseObserver<UserIdBean>(context) {
            @Override
            public void onSuccess(BaseReponse<UserIdBean> t) {
                //成功回调方法,可以直接在此更新ui,AndroidSchedulers.mainThread()表示切换到主线
                String sessionId = t.getData().getSessionId();
                String userId = t.getData().getUserId();
                // 将服务器返回的唯一的sessionId存储起来
                if(!StringUtil.isStrNull(sessionId)){
                    SharedPrefUtil.putSessionId(context, sessionId);
                    rlRegister.setVisibility(View.GONE);
                    initData();
                    llPersonal.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCodeError(BaseReponse baseReponse) {
                //失败回调方法,可以直接在此更新ui,AndroidSchedulers.mainThread()表示切换到主线程
            }
            @Override
            public void onFailure(Throwable e, boolean netWork) throws Exception {
            }
        });
    }

    /**
     * 获取个人基本信息
     */
    private void getPersonalBasicInfo(){
        String sessionId = SharedPrefUtil.getSessionId(context);
        if (!StringUtil.isStrNull(sessionId)) {
            if (!NetworkUtils.isNetWorkAvailable(getActivity())) {
                ToastUtil.showToast(context,getString(R.string.show_NetWork));
                return;
            }
            BaseRequest.getInstance()
            .getApiServise()
            .getUserInfo(sessionId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseObserver<GetUserInfoBean>(context) {
                @Override
                public void onSuccess(BaseReponse<GetUserInfoBean> t) {
                    //成功回调方法,可以直接在此更新ui,AndroidSchedulers.mainThread()表示切换到主线
                    getUserInfoBean = t.getData();
                    if(getUserInfoBean != null){
                        parseBasicJson(getUserInfoBean);
                    }
                }
                @Override
                public void onCodeError(BaseReponse baseReponse) {
                    //失败回调方法,可以直接在此更新ui,AndroidSchedulers.mainThread()表示切换到主线程
                }
                @Override
                public void onFailure(Throwable e, boolean netWork) throws Exception {
                }
            });
        }
    }
    /**
     * 解析个人基本信息
     * @param getUserInfoBean
     */
    private void parseBasicJson(GetUserInfoBean getUserInfoBean) {
        Log.e("sessionId", "parseBasicJson: "+sessionId);
        String customerId = getUserInfoBean.getCustomerId();
        String mobileNumber = getUserInfoBean.getMobileNumber();
        String userName = getUserInfoBean.getName();
        String birthday = getUserInfoBean.getBirthday();
        String regionCode = getUserInfoBean.getRegionCode();
        String regionName = getUserInfoBean.getRegionName();
        String doctorId = getUserInfoBean.getDoctorId();
        String pic = getUserInfoBean.getPic();
        String doctorName = getUserInfoBean.getDoctorName();
        String edc = getUserInfoBean.getEdc();
        String probeNo = getUserInfoBean.getProbeNo();//胎心仪设备号
        String bloodPressureNo = getUserInfoBean.getBloodPressureNo();//血压仪设备号
        String weightNo = getUserInfoBean.getWeightNo();//体重仪设备号
        String urinalysisNo = getUserInfoBean.getUrinalysisNo();//尿检仪设备号
        String doctorStatus = getUserInfoBean.getDoctorStatus();
        String registerNo = getUserInfoBean.getRegisterNo();
        String filingTime = getUserInfoBean.getFilingTime();
        String hospital = getUserInfoBean.getHospital();
        if(!StringUtil.isStrNull(pic)){
            Glide.with(context).load(pic).into(img_icon);
        }
        tv_muserName.setText(userName);
        tv_registrationID.setTvString(mobileNumber);
        tv_userName.setTvString(userName);
        tv_userName.setEtString(userName);
        if (birthday.length() > 9) {
            tv_birthday.setTvString(birthday.substring(0, birthday.indexOf(" ")));
        }
        if (edc.length() > 9) {
            tv_EDC.setTvString(edc.substring(0, edc.indexOf(" 0")));
            tv_userEDC.setText(edc.substring(0, edc.indexOf(" 0")));
            String substring = edc.substring(0, edc.indexOf(" 0"));
            String replaceStr = substring.replace('/', '-');
            SharedPrefUtil.putUserEDC(context,replaceStr);
        }
        tv_CASRN.setTvString(registerNo);
        tv_CASRN.setEtString(registerNo);
        if(!StringUtil.isStrNull(filingTime)){
            tv_fileCreat.setTvString(filingTime.substring(0, filingTime.indexOf(" 0")));
        }
        // 所在地的code ——> 所在地对应的地名
        String userAreaName = null;
        if (!StringUtil.isStrNull(regionCode)) {
            SharedPrefUtil.putArea(context, regionCode);
            userAreaName = codeToAreaName(regionCode);
            tv_Location.setTvString(userAreaName);
        }
        tv_hospital.setTvString(hospital);
        tv_doctor.setTvString(doctorName);

        //四种仪器设备号存储
        if (!StringUtil.isStrNull(probeNo)) {
            SharedPrefUtil.putUserProbeNo(context, probeNo);
            SharedPrefUtil.putActivateState(context, true);
        } else {
            SharedPrefUtil.putUserProbeNo(context, "");
            SharedPrefUtil.putActivateState(context, false);
        }
        if (StringUtil.isStrNull(bloodPressureNo)) {
            SharedPrefUtil.putUserXueyaNo(context, "");
        } else {
            SharedPrefUtil.putUserXueyaNo(context, bloodPressureNo);
        }
        if (StringUtil.isStrNull(weightNo)) {
            SharedPrefUtil.putUserTizhongNo(context, "");
        } else {
            SharedPrefUtil.putUserTizhongNo(context, weightNo);
        }
        if (StringUtil.isStrNull(urinalysisNo)) {
            SharedPrefUtil.putUserNiaoJianNo(context, "");
        } else {
            SharedPrefUtil.putUserNiaoJianNo(context, urinalysisNo);
        }

        if (doctorId == null || doctorId.equals("")) {
            zhidingdoctor.setTvString("您当前未指定医生，默认有平台医师为你服务");
        } else if (doctorStatus.equals("1")) {
            zhidingdoctor.setTvString("您所指定的医生为");
        } else if (doctorStatus.equals("0")) {
            zhidingdoctor.setTvString("系统已向您所指定的医生发出请求，等待对方响应，在这个过程中仍由我们的医师为您服务");
        } else if (doctorStatus.equals("-1")) {
            zhidingdoctor.setTvString("您指定的医生拒绝了您的请求，您可以重新选择,在未选择之前将由我们的医师为您服务");
        } else if (doctorStatus.equals("-2")) {
            zhidingdoctor.setTvString("您上次指定的医生未及时相应您的请求，您可以重新选择,在未选择之前将由我们的医师为您服务。");
        }
    }


    /**
     * 保存用户基本信息
     */
    private void keepBasicInfo() {
        //可以单独修改个人信息
        String sessionId = SharedPrefUtil.getSessionId(context);
        if (StringUtil.isStrNull(sessionId)) {
            ToastUtil.showToast(context, "请先注册！");
        } else if (StringUtil.isStrNull(tv_userName.getEtString())) {
            ToastUtil.showToast(context, getString(R.string.account_name_null));
        } else if (TextUtils.isEmpty(tv_birthday.getTvString())) {
            ToastUtil.showToast(context, getString(R.string.birthday_null));
        } else if (TextUtils.isEmpty(tv_EDC.getTvString())) {
            ToastUtil.showToast(context, getString(R.string.child_birth_null));
        } else if (TextUtils.isEmpty(SharedPrefUtil.getArea(context))) {
            //选择所在地不合法，这里是为什么呢
            ToastUtil.showToast(context, getString(R.string.area_null));
        } else if (!NetworkUtils.isNetWorkAvailable(getActivity())) {
            ToastUtil.showToast(context,getString(R.string.show_NetWork));
        } else {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String selectType = "0";//0是预产期，1是末次月经期
                        String userCityCode = SharedPrefUtil.getArea(context);
                        String body = "sessionId=" + URLEncoder.encode(SharedPrefUtil.getSessionId(context),"UTF-8")
                                + "&realName=" + URLEncoder.encode(tv_userName.getEtString(),"UTF-8")
                                + "&selectType=" + URLEncoder.encode(selectType,"UTF-8")
                                + "&birthday=" + URLEncoder.encode(tv_birthday.getTvString(),"UTF-8")
                                + "&childBirth=" + URLEncoder.encode(tv_EDC.getTvString(),"UTF-8")
                                + "&cityCode=" + URLEncoder.encode(userCityCode,"UTF-8")
                                + "&registerNo=" + URLEncoder.encode(tv_CASRN.getEtString(),"UTF-8")
                                + "&filingTime=" + URLEncoder.encode(tv_fileCreat.getTvString(),"UTF-8");
                        HttpURLConnection httpURLConnection = null;
                        httpURLConnection = HttpsURLConnectionUtil.CreatePostHttpConnection(SharedPrefUtil.getSessionId(context), MyConstant.SERVERS_URL, "application/x-www-form-urlencoded");
                        HttpsURLConnectionUtil.setBodyParameter(body, httpURLConnection);
                        String result = HttpsURLConnectionUtil.returnResult(httpURLConnection);
                        JSONObject myBasicJson = JSON.parseObject(result);
                        // 子线程执行完毕的地方，利用主线程的handler发送消息
                        Message msg = new Message();
                        msg.obj = myBasicJson;
                        keepBasicInfoHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }
    private Handler keepBasicInfoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            JSONObject myBasicJson = (JSONObject) msg.obj;
            int statusCode = myBasicJson.getIntValue("StatusCode");
            if(statusCode == 200){
                ToastUtil.showToast(context, getString(R.string.keep_sucessed));
                // 保存用户所填信息成功后,重新获取个人基本信息
                getPersonalBasicInfo();
            }else{
                ToastUtil.showToast(context, myBasicJson.getString("msg"));
            }
        }
    };


    /**
     * 获取个人账户信息
     */
    private void getAccountInfo() {
        String sessionId = SharedPrefUtil.getSessionId(context);
        if (!StringUtil.isStrNull(sessionId)) {
            if (!NetworkUtils.isNetWorkAvailable(getActivity())) {
                ToastUtil.showToast(context,getString(R.string.show_NetWork));
                return;
            }
            BaseRequest.getInstance()
            .getApiServise()
            .getUserAccountInfo(sessionId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseObserver<GetAccountBean>(context) {
                @Override
                public void onSuccess(BaseReponse<GetAccountBean> t) {
                    //成功回调方法,可以直接在此更新ui,AndroidSchedulers.mainThread()表示切换到主线
                    GetAccountBean getAccountBean = t.getData();
                    if(getAccountBean != null){
                        parseAccountJson(getAccountBean);
                    }
                }
                @Override
                public void onCodeError(BaseReponse baseReponse) {
                    //失败回调方法,可以直接在此更新ui,AndroidSchedulers.mainThread()表示切换到主线程
                }
                @Override
                public void onFailure(Throwable e, boolean netWork) throws Exception {
                }
            });
        }
    }
    /**
     * 解析个人账户信息
     */
    private void parseAccountJson(GetAccountBean getAccountBean) {
        tv_nowForegift.setTvString(getAccountBean.getDeposit()+"");
        tv_nowPay.setTvString(getAccountBean.getExpenditure()+"");
        tv_nowPrize.setTvString(getAccountBean.getReward()+"");
        tv_nowBalance.setTvString(getAccountBean.getBalance()+"");
    }


    private void requestPermission(String... permissions) {
        AndPermission.with(this)
        .runtime()
        .permission(permissions)
        .onGranted(new Action<List<String>>() {
            @Override
            public void onAction(List<String> permissions) {
                showSelectPictureMenu();
            }
        })
        .onDenied(new Action<List<String>>() {
            @Override
            public void onAction(@NonNull List<String> permissions) {
                Toast.makeText(getActivity(),getString(R.string.message_permission_Failure),Toast.LENGTH_SHORT).show();
                if (AndPermission.hasAlwaysDeniedPermission(getActivity(), permissions)) {
                    showSettingDialog(getActivity(), permissions);
                }
            }
        })
        .start();
    }
    public void showSettingDialog(Context context, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));
        new AlertDialog.Builder(context)
        .setCancelable(false)
        .setTitle(R.string.message_permission_qq)
        .setMessage(message)
        .setPositiveButton(R.string.message_permission_system, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setPermission();
            }
        })
        .setNegativeButton(R.string.message_permission_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
        .show();
    }
    private void setPermission() {
        AndPermission.with(context)
        .runtime()
        .setting()
        .onComeback(new Setting.Action() {
            @Override
            public void onAction() {

            }
        })
        .start();
    }

    /**
     * 弹出选择照片菜单
     */
    public void showSelectPictureMenu() {
        if(mCameraFile==null)
            mCameraFile = new File(Environment.getExternalStorageDirectory(), TMP_PATH);//照相机
        new SelectDialog(context)
        .builder()
        .setCancelable(true)
        .setTitle("请选择操作")
        .setCanceledOnTouchOutside(true)
        .addSelectItem("相机", SelectDialog.SelectItemColor.Green,
                new SelectDialog.OnSelectItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        startCamera();
                    }
                })
        .addSelectItem("图库", SelectDialog.SelectItemColor.Green,
                new SelectDialog.OnSelectItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        cameraPic();
                    }
                }).show();
    }

    /*
     * 打开相机
     * */
    private void startCamera() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0及以上
            Uri uriForFile = FileProvider.getUriForFile(getActivity(), "com.yunshengbao.fileprovider", mCameraFile);
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
            intentFromCapture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentFromCapture.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                    Environment.getExternalStorageDirectory(), TMP_PATH)));
        }
        startActivityForResult(intentFromCapture, CAMERA_WITH_DATA);
    }

    /**
     * 打开相册
     */
    private void cameraPic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果大于等于7.0使用FileProvider
            Uri uriForFile = FileProvider.getUriForFile(getActivity(), "com.yunshengbao.fileprovider", mCameraFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, SELECT_PIC_KITKAT);
        } else {
            startActivityForResult(intent, IMAGE_REQUEST_CODE);
        }

    }

    /**
     * 裁剪图片方法实现
     *
     * @param inputUri
     */
    public void startPhotoZoom(Uri inputUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri outPutUri = Uri.fromFile(mCameraFile);
            intent.setDataAndType(inputUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            Uri outPutUri = Uri.fromFile(mCameraFile);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String url = GetImagePath.getPath(context, inputUri);//这个方法是处理4.4以上图片返回的Uri对象不同的处理方法
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            } else {
                intent.setDataAndType(inputUri, "image/*");
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        }
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        startActivityForResult(intent, FLAG_MODIFY_FINISH);//这里就将裁剪后的图片的Uri返回了
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLAG_MODIFY_FINISH && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                Bitmap b = BitmapFactory.decodeFile(mCameraFile.getPath());
                img_icon.setImageBitmap(b);
                if (SharedPrefUtil.getSessionId(context) != null) {
                    upLoadHeadImage(Base64.encodeToString(Bitmap2Bytes(b), 0));
                }
            }
        }
        switch (requestCode) {
            case CAMERA_WITH_DATA:
                if (resultCode == -1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri inputUri = FileProvider.getUriForFile(getActivity(), "com.yunshengbao.fileprovider", mCameraFile);//通过FileProvider创建一个content类型的Uri
                        startPhotoZoom(inputUri);//设置输入类型
                    } else {
                        Uri inputUri = Uri.fromFile(mCameraFile);
                        startPhotoZoom(inputUri);//设置输入类型
                    }
                }
            break;
            case IMAGE_REQUEST_CODE://版本<7.0  图库后返回
                if(data != null){
                    startPhotoZoom(data.getData());
                }
                break;
            case SELECT_PIC_KITKAT://版本>= 7.0
                File imgUri = new File(GetImagePath.getPath(context, data.getData()));
                Uri dataUri = FileProvider.getUriForFile(getActivity(), "com.yunshengbao.fileprovider", imgUri);
                startPhotoZoom(dataUri);
                break;
        }
    }

    /**
     * 上传头像
     * @param picData
     */
    private void upLoadHeadImage(String picData) {
        String sessionId = SharedPrefUtil.getSessionId(context);
        if (!StringUtil.isStrNull(sessionId)) {
            if (!NetworkUtils.isNetWorkAvailable(getActivity())) {
                ToastUtil.showToast(context,getString(R.string.show_NetWork));
                return;
            }
            HashMap<String, String> map = new HashMap<>();
            map.put("sessionId",sessionId);
            map.put("picData",picData);
            BaseRequest.getInstance()
            .getApiServise()
            .setUserPic(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseObserver<SetUserPicBean>(context) {
                @Override
                public void onSuccess(BaseReponse<SetUserPicBean> t) {
                    //成功回调方法,可以直接在此更新ui,AndroidSchedulers.mainThread()表示切换到主线
                    ToastUtil.showToast(context,getString(R.string.imgIcon_success));
                }
                @Override
                public void onCodeError(BaseReponse baseReponse) {
                    //失败回调方法,可以直接在此更新ui,AndroidSchedulers.mainThread()表示切换到主线程
                }
                @Override
                public void onFailure(Throwable e, boolean netWork) throws Exception {
                }
            });
        }
    }


    /**
     * 根据地区码查找数据库，找出对应的地区名
     * @param userAreaCode
     * @return
     */
    private String codeToAreaName(String userAreaCode) {
        // 先在手机中拷贝一份关于地区的数据库
        file = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/" + context.getPackageName() + "/city.db");
        saveToDatabases();

        // 转换地区码
        mCityManager = new CityManager(file.getAbsolutePath());
        CityEntity currentCity = mCityManager.getCurrentCityName(userAreaCode);
        return currentCity.getAreaName();
    }

    private void saveToDatabases() {
        if (!file.exists()) {
            FileUtils.copyToFile(getResources().openRawResource(R.raw.city), file);
        }
    }


    //弹出软键盘
    private void showSoftInputFromWindow(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}

