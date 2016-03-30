package com.example.qin.littleweather.util;

import android.text.TextUtils;

import com.example.qin.littleweather.db.WeatherDBManager;
import com.example.qin.littleweather.model.City;
import com.example.qin.littleweather.model.County;
import com.example.qin.littleweather.model.Province;

/**
 * Created by qin on 2016/3/30.
 */
public class UtilParse {

    /**
     * 解析服务器返回的省级数据
     * @param db
     * @param response
     * @return
     */
    public synchronized static boolean handleProvincesResponse(WeatherDBManager db,String response){
        if (!TextUtils.isEmpty(response)) {
            String[] allProvince = response.split(",");
            if (allProvince != null && allProvince.length > 0) {
                for (String p:allProvince) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceName(array[0]);
                    province.setProvinceCode(array[1]);
                    db.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析服务器返回的市级数据
     * @param db
     * @param response
     * @return
     */
    public synchronized static boolean handleCitiesResponse(WeatherDBManager db,String response){
        if (!TextUtils.isEmpty(response)) {
            String[] allCity = response.split(",");
            if (allCity != null && allCity.length > 0) {
                for (String p:allCity) {
                    String[] array = p.split("\\|");
                    City city = new City();
                    city.setCityName(array[0]);
                    city.setCityCode(array[1]);
                    db.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析服务器返回的县级数据
     * @param db
     * @param response
     * @return
     */
    public synchronized static boolean handleCountiesResponse(WeatherDBManager db,String response){
        if (!TextUtils.isEmpty(response)) {
            String[] allCounty = response.split(",");
            if (allCounty != null && allCounty.length > 0) {
                for (String p:allCounty) {
                    String[] array = p.split("\\|");
                    County county = new County();
                    county.setCountyName(array[0]);
                    county.setCountyCode(array[1]);
                    db.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }
}
