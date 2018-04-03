package kualian.dc.deal.application.bean;

/**
 * Created by idmin on 2018/3/23.
 */

public class WebResponse {
    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    private DataBean data;
    public static class DataBean{
        private String pageUrl;

        public String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
        }
    }
}
