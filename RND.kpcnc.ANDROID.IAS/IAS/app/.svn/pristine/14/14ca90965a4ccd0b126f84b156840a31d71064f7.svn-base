package ias.kpcnc.co.kr.ias;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.zcw.togglebutton.ToggleButton;

/**
 * Created by Hong on 2016-10-31.
 */

public class AlarmSettingActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_setting);

        ToggleButton msgToggleBtn = (ToggleButton) findViewById(R.id.msg_alarm_toggle);
        ToggleButton noticeToggleBtn = (ToggleButton) findViewById(R.id.netice_alarm_toggle);

        // 메시지 알람 설정
        msgToggleBtn.toggle();
        msgToggleBtn.toggle(false);
        msgToggleBtn.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
            @Override
            public void onToggle(boolean on) {
                if(on){
                    Toast.makeText(AlarmSettingActivity.this, "메시지 토글 on", Toast.LENGTH_SHORT).show();

                    // TODO 메시지 알람 구현
                }

            }
        });

        // 공지사항 알람 설정
        noticeToggleBtn.toggle();
        noticeToggleBtn.toggle(false);
        noticeToggleBtn.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
            @Override
            public void onToggle(boolean on) {
                if(on){
                    Toast.makeText(AlarmSettingActivity.this, "공지사항 토글 on", Toast.LENGTH_SHORT).show();

                    // TODO 공지사항 알람 구현
                }

            }
        });
    }
}
