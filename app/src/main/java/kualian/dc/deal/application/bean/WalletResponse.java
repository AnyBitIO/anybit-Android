package kualian.dc.deal.application.bean;

import java.util.List;

/**
 * Created by idmin on 2018/3/9.
 */

public class WalletResponse extends ResponseCode{

    /**
     * data : {"walletid":"de7de36f36ec9cf43bc28ec473615c40","assets":[{"coinType":"UBTC","num":0.50305,"usdtAmt":0,"cnyAmt":0},{"coinType":"UBTC","num":0,"usdtAmt":0,"cnyAmt":0},{"coinType":"UBTC","num":0.615,"usdtAmt":0,"cnyAmt":0}],"totalUsdtAmt":0,"totalCnyAmt":0}
     * errCode : 10000
     * errMsg :
     * rtnCode : 1
     */

    private DataBean data;


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * walletid : de7de36f36ec9cf43bc28ec473615c40
         * assets : [{"coinType":"UBTC","num":0.50305,"usdtAmt":0,"cnyAmt":0},{"coinType":"UBTC","num":0,"usdtAmt":0,"cnyAmt":0},{"coinType":"UBTC","num":0.615,"usdtAmt":0,"cnyAmt":0}]
         * totalUsdtAmt : 0.0
         * totalCnyAmt : 0.0
         */

        private String walletid;
        private String totalUsdtAmt;
        private String totalCnyAmt;
        private List<AssetsBean> assets;

        public String getWalletid() {
            return walletid;
        }

        public void setWalletid(String walletid) {
            this.walletid = walletid;
        }

        public String getTotalUsdtAmt() {
            return totalUsdtAmt;
        }

        public void setTotalUsdtAmt(String totalUsdtAmt) {
            this.totalUsdtAmt = totalUsdtAmt;
        }

        public String getTotalCnyAmt() {
            return totalCnyAmt;
        }

        public void setTotalCnyAmt(String totalCnyAmt) {
            this.totalCnyAmt = totalCnyAmt;
        }

        public List<AssetsBean> getAssets() {
            return assets;
        }

        public void setAssets(List<AssetsBean> assets) {
            this.assets = assets;
        }

        public static class AssetsBean {
            /**
             * coinType : UBTC
             * num : 0.50305
             * usdtAmt : 0.0
             * cnyAmt : 0.0
             */

            private String coinType;
            private String num;
            private String usdtAmt;
            private String cnyAmt;
            private String maxFee;
            private String recommendFee;
            private String minFee;

            public String getMaxFee() {
                return maxFee;
            }

            public void setMaxFee(String maxFee) {
                this.maxFee = maxFee;
            }

            public String getRecommendFee() {
                return recommendFee;
            }

            public void setRecommendFee(String recommendFee) {
                this.recommendFee = recommendFee;
            }

            public String getMinFee() {
                return minFee;
            }

            public void setMinFee(String minFee) {
                this.minFee = minFee;
            }


            public String getCoinType() {
                return coinType;
            }

            public void setCoinType(String coinType) {
                this.coinType = coinType;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getUsdtAmt() {
                return usdtAmt;
            }

            public void setUsdtAmt(String usdtAmt) {
                this.usdtAmt = usdtAmt;
            }

            public String getCnyAmt() {
                return cnyAmt;
            }

            public void setCnyAmt(String cnyAmt) {
                this.cnyAmt = cnyAmt;
            }
        }
    }
}
