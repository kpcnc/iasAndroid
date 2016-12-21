package ias.kpcnc.co.kr.ias;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Hong on 2016-10-26.
 */

public class SearchIdActivity extends Activity{

    TextView schUserId;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_id);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        schUserId = (TextView) findViewById(R.id.search_user_id);
        schUserId.setText(userId);
    }

    public void onClick(View view) {
        Intent intent = new Intent(SearchIdActivity.this, LoginActivity.class);
        // 조회 된 ID put
        intent.putExtra("findId", userId);
        startActivity(intent);
    }
}
