package ias.kpcnc.co.kr.ias;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ias.kpcnc.co.kr.ias.bean.User;
import ias.kpcnc.co.kr.ias.common.CommonManager;

/**
 * Created by Hong on 2016-10-24.
 */

public class SigninActivity extends Activity {
    private static final String TAG = "SigninActivity";

    // 공통 함수 호출
    private CommonManager commonManager;
    private String targetUrl = "http://iasapi.kpcnc.co.kr:8051/iasapi/members/member";
    static String strJson = "";

    LinearLayout linearLayout;
    InputMethodManager inputMethodManager;
    SharedPreferences userInfoPrefs;
    SharedPreferences.Editor userInfoEditor;

    EditText userId;
    EditText userPwd;
    EditText userPwdChk;
    EditText userNm;
    EditText userEmail;
    String tokenId;

    User userVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        // 화면에서 입력폼 외에 다른 화면 클릭시 키보드 숨기기
        linearLayout = (LinearLayout)findViewById(R.id.activity_sign);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(linearLayout.getWindowToken(), 0);
            }
        });

        userId = (EditText) findViewById(R.id.ias_reg_id);
        userPwd = (EditText) findViewById(R.id.ias_reg_password);
        userPwdChk = (EditText) findViewById(R.id.ias_reg_password_chk);
        userNm = (EditText) findViewById(R.id.ias_reg_name);
        userEmail = (EditText) findViewById(R.id.ias_reg_email);

        // Get FCM token
        tokenId = FirebaseInstanceId.getInstance().getToken();
        // Toast.makeText(SigninActivity.this, "토큰 아이디: " + tokenId, Toast.LENGTH_LONG).show();

/*
        try{
            JSONObject jsonObject = new JSONObject(tokenId);
            token = jsonObject.getString("token");
        }catch (JSONException e) {
            e.printStackTrace();
        }
*/
        commonManager = (CommonManager)getApplicationContext();
    }

    public void onClick(View view) {

        if(userId.getText().toString().length() == 0) {
            Toast.makeText(SigninActivity.this, R.string.request_info_id, Toast.LENGTH_SHORT).show();
            userId.requestFocus();
            return;
        }
        if(userPwd.getText().toString().length() == 0) {
            Toast.makeText(SigninActivity.this, R.string.request_info_pw, Toast.LENGTH_SHORT).show();
            userPwd.requestFocus();
            return;
        }
        if(userPwdChk.getText().toString().length() == 0) {
            Toast.makeText(SigninActivity.this, R.string.request_info_check_pw, Toast.LENGTH_SHORT).show();
            userPwdChk.requestFocus();
            return;
        }
        if(userNm.getText().toString().length() == 0) {
            Toast.makeText(SigninActivity.this, R.string.request_info_name, Toast.LENGTH_SHORT).show();
            userNm.requestFocus();
            return;
        }
        if(userEmail.getText().toString().length() == 0) {
            Toast.makeText(SigninActivity.this, R.string.request_info_email, Toast.LENGTH_SHORT).show();
            userEmail.requestFocus();
            return;
        }
        if(!userPwd.getText().toString().equals(userPwdChk.getText().toString())) {
            Toast.makeText(SigninActivity.this, R.string.request_pw_not_equal, Toast.LENGTH_SHORT).show();
            userPwd.setText("");
            userPwdChk.setText("");
            userPwd.requestFocus();
            return;
        }

        // 회원가입 API 호출
        HttpAsyncTask httpTask = new HttpAsyncTask(SigninActivity.this);
        httpTask.execute(targetUrl, userId.getText().toString(), userPwd.getText().toString(), userNm.getText().toString(), userEmail.getText().toString(), tokenId);

    }

    public String POST(String url, User user){
        InputStream is = null;
        String result = "";
        String dveImei;

        try {
            // Get Device Information
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            dveImei = telephonyManager.getDeviceId();

            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            JSONObject header = new JSONObject();
            JSONObject data = new JSONObject();

            header.put("_method", "POST");
            data.put("user_id", user.getUserId());
            data.put("user_nm", user.getUserNm());
            data.put("user_pwd", user.getUserPwd());
            data.put("user_email", user.getUserEmail());
            data.put("main_ch_dve", dveImei);
            data.put("token_id", user.getTokenId());
            jsonObject.put("header", header);
            jsonObject.put("data", data);

            String json = "";
            json = jsonObject.toString();

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
            Log.d(TAG,  "********** InputStream: " + e.getLocalizedMessage());
        }
        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private SigninActivity signin;

        HttpAsyncTask(SigninActivity signinActivity) {
            this.signin = signinActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            userVo = new User();
            userVo.setUserId(urls[1]);
            userVo.setUserPwd(urls[2]);
            userVo.setUserNm(urls[3]);
            userVo.setUserEmail(urls[4]);
            userVo.setTokenId(urls[5]);

            return POST(urls[0],userVo);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            signin.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 데이터 가져오기
                    try {
                        // result = "data"의 형식이 배열[]로 들어옴 (배열 갯수 1건)
                        JSONObject jsonObject = new JSONObject(strJson);
                        String data = jsonObject.getString("data");

                        if(data.equals("true")) {
                            Toast.makeText(SigninActivity.this, R.string.signin_ok, Toast.LENGTH_SHORT).show();
                            // 로그인 화면 이동
                            Intent intent = new Intent(SigninActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Log.d(TAG, "********** API output error ");
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
