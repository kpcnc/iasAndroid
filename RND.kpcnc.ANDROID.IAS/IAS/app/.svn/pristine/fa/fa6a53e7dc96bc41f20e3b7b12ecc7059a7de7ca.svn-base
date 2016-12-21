package ias.kpcnc.co.kr.ias;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import ias.kpcnc.co.kr.ias.bean.Device;
import ias.kpcnc.co.kr.ias.common.CommonManager;

/**
 * Created by Hong on 2016-10-28.
 */

public class MainDeviceSettingActivity extends Activity {
    private static final String TAG = "MainDveSetActivity";
    // 공통 함수 호출
    private CommonManager commonManager;
    static String strJson = "";
    String deviceUrl = "http://iasapi.kpcnc.co.kr:8051/iasapi/devices/device";
    String mainDeviceUrl = "http://iasapi.kpcnc.co.kr:8051/iasapi/devices/devicemain";
    String prefUserId;
    int itemChkPosition;
    int mainDvePosition;

    SharedPreferences userInfoPrefs;
    ArrayList<String> itemArrayList;
    ArrayList<HashMap<String, String>> mapList;

    Device deviceVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_device_setting);

        userInfoPrefs = getSharedPreferences("userInfoPrefs", MODE_PRIVATE);
        prefUserId = userInfoPrefs.getString("userId", "");

        // 주 채널 디바이스 리스트 조회 api 호출
        HttpAsyncTaskDevice httpTaskDevice = new HttpAsyncTaskDevice(MainDeviceSettingActivity.this);
        httpTaskDevice.execute(deviceUrl, prefUserId);

    }

    /**
     * 주 채널 디바이스 리스트 조회 input Data - url, json, method 공통함수 호출
     */
    public String getMainDevice(String url, Device device) {
        InputStream is = null;
        String result = "";

        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject header = new JSONObject();
            JSONObject data = new JSONObject();

            header.put("_method", "GET");
            data.put("user_id", device.getUserId());
            jsonObject.put("header", header);
            jsonObject.put("data", data);

            String json = "";
            json = jsonObject.toString();

            commonManager = (CommonManager)getApplicationContext();
            is = commonManager.reqHttp(url, json);

            if(is != null)
                result = convertInputStreamToString(is);
            else
                result = "Did not work";
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            Log.d("********** InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    /**
     * 주 채널 디바이스 리스트 조회 thread - 디바이스 리스트 조회 API 호출
     */
    private class HttpAsyncTaskDevice extends AsyncTask<String, Void, String> {

        private MainDeviceSettingActivity mainDevSet;

        HttpAsyncTaskDevice(MainDeviceSettingActivity mainDeviceSettingActivity) {
            this.mainDevSet = mainDeviceSettingActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            deviceVo = new Device();
            deviceVo.setUserId(urls[1]);

            return getMainDevice(urls[0],deviceVo);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            mainDevSet.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 데이터 가져오기
                    try {
                        JSONObject jsonObject = new JSONObject(strJson);
                        JSONArray json = new JSONArray(jsonObject.getString("data"));

                        ArrayList list = new ArrayList();
                        for(int i=0; i<json.length(); i++) {
                            JSONObject jsonData = json.getJSONObject(i); // JSONObject 추출
                            list.add(jsonData);
                        }

                        mapList = new ArrayList<HashMap<String, String>>();
                        JSONObject jsonData;
                        itemArrayList = new ArrayList<String>();
                        for(int i=0; i<list.size(); i++){
                            jsonData = new JSONObject(list.get(i).toString());
                            HashMap map = new HashMap();
                            map.put("model_nm", jsonData.getString("model_nm"));
                            map.put("main_dve_yn", jsonData.getString("main_dve_yn"));
                            map.put("dve_id", jsonData.getString("dve_id"));
                            mapList.add(map);

                            itemArrayList.add(mapList.get(i).get("model_nm"));
                            //itemArrayList.add(jsonData.getString("model_nm"));

                            if(mapList.get(i).get("main_dve_yn").equals("Y")){
                               itemChkPosition = i;
                            }
                        }

                        ListView listView = (ListView)findViewById(R.id.main_device_listView);
                        listView.setAdapter(new ArrayAdapter<String>(MainDeviceSettingActivity.this, android.R.layout.simple_list_item_single_choice, itemArrayList));
                        listView.setItemChecked(itemChkPosition,true);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //Toast.makeText(MainDeviceSettingActivity.this ,itemArrayList.get(position),Toast.LENGTH_LONG).show();

                                // 주 채널 디바이스 클릭 position SET
                                mainDvePosition = position;
                                String dveId = mapList.get(mainDvePosition).get("dve_id");

                                // 주 채널 디바이스 변경 api 호출
                                HttpAsyncTaskDevChange httpTaskDevChg = new HttpAsyncTaskDevChange(MainDeviceSettingActivity.this);
                                httpTaskDevChg.execute(mainDeviceUrl, prefUserId, dveId);
                            }
                       });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 주 채널 디바이스 설정 input Data - url, json, method 공통함수 호출
     */
    public String putMainDevice(String url, Device device) {
        InputStream is = null;
        String result = "";

        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject header = new JSONObject();
            JSONObject data = new JSONObject();

            header.put("_method", "PUT");
            data.put("user_id", device.getUserId());
            data.put("dve_id", device.getDveImei());
            jsonObject.put("header", header);
            jsonObject.put("data", data);

            String json = "";
            json = jsonObject.toString();

            commonManager = (CommonManager)getApplicationContext();
            is = commonManager.reqHttp(url, json);

            if(is != null)
                result = convertInputStreamToString(is);
            else
                result = "Did not work";
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            Log.d("********** InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 주 채널 디바이스 설정 thread - 주 채널 디바이스 설정 API 호출
     */
    private class HttpAsyncTaskDevChange extends AsyncTask<String, Void, String> {

        private MainDeviceSettingActivity mainDevSet;

        HttpAsyncTaskDevChange(MainDeviceSettingActivity mainDeviceSettingActivity) {
            this.mainDevSet = mainDeviceSettingActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            deviceVo = new Device();
            deviceVo.setUserId(urls[1]);
            deviceVo.setDveImei(urls[2]);

            return putMainDevice(urls[0],deviceVo);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            mainDevSet.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 데이터 가져오기
                    try {
                        JSONObject jsonObject = new JSONObject(strJson);
                        String jsonData = jsonObject.getString("data");

                        if(jsonData.equals("true")) {
                            Toast.makeText(MainDeviceSettingActivity.this, R.string.main_device_change_text, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}
