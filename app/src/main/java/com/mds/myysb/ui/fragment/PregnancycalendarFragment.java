package com.mds.myysb.ui.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.mds.myysb.R;
import com.mds.myysb.bean.SetEDCBean;
import com.mds.myysb.net.BaseObserver;
import com.mds.myysb.net.BaseReponse;
import com.mds.myysb.net.BaseRequest;
import com.mds.myysb.ui.activity.NounInterpretationActivity;
import com.mds.myysb.ui.activity.WeightManagementActivity;
import com.mds.myysb.utils.BetWeenDaysUtil;
import com.mds.myysb.utils.ChangerDateUtil;
import com.mds.myysb.utils.DateFormatUtils;
import com.mds.myysb.utils.NetworkUtils;
import com.mds.myysb.utils.SharedPrefUtil;
import com.mds.myysb.utils.StringUtil;
import com.mds.myysb.utils.ToastUtil;
import com.mds.myysb.widget.ChooseYCQCustomDatePicker;
import com.mds.myysb.widget.CustomDatePicker;
import com.mds.myysb.widget.EDCNoteDialog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 孕生日历页面
 */

public class PregnancycalendarFragment extends Fragment implements View.OnClickListener, CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener {

    private View view;
    //选择预产期页面
    private LinearLayout ll_antenatal_frame_time;
    private RelativeLayout rl_ycq;
    private TextView tv_ycq;
    private RelativeLayout rl_mcyj;
    private TextView tv_mcyj;
    private Spinner spinner_yiyuan;
    private Spinner spinner_doctor;
    private TextView tv_EDCNote;
    private Button btn_sure;
    //孕生日历页面
    private LinearLayout ll_PregnancyCalendar;
    private TextView tv_date;
    private Button btn_today;
    private CalendarView calendarView;
    private LinearLayout ll_NounInterpretation;
    private RelativeLayout rl_WeightManagement;
    private TextView tv_EveryDayBaby;
    private TextView tv_EveryDayMum;
    private ScrollView srollView;


    private Context context;
    private String sessionId;
    private String EDC = "";
    private String yiyuanString;
    private String doctorString;
    private long beginTimestamp;
    private CustomDatePicker mDatePicker;

    private String lastYueJingDateString;
    private String yuchanqiDateString;
    private ChooseYCQCustomDatePicker chooseYCQCustomDatePicker;
    private EDCNoteDialog edcNoteDialog;
    private DatePickerDialog datePickerDialog;

    private int year = 2019, month, day;

