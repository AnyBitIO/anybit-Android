package kualian.dc.deal.application.bean;

/**
 * Created by idmin on 2018/3/5.
 */

public class ContactHandle extends RequestBean {
    private BodyBean body;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        private String contacterAddr;
        private String origContacterAddr;

        public String getOrigContacterAddr() {
            return origContacterAddr;
        }

        public void setOrigContacterAddr(String origContacterAddr) {
            this.origContacterAddr = origContacterAddr;
        }

        private String coinType;

        public String getContactAddr() {
            return contacterAddr;
        }

        public void setContactAddr(String contactAddr) {
            this.contacterAddr = contactAddr;
        }

        public String getCoinType() {
            return coinType;
        }

        public void setCoinType(String coinType) {
            this.coinType = coinType;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public BodyBean(String contactAddr, String coinType, String nickname) {

            this.contacterAddr = contactAddr;
            this.coinType = coinType;
            this.nickName = nickname;
        }

        private String nickName;
    }
}
