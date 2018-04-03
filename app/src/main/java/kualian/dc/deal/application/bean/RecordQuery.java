package kualian.dc.deal.application.bean;

/**
 * Created by idmin on 2018/3/5.
 */

public class RecordQuery extends RequestBean {
    private BodyBean body;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        private String coinAddr;
        private String coinType;

        public String getCoinAddr() {
            return coinAddr;
        }

        public void setCoinAddr(String coinAddr) {
            this.coinAddr = coinAddr;
        }

        public String getCoinType() {
            return coinType;
        }

        public void setCoinType(String coinType) {
            this.coinType = coinType;
        }

        public BodyBean(String coinAddr, String coinType) {

            this.coinAddr = coinAddr;
            this.coinType = coinType;
        }

    }
}