    public static PregnancycalendarFragment newInstance() {
        PregnancycalendarFragment fragment = new PregnancycalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        sessionId = SharedPrefUtil.getSessionId(context);
        view = inflater.inflate(R.layout.fragment_pregnancycalendar, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ll_antenatal_frame_time = (LinearLayout) view.findViewById(R.id.ll_antenatal_frame_time);
        ll_PregnancyCalendar = (LinearLayout) view.findViewById(R.id.ll_PregnancyCalendar);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPersonalBasicInfotoEDC();
    }

    private void getPersonalBasicInfotoEDC() {
        EDC = SharedPrefUtil.getUserEDC(context);
        initIsShowFragment();
    }

    private void initIsShowFragment() {
        if(StringUtil.isStrNull(sessionId) && StringUtil.isStrNull(EDC)){
            ll_PregnancyCalendar.setVisibility(View.GONE);
            ll_antenatal_frame_time.setVisibility(View.VISIBLE);
            initChooseEDCView();
            initDatePicker();
        }else if(!StringUtil.isStrNull(sessionId) && !StringUtil.isStrNull(EDC)){
            ll_antenatal_frame_time.setVisibility(View.GONE);
            ll_PregnancyCalendar.setVisibility(View.VISIBLE);
            initavigationView();
        }else if(!StringUtil.isStrNull(sessionId) && StringUtil.isStrNull(EDC)){
            ll_PregnancyCalendar.setVisibility(View.GONE);
            ll_antenatal_frame_time.setVisibility(View.VISIBLE);
            initChooseEDCView();
            initDatePicker();
        }
    }

    private void initavigationView() {
        //孕生日历页面
        tv_date = (TextView) view.findViewById(R.id.tv_date);
        btn_today = (Button) view.findViewById(R.id.btn_today);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        ll_NounInterpretation = (LinearLayout) view.findViewById(R.id.ll_NounInterpretation);
        rl_WeightManagement = (RelativeLayout) view.findViewById(R.id.rl_WeightManagement);
        tv_EveryDayBaby = (TextView) view.findViewById(R.id.tv_EveryDayBaby);
        tv_EveryDayMum = (TextView) view.findViewById(R.id.tv_EveryDayMum);
        srollView = (ScrollView) view.findViewById(R.id.srollView);
        tv_date.setOnClickListener(this);
        btn_today.setOnClickListener(this);
        ll_NounInterpretation.setOnClickListener(this);
        rl_WeightManagement.setOnClickListener(this);
        calendarView.setOnCalendarSelectListener(this);
        calendarView.setOnYearChangeListener(this);
        tv_date.setText(String.valueOf(calendarView.getCurYear())+"年"+calendarView.getCurMonth() + "月");
        getRiLiYunQiDate();
    }

    private void getRiLiYunQiDate() {
        if(StringUtil.isStrNull(EDC)){
            Map<String, Calendar> map = new HashMap<>();
            try {
                Date yunWanDate = ChangerDateUtil.getDateBefore(ChangerDateUtil.StringToDate(yuchanqiDateString), 91);
                List<String> yunWanList = BetWeenDaysUtil.betweenDays1(BetWeenDaysUtil.dateToLong(yunWanDate), BetWeenDaysUtil.dateToStamp(yuchanqiDateString));
                for (int i = 0; i < yunWanList.size(); i++) {
                    int year = Integer.parseInt(yunWanList.get(i).substring(0, 4));
                    int month = Integer.parseInt(yunWanList.get(i).substring(5, 7));
                    int day = Integer.parseInt(yunWanList.get(i).substring(8));
                    map.put(getSchemeCalendar(year, month, day, 0xFF13acf0, "晚").toString(),
                            getSchemeCalendar(year, month, day, 0xFF13acf0, "晚"));
                }
                Date yunZhongDate = ChangerDateUtil.getDateBefore(yunWanDate, 98);
                List<String> yunZhongList = BetWeenDaysUtil.betweenDays1(BetWeenDaysUtil.dateToLong(yunZhongDate), BetWeenDaysUtil.dateToLong(yunWanDate));
                for (int i = 0; i < yunZhongList.size(); i++) {
                    int year = Integer.parseInt(yunZhongList.get(i).substring(0, 4));
                    int month = Integer.parseInt(yunZhongList.get(i).substring(5, 7));
                    int day = Integer.parseInt(yunZhongList.get(i).substring(8));
                    map.put(getSchemeCalendar(year, month, day, 0xFFbc13f0, "中").toString(),
                            getSchemeCalendar(year, month, day, 0xFFbc13f0, "中"));
                }
                Date yunZaoDate = ChangerDateUtil.getDateBefore(yunZhongDate, 90);
                List<String> yunZaoList = BetWeenDaysUtil.betweenDays1(BetWeenDaysUtil.dateToLong(yunZaoDate), BetWeenDaysUtil.dateToLong(yunZhongDate));
                for (int i = 0; i < yunZaoList.size(); i++) {
                    int year = Integer.parseInt(yunZaoList.get(i).substring(0, 4));
                    int month = Integer.parseInt(yunZaoList.get(i).substring(5, 7));
                    int day = Integer.parseInt(yunZaoList.get(i).substring(8));
                    map.put(getSchemeCalendar(year, month, day, 0xFFe69138, "早").toString(),
                            getSchemeCalendar(year, month, day, 0xFFe69138, "早"));
                }
                //此方法在巨大的数据量上不影响遍历性能，推荐使用
                calendarView.setSchemeDate(map);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            Map<String, Calendar> map = new HashMap<>();
            try {
                Date yunWanDate = ChangerDateUtil.getDateBefore(ChangerDateUtil.StringToDate(EDC), 91);
                List<String> yunWanList = BetWeenDaysUtil.betweenDays1(BetWeenDaysUtil.dateToLong(yunWanDate), BetWeenDaysUtil.dateToStamp(EDC));
                for (int i = 0; i < yunWanList.size(); i++) {
                    int year = Integer.parseInt(yunWanList.get(i).substring(0, 4));
                    int month = Integer.parseInt(yunWanList.get(i).substring(5, 7));
                    int day = Integer.parseInt(yunWanList.get(i).substring(8));
                    map.put(getSchemeCalendar(year, month, day, 0xFF13acf0, "晚").toString(),
                            getSchemeCalendar(year, month, day, 0xFF13acf0, "晚"));
                }
                Date yunZhongDate = ChangerDateUtil.getDateBefore(yunWanDate, 98);
                List<String> yunZhongList = BetWeenDaysUtil.betweenDays1(BetWeenDaysUtil.dateToLong(yunZhongDate), BetWeenDaysUtil.dateToLong(yunWanDate));
                for (int i = 0; i < yunZhongList.size(); i++) {
                    int year = Integer.parseInt(yunZhongList.get(i).substring(0, 4));
                    int month = Integer.parseInt(yunZhongList.get(i).substring(5, 7));
                    int day = Integer.parseInt(yunZhongList.get(i).substring(8));
                    map.put(getSchemeCalendar(year, month, day, 0xFFbc13f0, "中").toString(),
                            getSchemeCalendar(year, month, day, 0xFFbc13f0, "中"));
                }
                Date yunZaoDate = ChangerDateUtil.getDateBefore(yunZhongDate, 90);
                List<String> yunZaoList = BetWeenDaysUtil.betweenDays1(BetWeenDaysUtil.dateToLong(yunZaoDate), BetWeenDaysUtil.dateToLong(yunZhongDate));
                for (int i = 0; i < yunZaoList.size(); i++) {
                    int year = Integer.parseInt(yunZaoList.get(i).substring(0, 4));
                    int month = Integer.parseInt(yunZaoList.get(i).substring(5, 7));
                    int day = Integer.parseInt(yunZaoList.get(i).substring(8));
                    map.put(getSchemeCalendar(year, month, day, 0xFFe69138, "早").toString(),
                            getSchemeCalendar(year, month, day, 0xFFe69138, "早"));
                }
                //此方法在巨大的数据量上不影响遍历性能，推荐使用
                calendarView.setSchemeDate(map);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }

    private void initDatePicker() {
        Date nowDate = new Date(System.currentTimeMillis());
        Date dateBefore = ChangerDateUtil.getDateBefore(nowDate, 280);
        String dateBeforeString = ChangerDateUtil.DateToString(dateBefore);

        Date dateAfter = ChangerDateUtil.getDateAfter(nowDate, 280);
        String dateAfterString = ChangerDateUtil.DateToString(dateAfter);

        beginTimestamp = DateFormatUtils.str2Long(dateBeforeString, false);
        long endTimestamp = DateFormatUtils.str2Long(dateAfterString, false);

        // 通过时间戳初始化日期，毫秒级别  末次月经日期选择
        mDatePicker = new CustomDatePicker(context, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                lastYueJingDateString = DateFormatUtils.long2Str(timestamp, false);
                Date lastYueJingDate = ChangerDateUtil.StringToDate(lastYueJingDateString);
                Date yuchanqiDate = ChangerDateUtil.getDateAfter(lastYueJingDate, 280);
                yuchanqiDateString = ChangerDateUtil.DateToString(yuchanqiDate);

                //获取当前时间
                Date nowDate = new Date(System.currentTimeMillis());
                Date dateAfter = ChangerDateUtil.getDateAfter(nowDate, 280);
                Date date = ChangerDateUtil.StringToDate(yuchanqiDateString);
                if (date.getTime() > nowDate.getTime() && date.getTime() <= dateAfter.getTime()) {
                    //在TextView上显示日期
                    tv_mcyj.setText("预产期:"+ yuchanqiDateString);
                } else {
                    yuchanqiDateString = null;
                    Toast.makeText(context, "请选择正确的预产期", Toast.LENGTH_SHORT).show();
                    tv_mcyj.setText("不知道,计算预产期");
                }
            }
        }, beginTimestamp, endTimestamp);
        // 允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(true);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);

        // 通过时间戳初始化日期，毫秒级别  预产期日期选择
        chooseYCQCustomDatePicker = new ChooseYCQCustomDatePicker(context, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                yuchanqiDateString = DateFormatUtils.long2Str(timestamp, false);
                //获取当前时间
                Date nowDate = new Date(System.currentTimeMillis());
                Date dateAfter = ChangerDateUtil.getDateAfter(nowDate, 280);
                Date date = ChangerDateUtil.StringToDate(yuchanqiDateString);
                if (date.getTime() > nowDate.getTime() && date.getTime() <= dateAfter.getTime()) {
                    //在TextView上显示日期
                    tv_ycq.setText("预产期:"+DateFormatUtils.long2Str(timestamp, false));
                    Date yushanqiDate = ChangerDateUtil.StringToDate(yuchanqiDateString);
                    Date lastYueJingDate = ChangerDateUtil.getDateBefore(yushanqiDate, 280);
                    lastYueJingDateString = ChangerDateUtil.DateToString(lastYueJingDate);
                } else {
                    yuchanqiDateString = null;
                    Toast.makeText(context, "请选择正确的预产期", Toast.LENGTH_SHORT).show();
                    tv_ycq.setText("我知道预产期");
                }

            }
        }, beginTimestamp, endTimestamp);
        // 允许点击屏幕或物理返回键关闭
        chooseYCQCustomDatePicker.setCancelable(true);
        // 不显示时和分
        chooseYCQCustomDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        chooseYCQCustomDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        chooseYCQCustomDatePicker.setCanShowAnim(false);
    }

