package ias.kpcnc.co.kr.ias.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import ias.kpcnc.co.kr.ias.R;

/**
 * Created by Hong on 2016-11-01.
 */

public class CustomDialog extends Dialog {

    private EditText newPwd;
    private EditText newPwdChk;
    private TextView btnCancel;
    private TextView btnChange;

    private View.OnClickListener cancelButton;
    private View.OnClickListener changeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pwd_change);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        newPwd = (EditText) findViewById(R.id.new_password);
        newPwdChk = (EditText) findViewById(R.id.new_password_chk);
        btnCancel = (TextView) findViewById(R.id.password_cancel);
        btnChange = (TextView) findViewById(R.id.password_change);

        // TextView에 클릭 이벤트 처리
        btnCancel.setOnClickListener(cancelButton);
        btnChange.setOnClickListener(changeButton);
    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public CustomDialog(Context context, View.OnClickListener cancelListener,
                        View.OnClickListener changeListener) {

        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.cancelButton = cancelListener;
        this.changeButton = changeListener;
    }

    public String dialogNewPwd () {
        return newPwd.getText().toString();
    }

    public String dialogNewPwdChk () {
        return newPwdChk.getText().toString();
    }

    public void validationPwd() {
        newPwd.setText("");
        newPwdChk.setText("");
        newPwd.requestFocus();
    }

}