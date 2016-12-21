package ias.kpcnc.co.kr.ias;

/**
 * Created by Hong on 2016-11-02.
 */

public class ListViewItem {
    private String deviceId;
    private String userId;
    private String modelNm;

    public String getModelNm() {
        return modelNm;
    }

    public void setModelNm(String modelNm) {
        this.modelNm = modelNm;
    }

    public String getDeviceId() {return deviceId;}

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ListViewItem (String deviceId) {
        this.deviceId = deviceId;
    }

    public ListViewItem(String modelNm, String deviceId) {
        this.modelNm = modelNm;
        this.deviceId = deviceId;
    }
}
