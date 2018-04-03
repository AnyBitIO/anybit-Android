package kualian.dc.deal.application.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by idmin on 2018/3/5.
 */

public class WalletRequest extends RequestBean {
    private BodyBean body;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean implements Serializable {

        public BodyBean(List<String> addrs) {
            this.assets = addrs;
        }

        private List<String> assets;

        public List<String> getAddrs() {
            return assets;
        }

        public void setAddrs(List<String> addrs) {
            this.assets = addrs;
        }

        public static class AddrsBean {


            public AddrsBean(String coinType) {
                this.coinType = coinType;
            }

            /**
             * coinAddr : xxx
             * coinType : yyy
             */

            private String coinType;


            public String getCoinType() {
                return coinType;
            }

            public void setCoinType(String coinType) {
                this.coinType = coinType;
            }
        }
    }
}
