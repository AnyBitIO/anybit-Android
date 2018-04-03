package kualian.dc.deal.application.bean;

/**
 * Created by idmin on 2018/3/24.
 */

public class LogBean {
    private String logVersion;
    private String logContent;
    private boolean isOpen;

    public String getLogVersion() {
        return logVersion;
    }

    public void setLogVersion(String logVersion) {
        this.logVersion = logVersion;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
