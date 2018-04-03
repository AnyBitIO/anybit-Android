package kualian.dc.deal.application.bean;

/**
 * Created by idmin on 2018/3/3.
 */

public class ResponseCode {
    // 返回码，1：成功 2：失败
    private int rtnCode;
    // 错误码   1xxxx
    private String errCode;
    // 错误消息
    private String errMsg;

    public int getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(int rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
