package com.mds.myysb.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mds.myysb.R;
import com.mds.myysb.bean.His;
import com.mds.myysb.bean.UploadMonitorFileBean;
import com.mds.myysb.net.BaseObserver;
import com.mds.myysb.net.BaseReponse;
import com.mds.myysb.net.BaseRequest;
import com.mds.myysb.ui.activity.ChooseUpFileType;
import com.mds.myysb.utils.NetworkUtils;
import com.mds.myysb.utils.SharedPrefUtil;
import com.mds.myysb.utils.StringUtil;
import com.mds.myysb.utils.ToastUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import q.rorbin.badgeview.QBadgeView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewNotesAdapter extends BaseAdapter {

    private int thepostion;
    /**被绑定对象*/
    private ListView listView;

    private LayoutInflater mInflater;
    public List<Map<String, Object>> mList;
    private Context context;
    public static final int UP_LOAD = 100;

    private String userID;
    private String monitorID;
    private Map<String, Object> map;
    private boolean isFile = true, isVoice;
    private AlertDialog dialog_shangchuan, dialog_fenxiang;
    String fileType = "01";// 文件类型 ：01"：传.f文件、"02"：传.v文件
    byte[] bytes = null;
    private View progressView;
    /**Item对象集*/
    HashMap<String, Object> itemMap =  new HashMap<String, Object>();

    public NewNotesAdapter(Context context,
                           List<Map<String, Object>> newNotesList,View progressView,ListView listView) {
        this.listView=listView;
        this.context = context;
//		this.db = db;
        this.progressView=progressView;
        mInflater = LayoutInflater.from(context);
        if (newNotesList != null) {
            mList = newNotesList;
        } else {
            mList = new ArrayList<Map<String, Object>>();
        }
        userID = SharedPrefUtil.getSessionId(context)+"";// 用户 测试id
    }

    @Override
    public int getCount() {
        if(mList!=null){
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(mList==null){return null;}
        if(mList.size()==0){return null;}
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    View theView;
    public ImageView getImageViewByPosition(int position){
        if(theView!=null){
            ViewHolder holder=(ViewHolder) theView.getTag();
            return holder.up_load;
        }
        return null;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (getCount() == 0) {
            return null;
        }
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_item, null);

            holder = new ViewHolder();
            holder.badgeView=new QBadgeView(context);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.up_load = (ImageView) convertView
                    .findViewById(R.id.imageButton1);
            //holder.share = (ImageView) convertView.findViewById(R.id.imageButton2);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            //
            holder.baseline = (TextView) convertView
                    .findViewById(R.id.baseline);
            holder.acc = (TextView) convertView.findViewById(R.id.acc);
            holder.result = (TextView) convertView.findViewById(R.id.result);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.fm = (TextView) convertView.findViewById(R.id.fm);
            convertView.setTag(holder);
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 标记图片视图，注意不能放在上面
        holder.up_load.setTag(position);

        Bitmap bmp;
        try {
            bmp = (Bitmap) mList.get(position).get("img");

        } catch (Exception e) {
            // TODO Auto-generated catch block
//			Toast.makeText(context, "生成缩略图失败", 0).show();
            bmp = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.contacts_selected);
        }

        holder.img.setImageBitmap(bmp);

        if(mList.get(position).get("type").toString().equals("")){
//			还没有判读
            holder.acc.setText("");
            holder.baseline.setText("");
            holder.result.setText(mList.get(position).get("baseline").toString());
            holder.time.setText(mList.get(position).get("starttime").toString());
            holder.type.setText("等待医生判读");
            holder.fm.setText("");
        }else{
//			+ mList.get(position).get("baseline").toString()
            holder.baseline.setText(mList.get(position).get("Mean").toString());
            holder.acc.setText("加速"+mList.get(position).get("Acc").toString()+"次");

            holder.result.setText("减速"+mList.get(position).get("Dec").toString()+"次"
            );
            holder.time.setText(mList.get(position).get("starttime").toString());
            holder.type.setText(mList.get(position).get("type").toString());
            holder.fm.setText(mList.get(position).get("Fm").toString());
        }
        int count=Integer.parseInt((mList.get(position).get("MsgCount")+"").equals("null")?"0":(mList.get(position).get("MsgCount")+""));
        if(count>0){
            holder.badgeView.bindTarget(holder.img).setBadgeNumber(count);
        }else{
            holder.badgeView.hide(false);
        }




        holder.up_load.setImageResource((Integer) mList.get(position).get("imageButton1"));
        final int status=(Integer) mList.get(position).get("status");

        holder.up_load.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//				查询数据库，已经上传则返回
                if(status==2){
//					holder.up_load.setClickable(false);
                    Toast.makeText(context, "已经上传，请勿重复上传！",Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                thepostion=position;
//				Log.e("位置位置：", ""+position);
                // 初始化上传设置对话框
                AlertDialog.Builder builder_shangchuan = new AlertDialog.Builder(
                        context);
                builder_shangchuan.setTitle("是要上传监护记录吗？");

                String[] items_shangchuan = { "上传图像", "上传声音" };
                //
                boolean[] checkedItems = { isFile, isVoice };
                builder_shangchuan.setMultiChoiceItems(items_shangchuan,
                        checkedItems, new DialogMultiChoiceListener());

                builder_shangchuan.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // Toast.makeText(context,
                                // isFile+"-------"+isVoice, 1).show();
                                // 这里做上传处理，根据用户选择类型，这里上传时需要分别上传、
                                if(!NetworkUtils.isNetWorkAvailable(context)){
                                    Toast.makeText(context, "请检查网络",Toast.LENGTH_SHORT)
                                            .show();
                                    return;
                                }
                                if((SharedPrefUtil.getSessionId(context)+"").equals("null")){
                                    Toast.makeText(context, "上传前请到模块“我”注册登录",Toast.LENGTH_SHORT)
                                            .show();
                                    return;
                                }
                                if(userID.equals("null")){
                                    userID=SharedPrefUtil.getSessionId(context)+"";
                                }

                                if (!isFile && !isVoice) {
                                    Toast.makeText(context, "必须选中一个上传",Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    progressView.setVisibility(View.VISIBLE);
                                    // 蒋工的上传信息代码
                                    map = mList.get(position);
                                    monitorID = (String) map.get("monitor_id"); // 监测ID
                                    String starttime = (String) map.get("starttime"); // 监测开始时间
                                    String baseline_fromMap = (String) map.get("baseline");

                                    int durationTime = Integer.parseInt(baseline_fromMap.substring(0, 1))
                                            * 10
                                            * 60
                                            + Integer.parseInt(baseline_fromMap.substring(1, 2))
                                            * 60
                                            + Integer.parseInt(baseline_fromMap.substring(3, 4))
                                            * 10
                                            + Integer.parseInt(baseline_fromMap.substring(4, 5));// 监测时长
                                    String endtime=getEndTime(starttime, durationTime);//结束时间

                                    Bitmap pic=(Bitmap) map.get("img");

                                    String basePic= Base64.encodeToString(Bitmap2Bytes(pic), 0);

                                    if(isFile && !isVoice){
                                        String file_fhr = (String) map
                                                .get("file_fhr"); // .f文件
                                        bytes = getFile(file_fhr);
                                        if(bytes==null){
                                            Toast.makeText(context,"文件可能被删除了，无法上传",Toast.LENGTH_SHORT).show();
                                            progressView.setVisibility(View.GONE);
                                            return;
                                        }
                                        String baseStr = Base64.encodeToString(
                                                bytes, 0);// baseString64转换；
                                        baseStr.replace("+", "%2B");// 用%2B替换baseStr里面的"+"

                                        uploadData(new String[] {
                                                userID,
                                                monitorID,
                                                starttime,
                                                endtime,
                                                durationTime+"",baseStr,"",basePic});

                                    }else if(!isFile && isVoice){
//										上传声音
                                        // 上传音频
//										同时上传
                                        String file_fhr = (String) map
                                                .get("file_fhr"); // .f文件
                                        bytes = getFile(file_fhr);
                                        String baseStr = Base64.encodeToString(
                                                bytes, 0);// baseString64转换；
                                        baseStr.replace("+", "%2B");// 用%2B替换baseStr里面的"+"

                                        // 上传音频
                                        String file_voice = (String) map
                                                .get("file_voice"); // .v文件
                                        byte[] bytes2 = getFile(file_voice);
                                        String baseStr2 = Base64
                                                .encodeToString(bytes2, 0);// baseString64转换；
                                        baseStr2.replace("+", "%2B");// 用%2B替换baseStr里面的"+"

                                        uploadData(new String[] {
                                                userID,
                                                monitorID,
                                                starttime,
                                                endtime,
                                                durationTime+"",baseStr,baseStr2,basePic});

                                    }else{
//										同时上传
                                        String file_fhr = (String) map
                                                .get("file_fhr"); // .f文件
                                        bytes = getFile(file_fhr);
                                        String baseStr = Base64.encodeToString(
                                                bytes, 0);// baseString64转换；
                                        baseStr.replace("+", "%2B");// 用%2B替换baseStr里面的"+"

                                        // 上传音频
                                        String file_voice = (String) map
                                                .get("file_voice"); // .v文件
                                        byte[] bytes2 = getFile(file_voice);
                                        String baseStr2 = Base64
                                                .encodeToString(bytes2, 0);// baseString64转换；
                                        baseStr2.replace("+", "%2B");// 用%2B替换baseStr里面的"+"

                                        uploadData(new String[] {
                                                userID,
                                                monitorID,
                                                starttime,
                                                endtime,
                                                durationTime+"",baseStr,baseStr2,basePic});
                                    }


                                }

                                // ===
                            }
                        });
                builder_shangchuan.setNegativeButton("否", null);
                // 上传
                dialog_shangchuan = builder_shangchuan.create();
                WindowManager.LayoutParams lp = dialog_shangchuan.getWindow()
                        .getAttributes();
                lp.alpha = 0.9f;
                dialog_shangchuan.getWindow().setAttributes(lp);
                dialog_shangchuan.show();

            }
        });

        theView=convertView;
        itemMap.put(position+"", holder);
        return convertView;
    }
    //利用接口回调，更新图片UI
    public interface ImageCallback {
        public void imageLoaded(boolean isSuccess, int id);
    }
    public void loadImage(final int id,
                          final ImageCallback callback,boolean isSuccess){

        callback.imageLoaded(isSuccess, id);

    }
    ImageCallback imageCallback = new ImageCallback(){
        public void imageLoaded(boolean isSuccess, int position) {
            if (isSuccess) {
                //获取刚才标识的组件，并更新
                ImageView imageView = (ImageView) listView
                        .findViewWithTag(position);
                if (imageView != null) {
                    mList.get(position).put("imageButton1", R.drawable.list_a2);
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.list_a2));
                }
            }
        }
    };


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

    /*
     * 郭工改了数据库字段，直接提供了时长 private static String getDurationTime(String dateStart,
     * String dateStop) { try {
     *  Date d1 =
     *  Date d2 = format.parse(dateStop); // 毫秒ms long
     * diff = d2.getTime() - d1.getTime(); long sec=diff/ 1000/60; long
     * diffSeconds = diff / 1000 % 60; // LogUtil.info(NewNotesAdapter.class,
     * "监测时间秒：" + diffSeconds); return String.valueOf(60*sec+diffSeconds); }
     * catch (ParseException e) { e.printStackTrace(); } return "0"; }
     */
    private static String getEndTime(String dateStart, int during){
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

        return null;
    }
    private class ViewHolder {
        private ImageView img; // 缩略图
        private ImageView up_load; // 上传
        private ImageView share; // 分享
        /*
         * private TextView guid; private TextView info; private TextView data;
         * private TextView time;
         */
        private TextView time;
        private TextView baseline;
        private TextView acc;
        private TextView result;
        private TextView type;
        private TextView fm;
        QBadgeView badgeView;
    }
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    //
    private class DialogMultiChoiceListener implements
            DialogInterface.OnMultiChoiceClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            switch (which) {
                case 0:
                    isFile = isChecked;
                    break;
                case 1:
                    isVoice = isChecked;
                    break;

            }
        }
    }

    //	上传监测额数据到服务器
    public void uploadData(String[] s){

        String sessionId = SharedPrefUtil.getSessionId(context);
        if (!StringUtil.isStrNull(sessionId)) {
            if (!NetworkUtils.isNetWorkAvailable(context)) {

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
            .subscribe(new BaseObserver<UploadMonitorFileBean>(context) {
                @Override
                public void onSuccess(BaseReponse<UploadMonitorFileBean> t) {
                    ToastUtil.showToast("上传成功", 0);
                    progressView.setVisibility(View.GONE);
                  //				更新数据库，按钮置灰，不允许上传
                    if(monitorID!=null){
                        His his=new His();
                        his.setStatus(2);
                        his.updateAll("monitor_id =?",monitorID);
                    }

                    loadImage(thepostion, imageCallback, true);
                }
                @Override
                public void onCodeError(BaseReponse baseReponse) {
                    Toast.makeText(context,"访问服务器失败:",Toast.LENGTH_SHORT).show();
                                        }
                @Override
                public void onFailure(Throwable e, boolean netWork) throws Exception {
                    Toast.makeText(context,"访问服务器失败:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}
