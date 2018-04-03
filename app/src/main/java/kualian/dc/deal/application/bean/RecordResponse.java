package kualian.dc.deal.application.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by idmin on 2018/3/6.
 */

public class RecordResponse extends ResponseCode{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
    private String walletid;
    private String coinAddr;
    private String coinType;
    private String num;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getWalletid() {
            return walletid;
        }

        public void setWalletid(String walletid) {
            this.walletid = walletid;
        }

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

        public List<TradeBean> getTrans() {
            return trans;
        }

        public void setTrans(List<TradeBean> trans) {
            this.trans = trans;
        }

        private List<TradeBean> trans;

    public static class TradeBean implements Serializable{
        private String targetAddr;
        private String tranType;
        private String tranState;
        private String tranAmt;
        private String createTime;
        private String bak;
        private String txId;
        private String  tranFee;

        public String getTxId() {
            return txId;
        }

        public void setTxId(String txId) {
            this.txId = txId;
        }

        public String getTranFee() {
            return tranFee;
        }

        public void setTranFee(String tranFee) {
            this.tranFee = tranFee;
        }

        public String getTargetAddr() {
            return targetAddr;
        }

        public void setTargetAddr(String targetAddr) {
            this.targetAddr = targetAddr;
        }

        public String getTranType() {
            return tranType;
        }

        public void setTranType(String tranType) {
            this.tranType = tranType;
        }

        public String getTranState() {
            return tranState;
        }

        public void setTranState(String tranState) {
            this.tranState = tranState;
        }

        public String getTranAmt() {
            return tranAmt;
        }

        public void setTranAmt(String tranAmt) {
            this.tranAmt = tranAmt;
        }

        public String getTranTime() {
            return createTime;
        }

        public void setTranTime(String tranTime) {
            this.createTime = tranTime;
        }

        public String getBak() {
            return bak;
        }

        public void setBak(String bak) {
            this.bak = bak;
        }


    }
    }
}
