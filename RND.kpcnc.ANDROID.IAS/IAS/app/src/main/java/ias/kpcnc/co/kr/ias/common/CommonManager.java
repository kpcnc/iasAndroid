package ias.kpcnc.co.kr.ias.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ias.kpcnc.co.kr.ias.bean.Device;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Hong on 2016-10-31.
 */

public class CommonManager extends Application{
    private static final String TAG = "CommonManager";

//    public String modelNm;
//    public String dveComp;
//    public String dveImei;

    Device deviceVo;

    static Context context;

    public static Context getContext() {
        return context; //자원을 반환.//
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this; //현재 어플리케이션의 자원을 얻어온다
        Fabric.with(this, new Crashlytics());
    }

    public Device getDeviceVo() {
        // 디바이스 정보 가져오기
//        modelNm = Build.MODEL;
//        dveComp = Build.MANUFACTURER;
//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//        dveImei = telephonyManager.getDeviceId();
//
//        Log.d(TAG, "모델명: " + modelNm + "\n제조사: " + dveComp + "\nIMEI: " + dveImei);

        deviceVo.setModelNm(Build.MODEL);
        deviceVo.setDveComp(Build.MANUFACTURER);

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        deviceVo.setDveImei(telephonyManager.getDeviceId());
        Log.d(TAG, "모델명: " + deviceVo.getModelNm() + "\n제조사: " + deviceVo.getDveComp() + "\nIMEI: " + deviceVo.getDveImei());

        return deviceVo;
    }

    /**
     * Network Connected Check
     *
     */
    public void isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            Log.d(TAG, "################ network connected");
        else
            Toast.makeText(this, "인터넷 연결이 필요합니다.", Toast.LENGTH_LONG).show();
    }

    /**
     * 대상 URL에 파라미터를 전송한 후 응답을 InputStream으로 리턴한다.
     *
     * @return InputStream
     */
    public InputStream reqHttp(String url, String json) throws IOException {
        InputStream is = null;
        String result = "";

        URL connUrl = new URL(url);
        Log.d(TAG, "################ Url: " + connUrl);
        HttpURLConnection httpConn = (HttpURLConnection) connUrl.openConnection();

        // 서버 Response(응답) Data를 json 형식의 타입으로 요청
        httpConn.setRequestProperty("Accept", "application/json");
        // 타입설정 (application/json) 형식으로 전송 (Request Body 전달시 application/json로 서버에 전달.)
        httpConn.setRequestProperty("Content-type", "application/json");
        httpConn.setDoInput(true);
        httpConn.setDoOutput(true);
        httpConn.setUseCaches(false);

        Log.d(TAG, "################ json Data: " + json);

        OutputStream out = httpConn.getOutputStream();
        out.write(json.getBytes());
        out.flush();

        if (out != null)
            out.close();

        try {
            is = httpConn.getInputStream();
            // convert inputstream to string
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            httpConn.disconnect();
        }
        return is;
    }

    /**
     * 암호화 된 값을 받아 복호화 하여 리턴한다.
     *
     */

}
