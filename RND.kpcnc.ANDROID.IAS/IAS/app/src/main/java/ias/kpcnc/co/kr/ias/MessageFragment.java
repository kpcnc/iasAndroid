package ias.kpcnc.co.kr.ias;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Hong on 2016-10-27.
 */

public class MessageFragment extends Fragment {
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab01, container, false);

        listView= (ListView) view.findViewById(R.id.listViewMsg);

/*        String[] items = new String[] {"Item 1", "Item 2", "Item 3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);

        listView.setAdapter(adapter);*/

        return view;

      //  return inflater.inflate(R.layout.fragment_tab01, container, false);
    }
}
