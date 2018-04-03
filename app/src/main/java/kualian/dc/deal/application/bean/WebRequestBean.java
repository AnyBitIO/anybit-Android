package kualian.dc.deal.application.bean;

/**
 * Created by idmin on 2018/3/22.
 */

public class WebRequestBean extends RequestBean {
    private BodyBean body;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        private String pageType;

        public String getPageType() {
            return pageType;
        }

        public void setPageType(String pageType) {
            this.pageType = pageType;
        }
    }
}
