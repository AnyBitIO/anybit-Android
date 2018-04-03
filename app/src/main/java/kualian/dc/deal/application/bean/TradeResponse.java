package kualian.dc.deal.application.bean;

import java.util.List;

/**
 * Created by idmin on 2018/3/7.
 */

public class TradeResponse  {


    /**
     * data : {"result":"00","unsignTranHex":"02000000015a754117ea6275aeaa76247cde9c577007a907abcb22190dd94ddc1b0c6305680000000000ffffffff02a0860100000000001976a914f26510518b5cec0fb36e28e12df58badb37f5fe188ac159bb803000000001976a91426608f302cb34286b8518a8d7cacacb00d15ce2e88ac00000000","inputs":[{"scriptPubKey":"76a91426608f302cb34286b8518a8d7cacacb00d15ce2e88ac","amout":0.62530973,"txid":"6805630c1bdc4dd90d1922cbab07a90770579cde7c2476aaae7562ea1741755a","vout":0}]}
     * errCode : 10000
     * errMsg :
     * rtnCode : 1
     */

    private DataBean data;
    private String errCode;
    private String errMsg;
    private String rtnCode;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public static class DataBean {
        /**
         * result : 00
         * unsignTranHex : 02000000015a754117ea6275aeaa76247cde9c577007a907abcb22190dd94ddc1b0c6305680000000000ffffffff02a0860100000000001976a914f26510518b5cec0fb36e28e12df58badb37f5fe188ac159bb803000000001976a91426608f302cb34286b8518a8d7cacacb00d15ce2e88ac00000000
         * inputs : [{"scriptPubKey":"76a91426608f302cb34286b8518a8d7cacacb00d15ce2e88ac","amout":0.62530973,"txid":"6805630c1bdc4dd90d1922cbab07a90770579cde7c2476aaae7562ea1741755a","vout":0}]
         */

        private String result;
        private String unsignTranHex;
        private List<InputsBean> inputs;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getUnsignTranHex() {
            return unsignTranHex;
        }

        public void setUnsignTranHex(String unsignTranHex) {
            this.unsignTranHex = unsignTranHex;
        }

        public List<InputsBean> getInputs() {
            return inputs;
        }

        public void setInputs(List<InputsBean> inputs) {
            this.inputs = inputs;
        }

        public static class InputsBean {
            /**
             * scriptPubKey : 76a91426608f302cb34286b8518a8d7cacacb00d15ce2e88ac
             * amout : 0.62530973
             * txid : 6805630c1bdc4dd90d1922cbab07a90770579cde7c2476aaae7562ea1741755a
             * vout : 0
             */

            private String scriptPubKey;
            private double amout;
            private String txid;
            private int vout;

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
    }
}
