package kualian.dc.deal.application.bean;

/**
 * Created by idmin on 2018/3/5.
 */

public class TradeBean extends RequestBean{
    private BodyBean body;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        private String fromAddr;
        private String coinType;
        private String toAddr;
        private String tranAmt;
        private String bak;
        private String tranFee;

        public String getTranFee() {
            return tranFee;
        }

        public void setTranFee(String tranFee) {
            this.tranFee = tranFee;
        }


        public BodyBean(String fromAddr, String coinType, String toAddr, String tranAmt, String bak,String tranfee) {
            this.fromAddr = fromAddr;
            this.coinType = coinType;
            this.toAddr = toAddr;
            this.tranAmt = tranAmt;
            this.bak = bak;
            this.tranFee =tranfee;
        }

        public String getFromAddr() {
            return fromAddr;
        }

        public void setFromAddr(String fromAddr) {
            this.fromAddr = fromAddr;
        }

        public String getCoinType() {
            return coinType;
        }

        public void setCoinType(String coinType) {
            this.coinType = coinType;
        }

        public String getToAddr() {
            return toAddr;
        }

        public void setToAddr(String toAddr) {
            this.toAddr = toAddr;
        }

        public String getTranAmt() {
            return tranAmt;
        }

        public void setTranAmt(String tranAmt) {
            this.tranAmt = tranAmt;
        }

        public String getBak() {
            return bak;
        }

        public void setBak(String bak) {
            this.bak = bak;
        }
    }
}
