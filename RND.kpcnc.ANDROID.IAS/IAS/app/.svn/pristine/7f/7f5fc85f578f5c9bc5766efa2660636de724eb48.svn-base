package ias.kpcnc.co.kr.ias;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import ias.kpcnc.co.kr.ias.bean.User;
import ias.kpcnc.co.kr.ias.common.CommonManager;
import ias.kpcnc.co.kr.ias.dialog.CustomDialog;

/**
 * Created by Hong on 2016-10-28.
 */

public class MemberModifyActivity extends Activity {
    private static final String TAG = "MemberModifyActivity";

    LinearLayout linearLayout;
    InputMethodManager inputMethodManager;

    // 공통 함수 호출
    private CommonManager commonManager;
    private CustomDialog customDialog;
    static String strJson = "";
    private static final String CRYPTO_SEED_PASSWORD = "1234!@#$";

    EditText editId, editPw, editNm, editEmail;

    String userUrl = "http://iasapi.kpcnc.co.kr:8051/iasapi/members/member";
    String pwdUrl = "http://iasapi.kpcnc.co.kr:8051/iasapi/members/pwd";
    String prefUserId;
    String prefUserPw;

    User userVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        // 화면에서 입력폼 외에 다른 화면 클릭시 키보드 숨기기
        linearLayout = (LinearLayout)findViewById(R.id.activity_modify);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(linearLayout.getWindowToken(), 0);
            }
        });

        // 로그인 시 저장된 회원정보 호출
        SharedPreferences userInfoPrefs = getSharedPreferences("userInfoPrefs", MODE_PRIVATE);
        prefUserId = userInfoPrefs.getString("userId", "");
        prefUserPw = userInfoPrefs.getString("userPw", "");

        editId = (EditText)findViewById(R.id.modify_id);
        editPw = (EditText)findViewById(R.id.modify_pw);
        editNm = (EditText)findViewById(R.id.modify_name);
        editEmail = (EditText)findViewById(R.id.modify_email);

        // call AsynTask to perform network operation on separate thread
        HttpAsyncTaskUser httpTaskUser = new HttpAsyncTaskUser(MemberModifyActivity.this);
        httpTaskUser.execute(userUrl, prefUserId);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            // 비밀번호 변경
            case R.id.password_change:
                customDialog = new CustomDialog(this, cancelListener, changeListener);
                customDialog.show();
                break;
            // 회원정보 저장
            case R.id.btn_save:
                // call AsynTask to perform network operation on separate thread
                HttpAsyncTaskModUser httpTask = new HttpAsyncTaskModUser(MemberModifyActivity.this);
                httpTask.execute(userUrl, prefUserId, editNm.getText().toString(), editEmail.getText().toString());
                break;
        }
    }

    /**
     * CustomDialog OnClickListener - cancel button
     */
    private View.OnClickListener cancelListener = new View.OnClickListener() {
        public void onClick(View view) {
            customDialog.dismiss();
        }
    };

    /**
     * CustomDialog OnClickListener - change button
     */
    private View.OnClickListener changeListener = new View.OnClickListener() {
        public void onClick(View view) {
            // CustomDialog 메소드 호출 하여 값 세팅
            String newPassword = customDialog.dialogNewPwd();
            String newPasswordChk = customDialog.dialogNewPwdChk();

            // new password, new password check
            if(!newPassword.equals(newPasswordChk)) {
                Toast.makeText(MemberModifyActivity.this, R.string.request_pw_not_equal, Toast.LENGTH_LONG).show();
                customDialog.validationPwd();
                return;
            }
            customDialog.dismiss();

            editPw.setText(newPassword);

            // call AsynTask to perform network operation on separate thread
            HttpAsyncTaskPwd httpTaskPwd = new HttpAsyncTaskPwd(MemberModifyActivity.this);
            httpTaskPwd.execute(pwdUrl, prefUserId, newPassword);
        }
    };

    /**
     * 회원정보 조회 Data - url, json 공통함수 호출
     */
    public String getUser(String url, User user){
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
     * 회원정보 조회 thread
     */
    private class HttpAsyncTaskUser extends AsyncTask<String, Void, String> {

        private MemberModifyActivity memModify;

        HttpAsyncTaskUser(MemberModifyActivity memberModifyActivity) {
            this.memModify = memberModifyActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            userVo = new User();
            userVo.setUserId(urls[1]);

            return getUser(urls[0],userVo);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            memModify.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SecretKeySpec sks = null;

                    try {
                        String userNm = "";
                        String userEmail = "";

                        JSONObject jsonObject = new JSONObject(strJson);
                        JSONArray json = new JSONArray(jsonObject.getString("data"));
                        for(int i=0; i<json.length(); i++) {
                            JSONObject jsonData = json.getJSONObject(i); // JSONObject 추출

                            userNm += jsonData.getString("user_nm");
                            userEmail += jsonData.getString("user_email");
                        }

                        // TODO 암복호화 처리 구현
                        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
                        sr.setSeed("any data used as random seed".getBytes());
                        KeyGenerator kg = KeyGenerator.getInstance("AES");
                        kg.init(128, sr);
                        sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");

                        byte[] encodedBytes = null;
                        try {
                            Cipher c = Cipher.getInstance("AES");
                            c.init(Cipher.ENCRYPT_MODE, sks);
                            encodedBytes = c.doFinal(prefUserPw.getBytes());
                        } catch (Exception e) {
                            Log.e(TAG, "AES encryption error");
                        }

                        byte[] decodedBytes = null;
                        try {
                            Cipher c = Cipher.getInstance("AES");
                            c.init(Cipher.DECRYPT_MODE, sks);
                            decodedBytes = c.doFinal(encodedBytes);
                        } catch (Exception e) {
                            Log.e(TAG, "AES decryption error");
                        }

                        Log.d(TAG, "#############" +new String(decodedBytes));

                        editId.setText(prefUserId);
                        editPw.setText(new String(decodedBytes));
                        editNm.setText(userNm);
                        editEmail.setText(userEmail);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 비밀번호 변경 Data - url, json 공통함수 호출
     */
    public String putPassword(String url, User user){
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
            Log.d("********** InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 비밀번호 변경 thread
     */
    private class HttpAsyncTaskPwd extends AsyncTask<String, Void, String> {

        private MemberModifyActivity memModify;

        HttpAsyncTaskPwd(MemberModifyActivity memberModifyActivity) {
            this.memModify = memberModifyActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            userVo = new User();
            userVo.setUserId(urls[1]);
            userVo.setUserPwd(urls[2]);

            return putPassword(urls[0],userVo);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            memModify.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(strJson);
                        String resultData = jsonObject.getString("data");

                        // 패스워드 변경 된 경우 Toast 알람
                        if(resultData.equals("true")) {
                            Toast.makeText(MemberModifyActivity.this, R.string.password_change_text, Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 회정 정보 수정 Data - url, json 공통함수 호출
     */
    public String putUser(String url, User user){
        InputStream is = null;
        String result = "";

        try {
            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            JSONObject header = new JSONObject();
            JSONObject data = new JSONObject();

            header.put("_method", "PUT");
            data.put("user_id", user.getUserId());
            data.put("user_nm", user.getUserNm());
            data.put("user_email", user.getUserEmail());
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
     * 회원정보 수정 thread
     */
    private class HttpAsyncTaskModUser extends AsyncTask<String, Void, String> {

        private MemberModifyActivity memModify;

        HttpAsyncTaskModUser(MemberModifyActivity memberModifyActivity) {
            this.memModify = memberModifyActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            userVo = new User();
            userVo.setUserId(urls[1]);
            userVo.setUserNm(urls[2]);
            userVo.setUserEmail(urls[3]);

            return putUser(urls[0],userVo);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            memModify.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(strJson);
                        String resultData = jsonObject.getString("data");

                        // 회원정보 수정 경우 Toast 알람
                        if(resultData.equals("true")) {
                            Toast.makeText(MemberModifyActivity.this, R.string.user_info_change_text, Toast.LENGTH_SHORT).show();
                            // 메인 개인정보 수정 화면 이동
                            MemberModifyActivity.this.finish();
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
