package ias.kpcnc.co.kr.ias;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ias.kpcnc.co.kr.ias.bean.User;
import ias.kpcnc.co.kr.ias.common.CommonManager;
import ias.kpcnc.co.kr.ias.manager.PropertyManager;

/**
 * Created by Hong on 2016-10-19.
 */

public class LoginActivity extends Activity {
    // 공통 함수 호출
    private CommonManager commonManager;
    private static final String TAG = "LoginActivity";
    static String strJson = "";

    String loginUrl = "http://iasapi.kpcnc.co.kr:8051/iasapi/members/login";
    String findId ="";
    SharedPreferences autoLoginPrefs;
    SharedPreferences.Editor editor;
    SharedPreferences userInfoPrefs;
    SharedPreferences.Editor userInfoEditor;

    RelativeLayout relativeLayout;
    InputMethodManager inputMethodManager;

    EditText iasId;
    EditText iasPw;
    CheckBox autoLoginChk;
    TextView signin;
    TextView searchIdPw;
    String token;
    String tokenId;

    User userVo;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // topic : api를 사용하여 푸시 알림 전송시 같은 토픽명 그룹 전체에 메세지를 발송 할 수 있다.
        FirebaseMessaging.getInstance().subscribeToTopic("notice");
        FirebaseInstanceId.getInstance().getToken();

        // 화면에서 입력폼 외에 다른 화면 클릭시 키보드 숨기기
        relativeLayout = (RelativeLayout)findViewById(R.id.activity_login);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
            }
        });

        iasId = (EditText) findViewById(R.id.ias_id);
        iasPw = (EditText) findViewById(R.id.ias_password);
        autoLoginChk = (CheckBox) findViewById(R.id.chk_auto_login);
        signin = (TextView) findViewById(R.id.ias_signin);
        searchIdPw = (TextView) findViewById(R.id.ias_id_pw_search);

        // 아이디 찾기 후 로그인 시 SET iasID EditText
        Intent intent = getIntent();
        findId = intent.getStringExtra("findId");
        iasId.setText(findId);

        autoLoginPrefs = getSharedPreferences("autoLoginPrefs", MODE_PRIVATE);

        // if 자동로그인 설정 했을 시
        if(autoLoginPrefs.getBoolean("autoLogin", false)) {

            iasId.setText(autoLoginPrefs.getString("userId",""));
            iasPw.setText(autoLoginPrefs.getString("userPw",""));
            autoLoginChk.setChecked(true);

            // 메인 메시지 화면 이동
            Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intentMain);
        }

        commonManager = (CommonManager)getApplicationContext();
        // check network
        commonManager.isConnected();

        // TextView에 클릭 이벤트 처리
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    // Go to SignIn View
                    case R.id.ias_signin:
                        Intent intent = new Intent(LoginActivity.this, TermsActivity.class);
                        startActivity(intent);
                        break;
                    // Go to Search ID/PW View
                    case R.id.ias_id_pw_search:
                        Intent intent1 = new Intent(LoginActivity.this, SearchIdPwActivity.class);
                        startActivity(intent1);
                        break;
                }
            }
        };

        signin.setOnClickListener(listener);
        searchIdPw.setOnClickListener(listener);

        //배지 카운터 설정(초기화)(기기별 호환문제)
        Intent i = new Intent("android.intent.action.BADGE_COUNT_UPDATE");

        i.putExtra("badge_count", 0); //다시 배지카운터를 0으로 초기화
        i.putExtra("badge_count_package_name", getApplicationContext().getPackageName());
        i.putExtra("badge_count_class_name", LoginActivity.class.getName());

        //변경된 값으로 다시 공유 저장소 값 초기화
        PropertyManager.getInstance().setBadge_number(0);

        sendBroadcast(i); //브로드캐스트를 이용

    }

    public void onClick(View view) {

        // Validation ID
        if(iasId.getText().toString().length() == 0) {
            Toast.makeText(LoginActivity.this, R.string.request_id, Toast.LENGTH_SHORT).show();
            iasId.requestFocus();
            return;
        }
        // Validation PW
        if(iasPw.getText().toString().length() == 0) {
            Toast.makeText(LoginActivity.this, R.string.request_password, Toast.LENGTH_SHORT).show();
            iasPw.requestFocus();
            return;
        }

        // Get FCM token
        tokenId = FirebaseInstanceId.getInstance().getToken();
        //Toast.makeText(LoginActivity.this, "토큰 아이디: " + tokenId, Toast.LENGTH_LONG).show();

/*
        try{
            JSONObject jsonObject = new JSONObject(tokenId);
            token = jsonObject.getString("token");
        }catch (JSONException e) {
            e.printStackTrace();
        }
*/
        HttpAsyncTaskUser httpTaskUser = new HttpAsyncTaskUser(LoginActivity.this);
        httpTaskUser.execute(loginUrl, iasId.getText().toString(), iasPw.getText().toString(), tokenId);

    }

    public String POST(String url, User user) {
        InputStream is = null;
        String result = "";

        try {
            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            JSONObject header = new JSONObject();
            JSONObject data = new JSONObject();

            header.put("_method", "GET");
            data.put("user_id", user.getUserId());
            data.put("user_pwd", user.getUserPwd());
            data.put("token_id", user.getTokenId());
            jsonObject.put("header", header);
            jsonObject.put("data", data);

            String json = "";
            // convert JSONObject to JSON to String
            json = jsonObject.toString();

            // commonManager callback inputStream
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

    private class HttpAsyncTaskUser extends AsyncTask<String, Void, String> {

        private LoginActivity login;

        HttpAsyncTaskUser(LoginActivity loginActivity) {
            this.login = loginActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            userVo = new User();
            userVo.setUserId(urls[1]);
            userVo.setUserPwd(urls[2]);
            userVo.setTokenId(urls[3]);

            return POST(urls[0],userVo);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            strJson = result;
            login.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 데이터 가져오기
                    try {
                        String loginId ="";
                        String loginPw ="";

                        // 로그인 정보 없을 시 Toast
                        if(strJson.equals("FALSE")){
                            Toast.makeText(LoginActivity.this, R.string.request_check_id_pw, Toast.LENGTH_SHORT).show();
                        } else {
                            // result = "data"의 형식이 배열[]로 들어옴 (배열 갯수 1건)
                            JSONObject jsonObject = new JSONObject(strJson);
                            JSONArray json = new JSONArray(jsonObject.getString("data"));
                            for(int i=0; i<json.length(); i++) {
                                JSONObject jsonData = json.getJSONObject(i); // JSONObject 추출
                                loginId += jsonData.getString("user_id");
                                loginPw += jsonData.getString("user_pwd");
                            }

                            if(!loginId.isEmpty()) {

                                // 다른 Activity 호출용 SharedPreferences 생성
                                userInfoPrefs = getSharedPreferences("userInfoPrefs", MODE_PRIVATE);
                                userInfoEditor = userInfoPrefs.edit();
                                userInfoEditor.putString("userId", loginId);
                                userInfoEditor.putString("userPw", loginPw);
                                userInfoEditor.commit();

                                // 자동로그인(관리용) checked = true -> SharedPreferences 파일에 아이디/비밀번호 저장
                                if(autoLoginChk.isChecked() == true) {
                                    autoLoginPrefs = getSharedPreferences("autoLoginPrefs", MODE_PRIVATE);
                                    editor = autoLoginPrefs.edit();
                                    editor.putString("userId", loginId);
                                    editor.putString("userPw", loginPw);
                                    editor.putBoolean("autoLogin", true);
                                    editor.commit();
                                }
                                // 메인 메시지 화면 이동
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
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
