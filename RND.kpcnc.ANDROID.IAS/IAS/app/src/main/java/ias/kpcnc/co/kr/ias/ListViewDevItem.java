package ias.kpcnc.co.kr.ias;

/**
 * Created by Hong on 2016-11-07.
 */

public class ListViewDevItem {
    private String dveId;
    private String modelNm;

    public String getModelNm() {
        return modelNm;
    }

    public void setModelNm(String modelNm) {
        this.modelNm = modelNm;
    }

    public String getDeviceId() {return dveId;}

    public void setDeviceId(String deviceId) {
        this.dveId = deviceId;
    }

    public ListViewDevItem(String modelNm, String deviceId) {
        this.modelNm = modelNm;
        this.dveId = deviceId;
    }
}
