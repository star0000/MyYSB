package com.mds.myysb.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.mds.myysb.R;
import com.mds.myysb.adapter.NewNotesAdapter;
import com.mds.myysb.base.BaseActivity;
import com.mds.myysb.bean.His;
import com.mds.myysb.bean.UploadMonitorFileBean;
import com.mds.myysb.net.BaseObserver;
import com.mds.myysb.net.BaseReponse;
import com.mds.myysb.net.BaseRequest;
import com.mds.myysb.utils.NetworkUtils;
import com.mds.myysb.utils.SharedPrefUtil;
import com.mds.myysb.utils.StringUtil;
import com.mds.myysb.utils.ToastUtil;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChooseUpFileType extends BaseActivity implements NewNotesAdapter.ImageCallback {

    public static final int UP_LOAD = 100;
    //	private Map<String, Object> map;
    private boolean isImageover=false;
    private Button btn_upload,btn_cancel;
    private CheckBox cb_file;
    private CheckBox cb_voice;
    private ImageView iv_close;
    private boolean isFile = true, isVoice=false;
    private String userID;
    private String monitorID;
    String file_voice,file_fhr;
    byte[] bytes = null;
    //	private SQLiteDatabase db;
    private String duringTime;
    private String starttime;
    private View progressView;
    private View dialog;

    byte[] bytepic;
    //	Cursor c;
    private His his;


    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_up_file_type);
        btn_upload=(Button) findViewById(R.id.id_btn_uploadfile);
        btn_cancel=(Button) findViewById(R.id.id_btn_upcancel);
        cb_file=(CheckBox) findViewById(R.id.id_checkBox_imagefile);
        cb_voice=(CheckBox) findViewById(R.id.id_checkBox_voicefile);
        iv_close=(ImageView) findViewById(R.id.id_iv_choosefiletype);
        progressView=findViewById(R.id.id_fragment_upfileprogress);
        dialog=findViewById(R.id.id_up_dialog);
        progressView.setVisibility(View.GONE);
        userID = SharedPrefUtil.getSessionId(ChooseUpFileType.this)+"";// 用户 测试id

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Baby/";
        String dbpath = path + "/Baby.db";
        Bundle bundle=this.getIntent().getExtras();
        monitorID=bundle.getString("monitor_id");
        his= DataSupport.where("monitor_id=?",monitorID).order("id desc").findFirst(His.class);
        if(his!=null){
            file_voice=his.getFile_voice();
            file_fhr=his.getFile_fhr();
            duringTime=his.getPeriod();
            starttime=his.getStarttime();

        }
        initEvent();
    }

    private void initEvent() {
        iv_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ChooseUpFileType.this.finish();
            }
        });
        // TODO Auto-generated method stub
        btn_upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!NetworkUtils.isNetWorkAvailable(ChooseUpFileType.this)){
                    Toast.makeText(ChooseUpFileType.this, getString(R.string.show_NetWork),Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (!isFile && !isVoice) {
                    Toast.makeText(ChooseUpFileType.this, "必须选中一个上传", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    try {
                        FileInputStream in = new FileInputStream(his.getFilename_thumbnail());
                        byte[] buffer = new byte[in.available()];
                        in.read(buffer);
                        Bitmap bitmap= BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                        bytepic=Bitmap2Bytes(bitmap);
                    } catch (java.io.FileNotFoundException e) {
                        e.printStackTrace();
                        bytepic=Bitmap2Bytes( BitmapFactory.decodeResource(ChooseUpFileType.this.getResources(),R.drawable.contacts_selected));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        bytepic=Bitmap2Bytes( BitmapFactory.decodeResource(ChooseUpFileType.this.getResources(),R.drawable.contacts_selected));
                        e.printStackTrace();
                    }
                    progressView.setVisibility(View.VISIBLE);
                    dialog.setVisibility(View.GONE);

                    int durationTime = Integer.parseInt(duringTime.substring(0, 1))
                            * 10
                            * 60
                            + Integer.parseInt(duringTime.substring(1, 2))
                            * 60
                            + Integer.parseInt(duringTime.substring(3, 4))
                            * 10
                            + Integer.parseInt(duringTime.substring(4, 5));// 监测时长
                    String endtime=getEndTime(starttime, durationTime);//结束时间

                    if(isFile && !isVoice){
                        bytes = getFile(file_fhr);
                        String baseStr = Base64.encodeToString(
                                bytes, 0);// baseString64转换；
                        baseStr.replace("+", "%2B");// 用%2B替换baseStr里面的"+"
                        //上传图像
                        uploadData(new String[]{userID,
                                monitorID,
                                starttime,
                                endtime,
                                durationTime+"",baseStr," ",Base64.encodeToString(bytepic, 0)});

                    }else if(!isFile && isVoice){
//						上传声音
//						同时上传
                        bytes = getFile(file_fhr);
                        String baseStr = Base64.encodeToString(
                                bytes, 0);// baseString64转换；
                        baseStr.replace("+", "%2B");// 用%2B替换baseStr里面的"+"

                        // 上传音频
                        byte[] bytes2 = getFile(file_voice);
                        String baseStr2 = Base64
                                .encodeToString(bytes2, 0);// baseString64转换；
                        baseStr2.replace("+", "%2B");// 用%2B替换baseStr里面的"+"

                        uploadData(new String[]{
                                userID,
                                monitorID,
                                starttime,
                                endtime,
                                durationTime+"",baseStr,baseStr2,Base64.encodeToString(bytepic, 0)});

                    }else{
//						同时上传
                        bytes = getFile(file_fhr);
                        String baseStr = Base64.encodeToString(
                                bytes, 0);// baseString64转换；
                        baseStr.replace("+", "%2B");// 用%2B替换baseStr里面的"+"

                        // 上传音频
                        byte[] bytes2 = getFile(file_voice);
                        String baseStr2 = Base64
                                .encodeToString(bytes2, 0);// baseString64转换；
                        baseStr2.replace("+", "%2B");// 用%2B替换baseStr里面的"+"

                        uploadData(new String[] {
                                userID,
                                monitorID,
                                starttime,
                                endtime,
                                durationTime+"",baseStr,baseStr2,Base64.encodeToString(bytepic, 0)});

                    }
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ChooseUpFileType.this.finish();
            }
        });


        //给CheckBox设置事件监听
        cb_file.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                isFile=isChecked;
            }
        });
        cb_voice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                isVoice=isChecked;
            }
        });
    }

    private byte[] getFile(String path) {
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getEndTime(String dateStart, int during){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 =format.parse(dateStart);
            long end=d1.getTime()+during*1000;
            Date endTime=new Date(end);
            return format.format(endTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

    //	上传监测额数据到服务器
    public void uploadData(String[] s){
        String sessionId = SharedPrefUtil.getSessionId(this);
        if (!StringUtil.isStrNull(sessionId)) {
            if (!NetworkUtils.isNetWorkAvailable(this)) {
                ToastUtil.showToast(this,getString(R.string.show_NetWork));
                return;
            }
            Map<String, String> map = new HashMap<>();
            map.put("sessionId",s[0]);
            map.put("monitorId",s[1]);
            map.put("startTime",s[2]);
            map.put("endTime",s[3]);
            map.put("durationTime",s[4]);
            map.put("data",s[5]);
            map.put("data2",s[6]);
            map.put("pic",s[7]);
            BaseRequest.getInstance()
                    .getApiServise()
                    .uploadMonitorFile(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<UploadMonitorFileBean>(this) {
                        @Override
                        public void onSuccess(BaseReponse<UploadMonitorFileBean> t) {
                            ToastUtil.showToast("上传成功", 0);
                            if(monitorID!=null) {
                                His hisss=new His();
                                hisss.setStatus(2);
                                hisss.updateAll("monitor_id=?",monitorID);
                            }
                            progressView.setVisibility(View.GONE);
                            ChooseUpFileType.this.finish();
                        }
                        @Override
                        public void onCodeError(BaseReponse baseReponse) {
                            Toast.makeText(ChooseUpFileType.this,"访问服务器失败:",Toast.LENGTH_SHORT).show();
                            progressView.setVisibility(View.GONE);
                            ChooseUpFileType.this.finish();                        }
                        @Override
                        public void onFailure(Throwable e, boolean netWork) throws Exception {
                            Toast.makeText(ChooseUpFileType.this,"访问服务器失败:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            progressView.setVisibility(View.GONE);
                            ChooseUpFileType.this.finish();
                        }
                    });
        }
    }


    @Override
    public void imageLoaded(boolean isSuccess, int id) {

    }

}