    private void initChooseEDCView() {
        //选择预产期页面
        rl_ycq = (RelativeLayout) view.findViewById(R.id.rl_ycq);
        tv_ycq = (TextView) view.findViewById(R.id.tv_ycq);
        rl_mcyj = (RelativeLayout) view.findViewById(R.id.rl_mcyj);
        tv_mcyj = (TextView) view.findViewById(R.id.tv_mcyj);
        spinner_yiyuan = (Spinner) view.findViewById(R.id.spinner_yiyuan);
        spinner_doctor = (Spinner) view.findViewById(R.id.spinner_doctor);
        tv_EDCNote = (TextView) view.findViewById(R.id.tv_EDCNote);
        btn_sure = (Button) view.findViewById(R.id.btn_sure);
        rl_ycq.setOnClickListener(this);
        rl_mcyj.setOnClickListener(this);
        tv_EDCNote.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        ArrayAdapter<String> YiyuanAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_item, getYiYuanData());
        ArrayAdapter<String> DoctorAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_item, getDoctorData());
        spinner_yiyuan.setAdapter(YiyuanAdapter);
        spinner_doctor.setAdapter(DoctorAdapter);
        spinner_yiyuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yiyuanString = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doctorString = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private List<String> getYiYuanData(){
        // 医院数据源
        List<String> yiyuanDataList = new ArrayList<String>();
        yiyuanDataList.add("北京妇产医院");
        return yiyuanDataList;
    }
    private List<String> getDoctorData(){
        // 医院数据源
        List<String> doctorDataList = new ArrayList<String>();
        doctorDataList.add("张为远");
        doctorDataList.add("王欣");
        doctorDataList.add("王琪");
        doctorDataList.add("张新阳");
        doctorDataList.add("李扬");
        doctorDataList.add("丁新");
        doctorDataList.add("赵友萍");
        doctorDataList.add("陈奕");
        doctorDataList.add("周莉");
        doctorDataList.add("李雪艳");
        doctorDataList.add("申南");
        doctorDataList.add("黄醒华");
        doctorDataList.add("李光辉");
        doctorDataList.add("山丹");
        doctorDataList.add("夏晓艳");
        return doctorDataList;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_ycq:
                chooseYCQCustomDatePicker.show(DateFormatUtils.long2Str(beginTimestamp, false));
                break;
            case R.id.rl_mcyj:
                // 日期格式为yyyy-MM-dd
                mDatePicker.show(DateFormatUtils.long2Str(beginTimestamp, false));
                break;
            case R.id.tv_EDCNote:
                edcNoteDialog = new EDCNoteDialog(context);
                edcNoteDialog.setCancelable(true);
                edcNoteDialog.show();
                break;
            case R.id.btn_sure:
                if(TextUtils.isEmpty(sessionId) && TextUtils.isEmpty(EDC)){

                }else if(!TextUtils.isEmpty(sessionId) && !TextUtils.isEmpty(EDC)  && !TextUtils.isEmpty(yuchanqiDateString)){
                    upEDC(yuchanqiDateString);
                    ll_antenatal_frame_time.setVisibility(View.GONE);
                    ll_PregnancyCalendar.setVisibility(View.VISIBLE);
                    initavigationView();
                }else if(!TextUtils.isEmpty(sessionId) && TextUtils.isEmpty(EDC) && !TextUtils.isEmpty(yuchanqiDateString)){
                    upEDC(yuchanqiDateString);                    ll_antenatal_frame_time.setVisibility(View.GONE);
                    ll_PregnancyCalendar.setVisibility(View.VISIBLE);
                    initavigationView();
                }
                break;
            case R.id.tv_date:
                if(datePickerDialog == null) {
                    datePickerDialog = new DatePickerDialog(context, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {
                            //修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
                            year = myyear;
                            month = monthOfYear;
                            day = dayOfMonth;
                            //更新日期
                            updateDate();
                        }
                        //当DatePickerDialog关闭时，更新日期显示
                        private void updateDate() {
                            tv_date.setText(year + "年" + (month + 1)+"月");
                            calendarView.scrollToCalendar(year,month + 1,day);
                        }
                    }, year, month, day);
                }
                datePickerDialog.show();//显示DatePickerDialog组件
            case R.id.btn_today:
                calendarView.scrollToCurrent();
                break;
            case R.id.ll_NounInterpretation:
                Intent intent = new Intent(context, NounInterpretationActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_WeightManagement:
                Intent intent1 = new Intent(context, WeightManagementActivity.class);
                startActivity(intent1);
                break;
        }
    }

    /**
     * 上传预产期
     * @param yuchanqiDateString
     */
    private void upEDC(String yuchanqiDateString) {
        if (!StringUtil.isStrNull(sessionId)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("sessionId",sessionId);
            map.put("edc",yuchanqiDateString);
            if (!NetworkUtils.isNetWorkAvailable(getActivity())) {
                ToastUtil.showToast(context,getString(R.string.show_NetWork));
                return;
            }
            BaseRequest.getInstance()
            .getApiServise()
            .setEDC(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseObserver<SetEDCBean>(context) {
                @Override
                public void onSuccess(BaseReponse<SetEDCBean> t) {
                    //成功回调方法,可以直接在此更新ui,AndroidSchedulers.mainThread()表示切换到主线
                    Message msg = new Message();
                    msg.what = 1;
                    EDCHandler.sendMessage(msg);
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
    private Handler EDCHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    SharedPrefUtil.putUserEDC(context,yuchanqiDateString);
                    break;
            }
        }
    };

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        tv_date.setText(String.valueOf(calendar.getYear())+"年"+calendar.getMonth() + "月");
    }

    @Override
    public void onYearChange(int year) {

    }


}
