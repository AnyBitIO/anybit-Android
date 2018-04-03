package kualian.dc.deal.application.bean;

import java.util.List;

/**
 * Created by idmin on 2018/3/5.
 */

public class SendRequest extends RequestBean {
    private BodyBean body;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        private List<Input> inputs;

        public List<Input> getInputs() {
            return inputs;
        }

        public void setInputs(List<Input> inputs) {
            this.inputs = inputs;
        }

        public String getSignTranHex() {
            return signTranHex;
        }

        public void setSignTranHex(String signTranHex) {
            this.signTranHex = signTranHex;
        }

        public BodyBean(String signTranHex) {

            this.signTranHex = signTranHex;
        }

        private String signTranHex;
        private TradeBean tranContent;

        public TradeBean getTranContent() {
            return tranContent;
        }

        public void setTranContent(TradeBean tranContent) {
            this.tranContent = tranContent;
        }

        public static class Input {
            private String scriptPubKey;
            private double amout;
            private String txid;
            private int vout;

            public Input(String scriptPubKey, double amout, String txid, int vout) {
                this.scriptPubKey = scriptPubKey;
                this.amout = amout;
                this.txid = txid;
                this.vout = vout;
            }

            public String getScriptPubKey() {
                return scriptPubKey;
            }

            public void setScriptPubKey(String scriptPubKey) {
                this.scriptPubKey = scriptPubKey;
            }

            public double getAmout() {
                return amout;
            }

            public void setAmout(double amout) {
                this.amout = amout;
            }

            public String getTxid() {
                return txid;
            }

            public void setTxid(String txid) {
                this.txid = txid;
            }

            public int getVout() {
                return vout;
            }

            public void setVout(int vout) {
                this.vout = vout;
            }
        }

        public static class TradeBean {
            private String fromAddr;
            private String coinType;
            private String toAddr;
            private String tranAmt;
            private String bak;


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

            public String getTranFee() {
                return tranFee;
            }

            public void setTranFee(String tranFee) {
                this.tranFee = tranFee;
            }

            private String tranFee;

            public TradeBean(String fromAddr, String coinType, String toAddr, String tranAmt, String bak, String tranfee) {
                this.fromAddr = fromAddr;
                this.coinType = coinType;
                this.toAddr = toAddr;
                this.tranAmt = tranAmt;
                this.bak = bak;
                this.tranFee = tranfee;
            }

        }
    }
}
