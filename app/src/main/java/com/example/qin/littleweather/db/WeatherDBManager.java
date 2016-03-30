package com.example.qin.littleweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qin.littleweather.model.City;
import com.example.qin.littleweather.model.County;
import com.example.qin.littleweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qin on 2016/3/29.
 */
public class WeatherDBManager {
    public static final String DBNAME = "littleweather";
    public static final int VERSION = 1;
    private static WeatherDBManager weatherDBManager;
    private static SQLiteDatabase sqLiteDatabase;

    private WeatherDBManager(Context context){
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,DBNAME,null,VERSION);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public synchronized static WeatherDBManager getInstance(Context context){
        if (weatherDBManager == null){
            weatherDBManager = new WeatherDBManager(context);
        }

        return weatherDBManager;
    }

    /**
     * 保存Pronvince实例到数据库
     * @param province
     */
    public void saveProvince(Province province){
        if (province != null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("province_name",province.getProvinceName());
            contentValues.put("province_code",province.getProvinceCode());
            sqLiteDatabase.insert("Province",null,contentValues);
        }
    }

    /**
     * 读取Province表所有数据
     * @return
     */
    public List<Province> loadProvinces(){
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = sqLiteDatabase.query("Province",null,null,null,null,null,null);
        if (cursor.moveToNext()){
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }while (cursor.moveToNext());
        }

        return list;
    }

    /**
     * 保存City实例到数据库
     * @param city
     */
    public void saveCity(City city){
        if (city != null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("province_name",city.getCityName());
            contentValues.put("province_code",city.getCityCode());
            sqLiteDatabase.insert("City",null,contentValues);
        }
    }

    /**
     * 读取City表所有数据
     * @return
     */
    public List<City> loadCities(int provinceId){
        List<City> list = new ArrayList<City>();
        Cursor cursor = sqLiteDatabase.query("City",null,"province_id = ?",
                new String[]{String.valueOf(provinceId)},null,null,null);
        if (cursor.moveToNext()){
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                list.add(city);
            }while (cursor.moveToNext());
        }

        return list;
    }

    /**
     * 保存County实例到数据库
     * @param ounty
     */
    public void saveCounty(County ounty){
        if (ounty != null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("ounty_name",ounty.getCountyName());
            contentValues.put("ounty_code",ounty.getCountyCode());
            sqLiteDatabase.insert("County",null,contentValues);
        }
    }

    /**
     * 读取City表所有数据
     * @return
     */
    public List<County> loadCounties(int cityId){
        List<County> list = new ArrayList<County>();
        Cursor cursor = sqLiteDatabase.query("County",null,"city_id = ?",
                new String[]{String.valueOf(cityId)},null,null,null);
        if (cursor.moveToNext()){
            do {
                County ounty = new County();
                ounty.setId(cursor.getInt(cursor.getColumnIndex("id")));
                ounty.setCountyName(cursor.getString(cursor.getColumnIndex("ounty_name")));
                ounty.setCountyCode(cursor.getString(cursor.getColumnIndex("ounty_code")));
                list.add(ounty);
            }while (cursor.moveToNext());
        }

        return list;
    }

}
