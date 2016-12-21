package ias.kpcnc.co.kr.ias.bean;

/**
 * Created by Hong on 2016-10-25.
 */

public class Device {

    private String userId;
    private String modelNm;
    private String dveComp;
    private String dveImei;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    private String tokenId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getModelNm() {
        return modelNm;
    }

    public void setModelNm(String modelNm) {
        this.modelNm = modelNm;
    }

    public String getDveComp() {
        return dveComp;
    }

    public void setDveComp(String dveComp) {
        this.dveComp = dveComp;
    }

    public String getDveImei() {
        return dveImei;
    }

    public void setDveImei(String dveImei) {
        this.dveImei = dveImei;
    }
}
