package ias.kpcnc.co.kr.ias;

/**
 * Created by Hong on 2016-10-11.
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseInsIDService";

    // [START refresh_token] Registration Token의 신규 발급, 순환, 업데이트를 처리
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    // Registration Token API Server 전송
    private void sendRegistrationToServer(String token) {

        Log.d(TAG, "new token: " + token);

        HttpURLConnection connection;

        try {
            URL url = new URL("");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);   // 서버에 데이터를 보낼따, Post의 경우 사용
            connection.setDoInput(true );   // 서버에서 데이터를 가져올때
            connection.setRequestMethod("POST");

            StringBuffer buffer = new StringBuffer();
            buffer = buffer.append("Token").append("=").append(token);

            OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
            osw.write(buffer.toString());
            osw.flush();    // 서버에 작성
            osw.close();    // 객체 닫음

            // 서버에서 값을 받아오지 않아도 작성
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer buffer1 = new StringBuffer();
            String line = null;

            while ((line = reader.readLine()) != null) {
                buffer1.append(line);
            }

            connection.disconnect();

        }catch (Exception e) {
            e.printStackTrace();
        }



        // Add custom implementation, as needed.

//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("Token", token)
//                .build();
//
//        //request
//        Request request = new Request.Builder()
//                .url("http://서버주소/fcm/register.php")  토큰을 받아서 저장할 서버의 주소와 php파일
//                .post(body)
//                .build();
//
//        try {
//            client.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
