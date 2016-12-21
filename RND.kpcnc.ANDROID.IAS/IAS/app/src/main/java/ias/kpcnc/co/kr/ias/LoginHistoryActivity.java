package ias.kpcnc.co.kr.ias;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ias.kpcnc.co.kr.ias.bean.User;
import ias.kpcnc.co.kr.ias.common.CommonManager;

/**
 * Created by Hong on 2016-10-28.
 */

public class LoginHistoryActivity extends Activity {
    private static final String TAG = "MemberModifyActivity";

    // 공통 함수 호출
    private CommonManager commonManager;
    static String strJson = "";

    String histUrl = "http://iasapi.kpcnc.co.kr:8051/iasapi/members/loginhist";
    String userId;

    User userVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_login_list);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        // call AsynTask to perform network operation on separate thread
        HttpAsyncTaskHist httpTaskUser = new HttpAsyncTaskHist(LoginHistoryActivity.this);
        httpTaskUser.execute(histUrl, userId);
    }

    /**
     * 로그인 이력 input Data - url, json, method 공통함수 호출
     */
    public String getLoginHist(String url, User user){
        InputStream is = null;
        String result = "";
        try {
            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user.getUserId());

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
     * 로그인 이력 조회 thread
     */
    private class HttpAsyncTaskHist extends AsyncTask<String, Void, String> {

        private LoginHistoryActivity loginHistory;

        HttpAsyncTaskHist(LoginHistoryActivity loginHistoryActivity) {
            this.loginHistory = loginHistoryActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            userVo = new User();
            userVo.setUserId(urls[1]);

            return getLoginHist(urls[0],userVo);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            loginHistory.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "********** Signin User Data Received");

                    // TODO "list_item_login_list.xml" 화면에 listView 구현
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
