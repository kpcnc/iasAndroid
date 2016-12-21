package ias.kpcnc.co.kr.ias;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * Created by Hong on 2016-10-25.
 */

public class TermsActivity extends Activity{

    private static final String TAG = "TermsActivity";

    CheckBox svcTerms;
    CheckBox personTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        svcTerms = (CheckBox) findViewById(R.id.chk_svc_terms);
        personTerms = (CheckBox) findViewById(R.id.chk_person_terms);
    }

    public void onClick(View view) {

        if(!svcTerms.isChecked()) {
            Toast.makeText(TermsActivity.this, R.string.request_service_terms, Toast.LENGTH_SHORT).show();
        } else if (!personTerms.isChecked()) {
            Toast.makeText(TermsActivity.this, R.string.request_person_terms, Toast.LENGTH_SHORT).show();
        } else if (svcTerms.isChecked() && personTerms.isChecked()) {
            Intent intent = new Intent(TermsActivity.this, SigninActivity.class);
            startActivity(intent);
        }
    }

}
