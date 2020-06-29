package com.mds.myysb.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mds.myysb.bean.CityEntity;

import java.util.ArrayList;
import java.util.List;

public class CityManager {

    private SQLiteDatabase db;

    public CityManager(String name) {
        db = SQLiteDatabase.openDatabase(name,null,0);
    }

    public List<CityEntity> getCity(String pid){
        Cursor cursor= db.query("pub_cant", new String[]{"cant_code","cant_name"}, "super_code=?", new String[]{pid}, null, null, null);
        List<CityEntity> mCityList = new ArrayList<CityEntity>();
        while(cursor.moveToNext()){
            CityEntity city = new CityEntity();
            city.setId(cursor.getString(0));
            city.setName(cursor.getString(1));
            city.setPid(pid);
            mCityList.add(city);
        }
        cursor.close();
        return  mCityList;
    }

    /**
     * 根据地区码找出对应的省市区
     *
     * @param code
     * @return
     */
    public CityEntity getCurrentCityName(String code){
        CityEntity city = new CityEntity();
        String countyName = null;
        String cityName = null;
        String cityCode = null;
        String provinceName = null;
        String provinceCode = null;

        if(code != null && code != ""){//-----------------------非空判断

            // 找到第三级的地区
            Cursor countyNameCursor = db.rawQuery(
                    "select cant_name from pub_cant where cant_code = ?",
                    new String[]{code});
            if(countyNameCursor.moveToNext()){
                countyName = countyNameCursor.getString(0);
                city.setCountyName(countyName);
            }
            // 找到第二级的城市
            Cursor cityCodeCursor = db.rawQuery(
                    "select super_code from pub_cant where cant_code = ?",
                    new String[]{code});
            if(cityCodeCursor.moveToNext()){
                cityCode = cityCodeCursor.getString(0);
            }

            if(cityCode != null && cityCode != ""){//-----------------------非空判断
                Cursor cityCNameCursor = db.rawQuery(
                        "select cant_name from pub_cant where cant_code = ?",
                        new String[]{cityCode});
                if(cityCNameCursor.moveToNext()){
                    cityName = cityCNameCursor.getString(0);
                    city.setCityName(cityName);
                }

                // 找到第一级的省会
                Cursor provinceCodeCursor = db.rawQuery(
                        "select super_code from pub_cant where cant_code = ?",
                        new String[]{cityCode});
                if(provinceCodeCursor.moveToNext()){
                    provinceCode = provinceCodeCursor.getString(0);
                }
                if(provinceCode != null && provinceCode != ""){//-----------------------非空判断
                    Cursor provinceNameCursor = db.rawQuery(
                            "select cant_name from pub_cant where cant_code = ?",
                            new String[]{provinceCode});
                    if(provinceNameCursor.moveToNext()){
                        provinceName = provinceNameCursor.getString(0);
                        city.setProvinceName(provinceName);
                    }
                }else {
                    city.setProvinceName("");
                }
            }else {
                city.setCityName("");
                city.setProvinceName("");
            }
        }else {
            city.setCountyName("");
            city.setCityName("");
            city.setProvinceName("");
        }

        return  city;
    }
    /**
     * 根据第三极地区找出对应的地区码
     *
     * @param
     * @return
     */
    public CityEntity getCurrentCityCode(String areaName){
        Cursor cursor= db.rawQuery(
                "select cant_code from pub_cant where cant_name = ?",
                new String[]{areaName});

        CityEntity city = new CityEntity();
        if(cursor.moveToNext()){
            String areaCode = cursor.getString(0);
            city.setCountyCode(areaCode);
        }
        return  city;
    }

}
