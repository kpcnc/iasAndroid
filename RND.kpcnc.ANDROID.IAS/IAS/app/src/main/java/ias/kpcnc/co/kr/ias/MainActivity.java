package ias.kpcnc.co.kr.ias;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    String userId;
    String userPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // topic : api를 사용하여 푸시 알림 전송시 같은 토픽명 그룹 전체에 메세지를 발송 할 수 있다.
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();

        // 로그인 시 저장된 ID/PW 호출
        SharedPreferences autoLoginPrefs = getSharedPreferences("autoLoginPrefs", MODE_PRIVATE);
        String userId = autoLoginPrefs.getString("userId", "");
        String userPw = autoLoginPrefs.getString("userPw", "");

        // TODO userId로 사용자 메시지 조회

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(R.string.title_message);

        // tab 추가
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tab01));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tab02));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tab03));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tab04));

        // tab 정렬 지정 - 같은 크기와 비율로 배치 (GRAVITY_FILL)
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter); // PagerAdapter.java 파일에 getCount(), getItem() 차례로 호출

        //ViewPager에서 페이지의 상태가 변경될 때 페이지 변경 이벤트를 TabLayout에 전달하여  탭의 선택 상태를 동기화해주는 역할
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // 탭의 선택 상태가 변경될 때 호출되는 리스너
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                // 메시지 탭에 포지션인 경우
                if(tab.getPosition() == 0) {
                    // Toast.makeText(MainActivity.this, "메시지 탭", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete:
                Toast.makeText(MainActivity.this, "삭제", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.modify_member_info:
                Intent intentMember = new Intent(MainActivity.this, MemberModifyActivity.class);
                startActivity(intentMember);
                break;
            case R.id.search_login_hist:
                Intent intentHistory = new Intent(MainActivity.this, LoginHistoryActivity.class);
                intentHistory.putExtra("userId", userId);
                startActivity(intentHistory);
                break;
            case R.id.device_setting:
                Intent intentDeviceSet = new Intent(MainActivity.this, DeviceSettingActivity.class);
                startActivity(intentDeviceSet);
                break;
            case R.id.main_device_setting:
                Intent intentMainDeviceSet = new Intent(MainActivity.this, MainDeviceSettingActivity.class);
                startActivity(intentMainDeviceSet);
                break;
            case R.id.version_info:
                Intent intentVersion = new Intent(MainActivity.this, VersionActivity.class);
                startActivity(intentVersion);
                break;
            case R.id.notice:
                Intent intentNotice = new Intent(MainActivity.this, NoticeActivity.class);
                startActivity(intentNotice);
                break;
            case R.id.login_set:
                Intent intentLoginSet = new Intent(MainActivity.this, LoginSettingActivity.class);
                startActivity(intentLoginSet);
                break;
            case R.id.alert_set:
                Intent intentAlertSet = new Intent(MainActivity.this, AlarmSettingActivity.class);
                startActivity(intentAlertSet);
                break;
            case R.id.device_lost_set:
                Intent intentDeviceLostSet = new Intent(MainActivity.this, DeviceLostSettingActivity.class);
                startActivity(intentDeviceLostSet);
                break;
            case R.id.account_del:
                Intent intentAccountDel = new Intent(MainActivity.this, AccountDeleteActivity.class);
                startActivity(intentAccountDel);
                break;
            /* XMPP 테스트 */
            case R.id.xmpp_test:
                Intent intentXmppTest = new Intent(MainActivity.this, XmppActivity.class);
                startActivity(intentXmppTest);
                break;
            /* */
        }

    }

}
