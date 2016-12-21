package ias.kpcnc.co.kr.ias.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ias.kpcnc.co.kr.ias.common.CommonManager;

/**
 * Created by Hong on 2016-12-08.
 */

public class PropertyManager {
    private static final String ALARM_BADGE_NUMBER = "alarm_badge_number"; // 배지

    private static PropertyManager instance;

    //FCM알람관련 뱃지카운터.//
    private static int badgeNumber = 0;

    //공유 프래퍼런스 생성//
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEdittor;

    private PropertyManager() {
        Context context = CommonManager.getContext(); //현재 앱의 자원을 얻어온다.//

        //프래퍼런스를 사용하도록 설정//
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEdittor = mPrefs.edit();
    }

    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }

        return instance;
    }

    //프래퍼런스에 저장된 값들을 불러온다
    public int get_badge_number() {
        return mPrefs.getInt(ALARM_BADGE_NUMBER, badgeNumber);
    }

    public void setBadge_number(int badge_number) {
        mEdittor.putInt(ALARM_BADGE_NUMBER, badge_number);
        mEdittor.commit();
    }
}
