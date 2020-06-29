package com.mds.myysb.net;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Log;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import rx.Observer;

/*
 * 它主要是请求成功失败、网络异常时的回调,它实现的Observer类,
 * 其中成功失败主要在Onext方法里处理
 *
 */

public abstract class BaseObserver<T> implements Observer<BaseReponse<T>> {


    private Context mContext;

    public BaseObserver(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ConnectException ||
                e instanceof TimeoutException ||
                e instanceof NetworkErrorException ||
                e instanceof UnknownHostException) {
            try {
                onFailure(e, false);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else {
            try {
                onFailure(e, true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void onNext(BaseReponse<T> tBaseReponse) {
        if (tBaseReponse.getErrNo() == 0) {
            onSuccess(tBaseReponse);
        } else {
            onCodeError(tBaseReponse);
        }
    }
    //请求成功且返回码为200的回调方法,这里抽象方法申明
    public abstract void onSuccess(BaseReponse<T> tBaseReponse);

    //请求成功但返回的code码不是200的回调方法,这里抽象方法申明
    public abstract void onCodeError(BaseReponse tBaseReponse);

    //请求失败回调方法,这里抽象方法申明
    public abstract void onFailure(Throwable e, boolean netWork) throws Exception;

}
