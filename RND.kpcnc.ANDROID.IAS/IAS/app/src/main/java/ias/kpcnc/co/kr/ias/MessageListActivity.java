package ias.kpcnc.co.kr.ias;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hong on 2016-12-13.
 */

public class MessageListActivity extends Activity{
    private ListView listView;
    private MyAdapter myAdapter;
    private ArrayList<listItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_message);

        // listItem 클래스 - ListView의 아이템에 본인이 넣고자 하는 데이터들의 묶음
        list = new ArrayList<listItem>();
        listView = (ListView) findViewById(R.id.listViewMsg);
        myAdapter = new MyAdapter(list);
        listView.setAdapter(myAdapter);

        // 아이템 추가
        list.add(new listItem(R.drawable.ias_icon_96, "IAS", "IAS 메시지 전송내용 입니다."));
        list.add(new listItem(R.drawable.ias_icon_96, "KT", "KT 메시지 전송 내용 입니다."));
        list.add(new listItem(R.drawable.ias_icon_96, "긴급재난문자", "오늘 11:53분 경북 경주 남남서쪽 10km 지역에서 발생이 되었습니다."));
    }

    // ListView의 아이템에 들어가는 커스텀된 데이터들의 묶음
    public class listItem {
        private int profile;
        private String name;
        private String lstMsg;

        public listItem(int profile, String name, String lstMsg) {
            this.profile = profile;
            this.name = name;
            this.lstMsg = lstMsg;
        }
    }

    // Adapter
    public class MyAdapter extends BaseAdapter {
        private ArrayList<listItem> list;

        public MyAdapter(ArrayList<listItem> list) {
            // Adapter 생성시 list값을 넘겨 받는다.
            this.list = list;
        }

        @Override
        public int getCount() {
            // list 사이즈 만큼 반환
            return list.size();
        }

        @Override
        public listItem getItem(int position) {
            // 현재 position에 따른 list 값을 반환 시켜준다.
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            View view = convertView;

            if(view == null) {
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.fragment_tab01, parent, false);

                ImageView profile = (ImageView)view.findViewById(R.id.profile_thumbnail);
                TextView name = (TextView)view.findViewById(R.id.partner_name);
                TextView lstMsg = (TextView)view.findViewById(R.id.lst_msg);

                profile.setImageResource(getItem(pos).profile);
                name.setText(getItem(pos).name);
                lstMsg.setText(getItem(pos).lstMsg);
            }
            return view;
        }
    }
}
