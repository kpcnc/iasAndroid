package ias.kpcnc.co.kr.ias;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Hong on 2016-10-31.
 */

public class DeviceLostSettingActivity extends Activity {

    Spinner devLostSpinner;
    String setDevice;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_lost_setting);

        initSpinner();
    }

    public void initSpinner() {
        String[] strTextList = {"Galaxy A7", "Galaxy Note3", "Galaxy A5"};
        arrayList = new ArrayList<String>();

        for(int i=0; i<strTextList.length; i++) {
           arrayList.add(strTextList[i]);
        }

        ArrayAdapter<String> adapter;
        adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        devLostSpinner= (Spinner)findViewById(R.id.devLostSpinner);
        devLostSpinner.setAdapter(adapter);
    }
}
