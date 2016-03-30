package com.example.qin.littleweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qin.littleweather.R;
import com.example.qin.littleweather.db.WeatherDBManager;
import com.example.qin.littleweather.model.City;
import com.example.qin.littleweather.model.County;
import com.example.qin.littleweather.model.Province;
import com.example.qin.littleweather.util.HttpCallbackListener;
import com.example.qin.littleweather.util.HttpUtil;
import com.example.qin.littleweather.util.UtilParse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qin on 2016/3/30.
 */
public class ChooseAreaActivity extends Activity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private WeatherDBManager db;
    private List<String> dataList = new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province province;
    private City city;
    private County county;
    private int currentLevel;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_area);
        listView = (ListView)findViewById(R.id.list_view);
        titleText = (TextView)findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);

        db = WeatherDBManager.getInstance(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentLevel == LEVEL_PROVINCE){
                    province = provinceList.get(i);
                    queryCity();
                }else if (currentLevel == LEVEL_CITY){
                    city = cityList.get(i);
                    queryCounty();
                }
            }
        });
    }

    private void queryProvince(){
        provinceList = db.loadProvinces();
        if (provinceList.size() > 0){
            dataList.clear();;

            for (Province province:provinceList) {
                dataList.add(province.getProvinceName());
            }

            adapter.notifyDataSetChanged();

            listView.setSelection(0);

            titleText.setText("中国");

            currentLevel = LEVEL_PROVINCE;
        }else{
            queryFromServer(null, "province");
        }
    }

    private void queryCity() {
        cityList = db.loadCities(province.getId());
        if (cityList.size() > 0){
            dataList.clear();

            for (City city:cityList) {
                dataList.add(city.getCityName());
            }

            adapter.notifyDataSetChanged();

            listView.setSelection(0);

            titleText.setText(province.getProvinceName());

            currentLevel = LEVEL_CITY;
        }else{
            queryFromServer(province.getProvinceCode(), "city");
        }
    }

    private void queryCounty() {
        countyList = db.loadCounties(city.getId());
        if (countyList.size() > 0){
            dataList.clear();

            for (County county:countyList) {
                dataList.add(county.getCountyName());
            }

            adapter.notifyDataSetChanged();

            listView.setSelection(0);

            titleText.setText(city.getCityName());

            currentLevel = LEVEL_COUNTY;
        }else{
            queryFromServer(city.getCityCode(), "county");
        }
    }

    private void queryFromServer(final String code, final String type) {
        String address;
        if (!TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        }else{
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }

        showProgressDialog();

        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String responce) {
                boolean result = false;
                if ("province".equals(type)){
                    result = UtilParse.handleProvincesResponse(db,responce);
                }else if ("city".equals(type)){
                    result = UtilParse.handleCitiesResponse(db,responce);
                }else if ("county".equals(type)){
                    result = UtilParse.handleCountiesResponse(db,responce);
                }

                if (result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvince();
                            } else if ("city".equals(type)) {
                                queryCity();
                            } else if ("county".equals(type)) {
                                queryCounty();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void closeProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void showProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTY) {
            queryCity();
        } else if (currentLevel == LEVEL_CITY) {
            queryProvince();
        } else {
            finish();
        }
    }
}
