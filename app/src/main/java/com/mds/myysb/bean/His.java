package com.mds.myysb.bean;

import org.litepal.crud.DataSupport;

public class His extends DataSupport {

    private int id;//0
    private String starttime;//开始时间1
    private String period;//时长2
    private int status=0;//状态3
    private String file_voice;//声音文件地址4
    private String file_fhr;//图像视频文件地址5
    private String comment;//判读结果？6
    private String filename_thumbnail;//缩略图7
    private String monitor_id;//8
    private String DiagnosisID;//9
    private String Mean;//10
    private String Acc;//11
    private String Dec;//12
    private String Fm;//13
    private String AmplitudeType;//14

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFile_voice() {
        return file_voice;
    }

    public void setFile_voice(String file_voice) {
        this.file_voice = file_voice;
    }

    public String getFile_fhr() {
        return file_fhr;
    }

    public void setFile_fhr(String file_fhr) {
        this.file_fhr = file_fhr;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFilename_thumbnail() {
        return filename_thumbnail;
    }

    public void setFilename_thumbnail(String filename_thumbnail) {
        this.filename_thumbnail = filename_thumbnail;
    }

    public String getMonitor_id() {
        return monitor_id;
    }

    public void setMonitor_id(String monitor_id) {
        this.monitor_id = monitor_id;
    }

    public String getDiagnosisID() {
        return DiagnosisID;
    }

    public void setDiagnosisID(String diagnosisID) {
        DiagnosisID = diagnosisID;
    }

    public String getMean() {
        return Mean;
    }

    public void setMean(String mean) {
        Mean = mean;
    }

    public String getAcc() {
        return Acc;
    }

    public void setAcc(String acc) {
        Acc = acc;
    }

    public String getDec() {
        return Dec;
    }

    public void setDec(String dec) {
        Dec = dec;
    }

    public String getFm() {
        return Fm;
    }

    public void setFm(String fm) {
        Fm = fm;
    }

    public String getAmplitudeType() {
        return AmplitudeType;
    }

    public void setAmplitudeType(String amplitudeType) {
        AmplitudeType = amplitudeType;
    }

}
