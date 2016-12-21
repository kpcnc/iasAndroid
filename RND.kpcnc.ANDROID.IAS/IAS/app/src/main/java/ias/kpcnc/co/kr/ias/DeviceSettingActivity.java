package ias.kpcnc.co.kr.ias;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import ias.kpcnc.co.kr.ias.bean.User;
import ias.kpcnc.co.kr.ias.common.CommonManager;
import ias.kpcnc.co.kr.ias.dialog.DeviceDeleteDialog;
import ias.kpcnc.co.kr.ias.dialog.DeviceRegistrationDialog;

/**
 * Created by Hong on 2016-10-28.
 */

public class DeviceSettingActivity extends Activity {
    private static final String TAG = "DeviceSettingActivity";

    // 공통 함수 호출
    private CommonManager commonManager;
    private DeviceRegistrationDialog devRegistDialog;
    private DeviceDeleteDialog devDeleteDialog;
    static String strJson = "";
    SharedPreferences userInfoPrefs;

    String deviceUrl = "http://iasapi.kpcnc.co.kr:8051/iasapi/devices/device";
    String deviceMainUrl = "http://iasapi.kpcnc.co.kr:8051/iasapi/devices/devicemain";
    String prefUserId;
    String prefUserPw;
    String modelNm;
    String dveComp;
    String dveImei;
    int dvePosition;
    Boolean mainDevFlag = false;
    ArrayList<String> items;
    ArrayList<HashMap<String, String>> mapList;

    Device deviceVo;
    User userVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting);

        // ListView Footer Setting
