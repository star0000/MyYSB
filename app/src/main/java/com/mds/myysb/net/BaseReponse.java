package com.mds.myysb.net;

/* 接口返回的实体基类
 * @param <T>
 *     统一接口返回的数据基类,其中results的类型是泛型,用于接收具体的数据类；
 */

public class BaseReponse<T>{

    /**
     * errNo = 0 成功
     */

    private int errNo;                   //响应码
    private String errMsg;             //提示信息
    private T data;                  //返回的具体数据


    public int getErrNo() {
        return errNo;
    }

    public void setErrNo(int errNo) {
        this.errNo = errNo;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseReponse{" +
                "errNo=" + errNo +
                ", errMsg='" + errMsg + '\'' +
                ", data=" + data +
                '}';
    }

}
