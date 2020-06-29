package com.mds.myysb.net;

/*
 * 请求方法类
 */


import com.mds.myysb.bean.GetAccountBean;
import com.mds.myysb.bean.GetCaptchaStatusBean;
import com.mds.myysb.bean.GetUserInfoBean;
import com.mds.myysb.bean.SetEDCBean;
import com.mds.myysb.bean.SetUserPicBean;
import com.mds.myysb.bean.UploadMonitorFileBean;
import com.mds.myysb.bean.UserIdBean;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


public interface ApiServise {


    //线上测试
    String HOST = "http://114.215.142.138:8068/customer.asmx/";

    // 线上正式
   // String HOST = "http://server.mutaiwang.com/customer.asmx/";


    /**
     * 获取验证码的方法名---CheckCaptcha
     */
    @FormUrlEncoded
    @POST("GetCaptchaStatus")
    Observable<BaseReponse<GetCaptchaStatusBean>> getCaptchaStatus(@Field("PhoneNumber") String PhoneNumber);

    /**
     * 登录/注册的方法名---CheckCaptcha
     */
    @FormUrlEncoded
    @POST("CheckCaptcha")
    Observable<BaseReponse<UserIdBean>> getUserId(@FieldMap Map<String,String> map);

    /**
     * 获取个人基本信息的方法名---GetUserInfo
     */
    @FormUrlEncoded
    @POST("GetUserInfo")
    Observable<BaseReponse<GetUserInfoBean>> getUserInfo(@Field("sessionId") String sessionId);

    /**
     * 获取个人账户信息的方法名---GetUserAccountInfo
     */
    @FormUrlEncoded
    @POST("GetUserAccountInfo")
    Observable<BaseReponse<GetAccountBean>> getUserAccountInfo(@Field("sessionId") String sessionId);

    /**
     * 上传头像的方法名---SetUserPic
     */
    @FormUrlEncoded
    @POST("SetUserPic")
    Observable<BaseReponse<SetUserPicBean>> setUserPic(@FieldMap Map<String,String> map);

    /**
     * 上传预产期的方法名---SetEDC
     */
    @FormUrlEncoded
    @POST("SetEDC")
    Observable<BaseReponse<SetEDCBean>> setEDC(@FieldMap Map<String,String> map);

    /**
     * 上传胎心检测数据的方法名---UploadMonitorFile
     */
    @FormUrlEncoded
    @POST("UploadMonitorFile")
    Observable<BaseReponse<UploadMonitorFileBean>> uploadMonitorFile(@FieldMap Map<String,String> map);

}
