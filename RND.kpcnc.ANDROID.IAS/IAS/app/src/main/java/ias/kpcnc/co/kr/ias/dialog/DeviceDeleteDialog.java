package ias.kpcnc.co.kr.ias.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import ias.kpcnc.co.kr.ias.R;

/**
 * Created by Hong on 2016-11-04.
 */

public class DeviceDeleteDialog extends Dialog {
   //private EditText clearPwd;
    private TextView btnCancel;
    private TextView btnClear;

    private View.OnClickListener cancelButton;
    private View.OnClickListener clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_device_delete);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        //clearPwd = (EditText) findViewById(R.id.deletePwd);
        btnCancel = (TextView) findViewById(R.id.deviceCancel);
        btnClear = (TextView) findViewById(R.id.deviceClear);

        // TextView에 클릭 이벤트 처리
        btnCancel.setOnClickListener(cancelButton);
        btnClear.setOnClickListener(clearButton);
    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public DeviceDeleteDialog(Context context, View.OnClickListener delCancelListener,
                        View.OnClickListener clearListener) {

        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.cancelButton = delCancelListener;
        this.clearButton = clearListener;
    }

   // public String dialogNewPwd () {
    //    return clearPwd.getText().toString();
    //}
}
