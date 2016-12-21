package ias.kpcnc.co.kr.ias.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hong on 2016-11-02.
 */

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> notice1 = new ArrayList<String>();
        notice1.add("안녕하세요.\nIAS앱 담당자 입니다.\nIAS앱이 출시 되었습니다.");

        List<String> notice2 = new ArrayList<String>();
        notice2.add("안녕하세요.\nIAS앱 담당자 입니다.\n이번 장애로 이용에 불편을 드려서 죄송합니다.");

        List<String> notice3 = new ArrayList<String>();
        notice3.add("안녕하세요.\nIAS앱 담당자 입니다.\n이번 업데이트에는 안정화 작업을 통하여 더욱 편리한 기능을 제공하도록 추가 되었습니다.");

        expandableListDetail.put("IAS앱 출시 되었습니다.", notice1);
        expandableListDetail.put("장애 발생으로 불편을 드려서 죄송합니다.", notice2);
        expandableListDetail.put("IAS앱 1.0버전이 업데이트 되었습니다.", notice3);
        return expandableListDetail;
    }
}
