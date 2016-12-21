package ias.kpcnc.co.kr.ias;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ias.kpcnc.co.kr.ias.bean.User;
import ias.kpcnc.co.kr.ias.common.CommonManager;

/**
 * Created by Hong on 2016-10-31.
 */

public class AccountDeleteActivity extends Activity{
    private static final String TAG = "AccountDeleteActivity";

    // 공통 함수 호출
    private CommonManager commonManager;
    static String strJson = "";

    String delUrl = "http://iasapi.kpcnc.co.kr:8051/iasapi/members/member";
    CheckBox delCheck;
    String prefUserId;
    User userVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        // 로그인 시 저장된 회원정보 호출
        SharedPreferences userInfoPrefs = getSharedPreferences("userInfoPrefs", MODE_PRIVATE);
        prefUserId = userInfoPrefs.getString("userId", "");

        delCheck = (CheckBox) findViewById(R.id.deleteCheck);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.accountDelete:
                // 회정 정보 삭제
                if(delCheck.isChecked()==true) {
                    HttpAsyncTaskDel httpTaskUser = new HttpAsyncTaskDel(AccountDeleteActivity.this);
                    httpTaskUser.execute(delUrl, prefUserId);
                } else {
                    Toast.makeText(AccountDeleteActivity.this, R.string.request_delete_text, Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    /**
     * 회원 정보 삭제 input Data - url, json 공통함수 호출
     */
    public String delUser(String url, User user){
        InputStream is = null;
        String result = "";

        try {
            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            JSONObject header = new JSONObject();
            JSONObject data = new JSONObject();

            header.put("_method", "DELETE");
            data.put("user_id", user.getUserId());
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
            Log.d(TAG, "********** InputStream: " + e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 회원 정보 삭제 thread
     */
    private class HttpAsyncTaskDel extends AsyncTask<String, Void, String> {

        private AccountDeleteActivity accountDel;

        HttpAsyncTaskDel(AccountDeleteActivity accountDeleteActivity) {
            this.accountDel = accountDeleteActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            userVo = new User();
            userVo.setUserId(urls[1]);

            return delUser(urls[0],userVo);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            accountDel.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        JSONObject jsonObject = new JSONObject(strJson);
                        String resultData = jsonObject.getString("data");

                        // 회원정보 삭제인 경우 Toast 알람
                        if(resultData.equals("true")) {
                            Toast.makeText(AccountDeleteActivity.this, R.string.account_delete_ok, Toast.LENGTH_LONG).show();

                            // TODO - 디바이스 설정 정보 초기화 (IAS_TB_DEVICE_M, IAS_TB_CHANNEL_H, IAS_TB_MSG_CONFIG_M 등)
                            // 자동 로그인 데이터 초기화
                            SharedPreferences autoLoginPrefs = getSharedPreferences("autoLoginPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = autoLoginPrefs.edit();
                            editor.clear();
                            editor.commit();

                            // 로그인 화면 이동
                            Intent intent = new Intent(AccountDeleteActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
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
