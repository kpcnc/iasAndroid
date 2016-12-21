package ias.kpcnc.co.kr.ias;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
 * Created by Hong on 2016-10-26.
 */

public class SearchPwActivity extends Activity {
    private static final String TAG = "SearchPwActivity";

    // 공통 함수 호출
    private CommonManager commonManager;
    static String strJson = "";

    String targetUrl = "http://iasapi.kpcnc.co.kr:8051/iasapi/members/pwd";
    String userId;

    EditText newPwd;
    EditText newPwdChk;

    User userVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pw);

        Intent intent = getIntent();
        userId = intent.getStringExtra("findId");

        newPwd = (EditText) findViewById(R.id.ias_new_password);
        newPwdChk = (EditText) findViewById(R.id.ias_new_password_chk);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            // 확인 버튼 클릭시
            case R.id.btn_confirm:
                if(newPwd.getText().toString().length() == 0) {
                    Toast.makeText(SearchPwActivity.this, R.string.request_info_new_pw, Toast.LENGTH_LONG).show();
                    newPwd.requestFocus();
                    return;
                }
                if(newPwdChk.getText().toString().length() == 0) {
                    Toast.makeText(SearchPwActivity.this, R.string.request_info_check_new_pw, Toast.LENGTH_LONG).show();
                    newPwdChk.requestFocus();
                    return;
                }
                if(!newPwd.getText().toString().equals(newPwdChk.getText().toString())) {
                    Toast.makeText(SearchPwActivity.this, R.string.request_pw_not_equal, Toast.LENGTH_LONG).show();
                    newPwd.setText("");
                    newPwdChk.setText("");
                    newPwd.requestFocus();
                    return;
                }
                // call AsynTask to perform network operation on separate thread
                HttpAsyncTask httpTask = new HttpAsyncTask(SearchPwActivity.this);
                httpTask.execute(targetUrl, userId, newPwd.getText().toString());

                break;

            // 취소 버튼 클릭시
            case R.id.btn_cancel:
                Intent intentLogin = new Intent(SearchPwActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                break;
        }

    }

    public String putPw(String url, User user){
        InputStream is = null;
        String result = "";

        try {
            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            JSONObject header = new JSONObject();
            JSONObject data = new JSONObject();

            header.put("_method", "PUT");
            data.put("user_id", user.getUserId());
            data.put("user_pwd", user.getUserPwd());
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
            Log.d(TAG, "********** InputStream" + e.getLocalizedMessage());
        }

        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private SearchPwActivity searchPw;

        HttpAsyncTask(SearchPwActivity searchPwActivity) {
            this.searchPw = searchPwActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            userVo = new User();
            userVo.setUserId(urls[1]);
            userVo.setUserPwd(urls[2]);

            return putPw(urls[0],userVo);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            searchPw.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 데이터 가져오기
                    try {
                        JSONObject jsonObject = new JSONObject(strJson);
                        String data = jsonObject.getString("data");

                        if(data.equals("true")) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(SearchPwActivity.this);
                            alert.setMessage(R.string.password_change_text);
                            alert.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // 로그인 화면 이동
                                    Intent intent = new Intent(SearchPwActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            });

                            AlertDialog alertDialog = alert.create();
                            alertDialog.show();
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
