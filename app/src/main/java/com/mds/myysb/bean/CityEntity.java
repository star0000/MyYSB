package com.mds.myysb.bean;


/**
 * 城市的实体类
 */
public class CityEntity {

    private String id;
    private String pid;
    private String name;
    private String countyCode;
    private String cityCode;
    private String provinceCode;
    private String countyName;
    private String cityName;
    private String provinceName;


    // 返回一个精确到地区的地名
    public String getAreaName(){
        return provinceName + " " + cityName + " " + countyName;
    }


    @Override
    public String toString() {
        return "CityEntity{" +
                "id='" + id + '\'' +
                ", pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                ", areaCode='" + countyCode + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

}
