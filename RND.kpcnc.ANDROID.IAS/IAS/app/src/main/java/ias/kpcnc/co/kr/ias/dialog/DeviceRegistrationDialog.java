package ias.kpcnc.co.kr.ias.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ias.kpcnc.co.kr.ias.R;

/**
 * Created by Hong on 2016-11-02.
 */

public class DeviceRegistrationDialog extends Dialog {

    private EditText deviceNm;
    private EditText devicePw;
    private TextView btnCancel;
    private TextView btnRegistration;

    private View.OnClickListener cancelButton;
    private View.OnClickListener registButton;

    String modelNm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_device_register);

        deviceNm = (EditText)findViewById(R.id.device_name);
        devicePw = (EditText)findViewById(R.id.device_password);
        btnCancel = (TextView) findViewById(R.id.device_cancel);
        btnRegistration = (TextView) findViewById(R.id.device_registration);

        modelNm = Build.MODEL;
        deviceNm.setText(modelNm);

        // TextView에 클릭 이벤트 처리
        btnCancel.setOnClickListener(cancelButton);
        btnRegistration.setOnClickListener(registButton);
    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public DeviceRegistrationDialog(Context context, View.OnClickListener cancelListener,
                        View.OnClickListener registListener) {

        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.cancelButton = cancelListener;
        this.registButton = registListener;
    }

    public String dialogPw () {
        return devicePw.getText().toString();
    }

    public void validationPwd() {
        devicePw.setText("");
        devicePw.requestFocus();
    }
}
