package kualian.dc.deal.application.bean;

import java.util.List;

/**
 * Created by idmin on 2018/3/6.
 */

public class ContactResult extends ResponseCode{

    /**
     * data : {"contacters":[]}
     * errCode : 10000
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
        private List<Contacter> contacters;

        public List<Contacter> getContacters() {
            return contacters;
        }

        public void setContacters(List<Contacter> contacters) {
            this.contacters = contacters;
        }
        public static class  Contacter{
            private String coinType;
            private String contacterAddr;
            private String nickName;

            public String getCoinType() {
                return coinType;
            }

            public void setCoinType(String coinType) {
                this.coinType = coinType;
            }

            public String getContacterAddr() {
                return contacterAddr;
            }

            public void setContacterAddr(String contacterAddr) {
                this.contacterAddr = contacterAddr;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }
        }
    }
}
