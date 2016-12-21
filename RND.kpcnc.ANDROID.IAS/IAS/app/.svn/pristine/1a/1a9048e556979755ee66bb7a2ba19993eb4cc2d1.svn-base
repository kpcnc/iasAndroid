package ias.kpcnc.co.kr.ias;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.zcw.togglebutton.ToggleButton;

/**
 * Created by Hong on 2016-10-31.
 */

public class LoginSettingActivity extends Activity{
    private static final String TAG = "LoginSettingActivity";

    SharedPreferences autoLoginPrefs;
    SharedPreferences.Editor editor;
    String userId;
    String userPw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_setting);

        ToggleButton toggleBtn = (ToggleButton) findViewById(R.id.auto_login_toggle);

        // 로그인 시 저장된 회원정보 호출
        SharedPreferences userInfoPrefs = getSharedPreferences("userInfoPrefs", MODE_PRIVATE);
        userId = userInfoPrefs.getString("userId", "");
        userPw = userInfoPrefs.getString("userPw", "");

        autoLoginPrefs = getSharedPreferences("autoLoginPrefs", MODE_PRIVATE);
        editor = autoLoginPrefs.edit();
        if(autoLoginPrefs.getBoolean("autoLogin", false)) {
            toggleBtn.setToggleOn();
        } else {
            toggleBtn.setToggleOff();
        }

//        toggleBtn.toggle();
//        toggleBtn.toggle(true);

        toggleBtn.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
            @Override
            public void onToggle(boolean on) {
                if(on){
                    // 자동 로그인 설정 - SET SharedPreference "autoLoginPrefs" ID/PW/CHECK
                    editor.putString("userId", userId);
                    editor.putString("userPw", userPw);
                    editor.putBoolean("autoLogin", true);
                    editor.commit();

                    Toast.makeText(LoginSettingActivity.this, R.string.auto_login_on_text, Toast.LENGTH_SHORT).show();
                } else {
                    // 자동 로그인 해제 - SET SharedPreference "autoLoginPrefs" removeAll

                    editor.clear();
                    editor.commit();

                    Toast.makeText(LoginSettingActivity.this, R.string.auto_login_off_text, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
