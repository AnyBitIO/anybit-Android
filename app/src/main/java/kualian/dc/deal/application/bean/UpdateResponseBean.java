package kualian.dc.deal.application.bean;

/**
 * Created by idmin on 2018/3/15.
 */

public class UpdateResponseBean extends ResponseCode{
    public UpdateBean getData() {
        return data;
    }

    public void setData(UpdateBean data) {
        this.data = data;
    }

    private UpdateBean data;
    public static class UpdateBean{
        private String version;
        private String download_url;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }
    }
}