//        View view = getLayoutInflater().inflate(R.layout.activity_device_setting, null, false);
//        listView.addFooterView(view);

        userInfoPrefs = getSharedPreferences("userInfoPrefs", MODE_PRIVATE);
        prefUserId = userInfoPrefs.getString("userId", "");
        prefUserPw = userInfoPrefs.getString("userPw", "");

        // 디바이스 리스트 조회 api 호출
        HttpAsyncTaskDevice httpTaskUser = new HttpAsyncTaskDevice(DeviceSettingActivity.this);
        httpTaskUser.execute(deviceUrl, prefUserId);
    }

    /**
     * ListView에 CustomAdapter Inner Class 생성
     */
    private class CustomAdapter extends ArrayAdapter<String> {
        private ArrayList<String> items;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View conView = convertView;

            if(conView ==null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                conView = vi.inflate(R.layout.list_item_device_register, null);
            }

            TextView textView = (TextView) conView.findViewById(R.id.device_name);
            textView.setText(items.get(position));

            dvePosition= position;

            // 디바이스 해제 버튼
            Button button = (Button)conView.findViewById(R.id.deviceClear);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    devDeleteDialog = new DeviceDeleteDialog(DeviceSettingActivity.this, delCancelListener, clearListener);
                    devDeleteDialog.show();
                }
            });

            return conView;
        }

        @Override
        public long getItemId(int position) {
            return position ;
        }

        // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
        @Override
        public String getItem(int position) {
            return items.get(position) ;
        }
    }

    /**
     * CustomDialog OnClickListener - cancel button
     */
    private View.OnClickListener delCancelListener = new View.OnClickListener() {
        public void onClick(View view) {
            devDeleteDialog.dismiss();
        }
    };

    /**
     * CustomDialog OnClickListener - clear button
     */
    private View.OnClickListener clearListener = new View.OnClickListener() {
        public void onClick(View view) {
//            CustomDialog 메소드 호출 하여 값 세팅
//            String clearPassword = devDeleteDialog.dialogNewPwd();
            devDeleteDialog.dismiss();

            // dve_id 를 가지고 삭제
            String devId = mapList.get(dvePosition).get("dve_id");

            // 디바이스 삭제 api 호출
            HttpAsyncTaskDeviceDel httpTaskDevDel = new HttpAsyncTaskDeviceDel(DeviceSettingActivity.this);
            httpTaskDevDel.execute(deviceUrl, devId);
        }
    };

    public void onClick(View view) {
        switch (view.getId()) {
            // 디바이스 등록 버튼 onClick
            case R.id.btn_registration:
                devRegistDialog = new DeviceRegistrationDialog(this, cancelListener, registListener);
                devRegistDialog.show();
                break;
        }
    }

    /**
     * DeviceRegistrationDialog OnClickListener - cancel button
     */
    private View.OnClickListener cancelListener = new View.OnClickListener() {
        public void onClick(View view) {
            devRegistDialog.dismiss();
        }
    };

    /**
     * DeviceRegistrationDialog OnClickListener - registration button
     */
    private View.OnClickListener registListener = new View.OnClickListener() {
        public void onClick(View view) {
            // devRegistDialog 메소드 호출 하여 값 세팅
            String devicePw = devRegistDialog.dialogPw();

            // 디바이스 정보 가져오기
            modelNm = Build.MODEL;
            dveComp = Build.MANUFACTURER;
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            dveImei = telephonyManager.getDeviceId();

            // password check
            if(!devicePw.equals(prefUserPw)) {
                Toast.makeText(DeviceSettingActivity.this, R.string.password_check_text, Toast.LENGTH_SHORT).show();
                return;
            } else {
                HttpAsyncTaskDeviceReg httpTaskDeviceReg = new HttpAsyncTaskDeviceReg(DeviceSettingActivity.this);
                httpTaskDeviceReg.execute(deviceUrl, prefUserId, dveImei, dveComp, modelNm);

                devRegistDialog.dismiss();
            }
        }
    };

    /**
     * 디바이스 삭제 input Data - url, json, method 공통함수 호출
     */
    public String getDeviceDel(String url, Device device){
        InputStream is = null;
        String result = "";

        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject header = new JSONObject();
            JSONObject data = new JSONObject();

            header.put("_method", "DELETE");
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
                result = "Did not work!!";
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
     * 디바이스 삭제 thread
     */
    private class HttpAsyncTaskDeviceDel extends AsyncTask<String, Void, String> {

        private DeviceSettingActivity deviceSet;

        HttpAsyncTaskDeviceDel(DeviceSettingActivity deviceSettingActivity) {
            this.deviceSet = deviceSettingActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            deviceVo = new Device();
            deviceVo.setDveImei(urls[1]);

            return getDeviceDel(urls[0],deviceVo);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            deviceSet.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(strJson);
                        String resultData = jsonObject.getString("data");

                        // 디바이스 삭제 완료시 Toast 알람
                        if(resultData.equals("true")) {
                            Toast.makeText(DeviceSettingActivity.this, R.string.device_delete_ok, Toast.LENGTH_SHORT).show();
                        }
                        // 화면 ListView Reflash
                        HttpAsyncTaskDevice httpTaskUser = new HttpAsyncTaskDevice(DeviceSettingActivity.this);
                        httpTaskUser.execute(deviceUrl, prefUserId);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 디바이스 저장 input Data - url, json, method 공통함수 호출
     */
    public String getDeviceReg(String url, Device device){
        InputStream is = null;
        String result = "";

        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject header = new JSONObject();
            JSONObject data = new JSONObject();

            header.put("_method", "POST");
            data.put("user_id", device.getUserId());
            data.put("dve_id", device.getDveImei());
            data.put("dve_comp", device.getDveComp());
            data.put("model_nm", device.getModelNm());
            jsonObject.put("header", header);
            jsonObject.put("data", data);

            String json = "";
            json = jsonObject.toString();

            commonManager = (CommonManager)getApplicationContext();
            is = commonManager.reqHttp(url, json);

            if(is != null)
                result = convertInputStreamToString(is);
            else
                result = "Did not work!!";
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
     * 디바이스 저장 thread
     */
    private class HttpAsyncTaskDeviceReg extends AsyncTask<String, Void, String> {

        private DeviceSettingActivity deviceSet;

        HttpAsyncTaskDeviceReg(DeviceSettingActivity deviceSettingActivity) {
            this.deviceSet = deviceSettingActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            deviceVo = new Device();
            deviceVo.setUserId(urls[1]);
            deviceVo.setDveImei(urls[2]);
            deviceVo.setDveComp(urls[3]);
            deviceVo.setModelNm(urls[4]);

            return getDeviceReg(urls[0],deviceVo);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            deviceSet.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(strJson.equals("FALSE")){
                            Toast.makeText(DeviceSettingActivity.this, R.string.duplication_device, Toast.LENGTH_SHORT).show();
                        } else {
                            JSONObject jsonObject = new JSONObject(strJson);
                            String resultData = jsonObject.getString("data");

                            // 디바이스 등록 완료시 Toast 알람
                            if(resultData.equals("true")) {
                                Toast.makeText(DeviceSettingActivity.this, R.string.device_registration_ok, Toast.LENGTH_SHORT).show();
                            }

                            if(mainDevFlag) {
                                // 주 채널 디바이스 설정 api 호출
                                HttpAsyncTaskMainDveSet httpTaskMainDveSet = new HttpAsyncTaskMainDveSet(DeviceSettingActivity.this);
                                httpTaskMainDveSet.execute(deviceMainUrl, prefUserId, dveImei);
                            }

                            // 화면 ListView Refresh
                            HttpAsyncTaskDevice httpTaskUser = new HttpAsyncTaskDevice(DeviceSettingActivity.this);
                            httpTaskUser.execute(deviceUrl, prefUserId);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 주채널 등록 input Data - url, json, method 공통함수 호출
     */
    public String putMainDevice(String url, Device device){
        InputStream is = null;
        String result = "";

        try {
            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            JSONObject header = new JSONObject();
            JSONObject data = new JSONObject();

            header.put("_method", "PUT");
            data.put("user_id", device.getUserId());
            data.put("dve_id", device.getDveImei());
            jsonObject.put("header", header);
            jsonObject.put("data", data);

            String json = "";
            // convert JSONObject to JSON to String
            json = jsonObject.toString();

            commonManager = (CommonManager)getApplicationContext();
            is = commonManager.reqHttp(url, json);

            if(is != null)
                result = convertInputStreamToString(is);
            else
                result = "Did not work!!";
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
     * 주채널 등록 thread
     */
    private class HttpAsyncTaskMainDveSet extends AsyncTask<String, Void, String> {

        private DeviceSettingActivity deviceSet;

        HttpAsyncTaskMainDveSet(DeviceSettingActivity deviceSettingActivity) {
            this.deviceSet = deviceSettingActivity;
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
            deviceSet.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*
                    try {
                        JSONObject jsonObject = new JSONObject(strJson);
                        JSONArray jsonDeviceList = new JSONArray(jsonObject.getString("data"));

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    */
                }
            });
        }
    }

    /**
     * 디바이스 리스트 조회 input Data - url, json, method 공통함수 호출
     */
    public String getDevice(String url, User user){
        InputStream is = null;
        String result = "";

        try {
            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            JSONObject header = new JSONObject();
            JSONObject data = new JSONObject();

            header.put("_method", "GET");
            data.put("user_id", user.getUserId());
            jsonObject.put("header", header);
            jsonObject.put("data", data);

            String json = "";
            // convert JSONObject to JSON to String
            json = jsonObject.toString();

            commonManager = (CommonManager)getApplicationContext();
            is = commonManager.reqHttp(url, json);

            if(is != null)
                result = convertInputStreamToString(is);
            else
                result = "Did not work!!";
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
     * 디바이스 리스트 조회 thread
     */
    private class HttpAsyncTaskDevice extends AsyncTask<String, Void, String> {

        private DeviceSettingActivity deviceSet;

        HttpAsyncTaskDevice(DeviceSettingActivity deviceSettingActivity) {
            this.deviceSet = deviceSettingActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            userVo = new User();
            userVo.setUserId(urls[1]);

            return getDevice(urls[0],userVo);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            deviceSet.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(strJson);
                        JSONArray json = new JSONArray(jsonObject.getString("data"));

                        // 신규 디바이스 등록인 경우 주채널 디바이스 SET
                        if(json.length() ==0) {
                            mainDevFlag = true;
                        }

                        ArrayList list = new ArrayList();
                        for(int i=0; i<json.length(); i++) {
                            JSONObject jsonData = json.getJSONObject(i); // JSONObject 추출
                            list.add(jsonData);
                        }

                        mapList = new ArrayList<HashMap<String, String>>();
                        JSONObject  jsonData;
                        items = new ArrayList<>();
                        for(int i=0; i<list.size(); i++){
                            jsonData = new JSONObject(list.get(i).toString());
                            HashMap map = new HashMap();
                            map.put("model_nm", jsonData.getString("model_nm"));
                            map.put("dve_id", jsonData.getString("dve_id"));
                            mapList.add(map);

                            // ListView에 보여질 items SET
                            items.add(mapList.get(i).get("model_nm"));
                        }

                        // List view set
                        ListView listView = (ListView) DeviceSettingActivity.this.findViewById(R.id.deviceList);
                        CustomAdapter adapter = new CustomAdapter(DeviceSettingActivity.this, 0, items);
                        listView.setAdapter(adapter);

                    }catch (JSONException e) {
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
