package kualian.dc.deal.application.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by idmin on 2018/3/5.
 */

public class HomeBean extends RequestBean{
    private BodyBean body;
    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean implements Serializable {
        public BodyBean(){

        }
        public BodyBean(List<AddrsBean> addrs) {
            this.addrs = addrs;
        }

        private List<AddrsBean> addrs;

        public List<AddrsBean> getAddrs() {
            return addrs;
        }

        public void setAddrs(List<AddrsBean> addrs) {
            this.addrs = addrs;
        }

        public static class AddrsBean {
            public AddrsBean(String coinAddr, String coinType) {
                this.coinAddr = coinAddr;
                this.coinType = coinType;
            }

            /**
             * coinAddr : xxx
             * coinType : yyy
             */


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
        }
    }
}
