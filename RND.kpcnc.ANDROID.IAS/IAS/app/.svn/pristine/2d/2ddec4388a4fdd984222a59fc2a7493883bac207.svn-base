package ias.kpcnc.co.kr.ias;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Hong on 2016-10-27.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
    // getItem()은 adapter에게 ViewPager에 공급할 Fragment를 반환(제공)해주는 역할

        switch (position) {
            case 0:
                MessageFragment tab1 = new MessageFragment();
                return tab1;
            case 1:
                PersonalFragment tab2 = new PersonalFragment();
                return tab2;
            case 2:
                DeviceFragment tab3 = new DeviceFragment();
                return tab3;
            case 3:
                SettingFragment tab4 = new SettingFragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    // getCount()는 page의 갯수를 알려주는 기능
}
