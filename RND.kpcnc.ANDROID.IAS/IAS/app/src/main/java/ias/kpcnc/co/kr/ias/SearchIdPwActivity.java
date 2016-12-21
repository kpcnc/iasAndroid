package ias.kpcnc.co.kr.ias;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
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

public class SearchIdPwActivity extends Activity{
    private static final String TAG = "SearchIdPwActivity";

    // 공통 함수 호출
    private CommonManager commonManager;
    static String strJson = "";

    String targetUrl = "http://iasapi.kpcnc.co.kr:8051/iasapi/members/member";

    LinearLayout linearLayout;
    InputMethodManager inputMethodManager;

    EditText schIdName;
    EditText schIdEmail;
    EditText schPwdId;
    EditText schPwName;
    EditText schPwEmail;

    User userVo;

    String flagIdPw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_pw_search);

        // 화면에서 입력폼 외에 다른 화면 클릭시 키보드 숨기기
        linearLayout = (LinearLayout) findViewById(R.id.activity_id_pw_search);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(linearLayout.getWindowToken(), 0);
            }
        });

        schIdName = (EditText) findViewById(R.id.search_id_name);
        schIdEmail = (EditText) findViewById(R.id.search_id_email);
        schPwdId = (EditText) findViewById(R.id.search_pw_id);
        schPwName = (EditText) findViewById(R.id.search_pw_name);
        schPwEmail = (EditText) findViewById(R.id.search_pw_email);
    }

    public void onClick(View view) {

        switch (view.getId()) {
            // 아이디찾기 버튼 클릭시
            case R.id.btn_id_search:
                if(schIdName.getText().toString().length() == 0) {
                    Toast.makeText(SearchIdPwActivity.this, R.string.request_info_name, Toast.LENGTH_SHORT).show();
                    schIdName.requestFocus();
                    return;
                }
                if(schIdEmail.getText().toString().length() == 0) {
                    Toast.makeText(SearchIdPwActivity.this, R.string.request_info_email, Toast.LENGTH_SHORT).show();
                    schIdEmail.requestFocus();
                    return;
                }
                flagIdPw = "schId";

                HttpAsyncTask httpTaskId = new HttpAsyncTask(SearchIdPwActivity.this);
                httpTaskId.execute(targetUrl, schIdName.getText().toString(), schIdEmail.getText().toString());
            break;
            // 비밀번호찾기 버튼 클릭시
            case R.id.btn_pw_search:

                if(schPwdId.getText().toString().length() == 0) {
                    Toast.makeText(SearchIdPwActivity.this, R.string.request_info_id, Toast.LENGTH_SHORT).show();
                    schPwdId.requestFocus();
                    return;
                }
                if(schPwName.getText().toString().length() == 0) {
                    Toast.makeText(SearchIdPwActivity.this, R.string.request_info_name, Toast.LENGTH_SHORT).show();
                    schPwName.requestFocus();
                    return;
                }
                if(schPwEmail.getText().toString().length() == 0) {
                    Toast.makeText(SearchIdPwActivity.this, R.string.request_info_email, Toast.LENGTH_SHORT).show();
                    schPwEmail.requestFocus();
                    return;
                }
                flagIdPw = "schPw";

                HttpAsyncTask httpTaskPw = new HttpAsyncTask(SearchIdPwActivity.this);
                httpTaskPw.execute(targetUrl, schPwdId.getText().toString(), schPwName.getText().toString(), schPwEmail.getText().toString());
                break;
        }
    }

    public String getIdPw(String url, User user) {
        InputStream is = null;
        String result = "";

        try {
            // build jsonObject
            JSONObject jsonObject;

            // 아이디 찾기
            if(flagIdPw.equals("schId")) {
                jsonObject = new JSONObject();
                JSONObject header = new JSONObject();
                JSONObject data = new JSONObject();

                header.put("_method", "GET");
                data.put("user_nm", user.getUserNm());
                data.put("user_email", user.getUserEmail());
                jsonObject.put("header", header);
                jsonObject.put("data", data);

                String json = jsonObject.toString();
                commonManager = (CommonManager)getApplicationContext();
                // commonManager callback inputStream
                is = commonManager.reqHttp(url, json);

                if(is != null)
                    result = convertInputStreamToString(is);
                else
                    result = "Did not work";

            // 비밀번호 찾기
            } else if(flagIdPw.equals("schPw")) {
                jsonObject = new JSONObject();
                JSONObject header = new JSONObject();
                JSONObject data = new JSONObject();

                header.put("_method", "GET");
                data.put("user_id", user.getUserId());
                data.put("user_nm", user.getUserNm());
                data.put("user_email", user.getUserEmail());
                jsonObject.put("header", header);
                jsonObject.put("data", data);

                String json= jsonObject.toString();

                commonManager = (CommonManager)getApplicationContext();
                // commonManager callback inputStream
                is = commonManager.reqHttp(url, json);

                if(is != null)
                    result = convertInputStreamToString(is);
                else
                    result = "Did not work";
            } // end if~else
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            Log.d("##### InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private SearchIdPwActivity schIdPwActivity;
        String userId="";

        HttpAsyncTask(SearchIdPwActivity searchIdPwActivity) {
            this.schIdPwActivity = searchIdPwActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            userVo = new User();

            // Search ID
            if(flagIdPw.equals("schId")) {
                userVo.setUserNm(urls[1]);
                userVo.setUserEmail(urls[2]);

            // Search PW
            } else if(flagIdPw.equals("schPw")) {
                userVo.setUserId(urls[1]);
                userVo.setUserNm(urls[2]);
                userVo.setUserEmail(urls[3]);
            }

            return getIdPw(urls[0],userVo);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            strJson = result;
            schIdPwActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 아이디 찾기
                    if (flagIdPw.equals("schId")) {
                        try {
                            String findId ="";

                            // result = "data"의 형식이 배열[]로 들어옴 (배열 갯수 1건)
                            JSONObject jsonObject = new JSONObject(strJson);
                            JSONArray json = new JSONArray(jsonObject.getString("data"));
                            for(int i=0; i<json.length(); i++) {
                                JSONObject jsonData = json.getJSONObject(i); // JSONObject 추출
                                findId += jsonData.getString("user_id");
                            }

                            // 회원 정보 없을 시 Toast
                            if(findId.isEmpty()){
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SearchIdPwActivity.this);

                                // AlertDialog set
                                alertDialogBuilder
                                        .setMessage(R.string.info_not_found_text)
                                        .setNeutralButton("확인", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                // AlertDialog 생성
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            } else {
                                Intent intentId = new Intent(SearchIdPwActivity.this, SearchIdActivity.class);
                                // 조회 된 ID put
                                intentId.putExtra("userId", findId);
                                startActivity(intentId);
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        flagIdPw = "";  // flag clear
                    // 비밀번호 찾기
                    } else if(flagIdPw.equals("schPw")) {
                        try {
                            // 회원 정보 없을 시 Toast
                            String findId ="";
                            // result = "data"의 형식이 배열[]로 들어옴 (배열 갯수 1건)
                            JSONObject jsonObject = new JSONObject(strJson);
                            JSONArray json = new JSONArray(jsonObject.getString("data"));
                            for(int i=0; i<json.length(); i++) {
                                JSONObject jsonData = json.getJSONObject(i); // JSONObject 추출
                                findId += jsonData.getString("user_id");
                            }
                            // 회원 정보 없을 시 Toast
                            if(findId.isEmpty()){
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SearchIdPwActivity.this);

                                // AlertDialog set
                                alertDialogBuilder
                                        .setMessage(R.string.info_not_found_text)
                                        .setNeutralButton("확인", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                // AlertDialog 생성
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            } else {
                                Intent intentPw = new Intent(SearchIdPwActivity.this, SearchPwActivity.class);
                                intentPw.putExtra("findId", findId);
                                startActivity(intentPw);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        flagIdPw = "";  // flag clear
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