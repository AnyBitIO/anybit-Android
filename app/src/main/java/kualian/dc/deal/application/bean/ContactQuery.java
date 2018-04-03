package kualian.dc.deal.application.bean;

/**
 * Created by idmin on 2018/3/5.
 */

public class ContactQuery extends RequestBean {
    private BodyBean body;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        private String contacterAddr;
        private String coinType;

        public String getContactAddr() {
            return contacterAddr;
        }

        public void setContactAddr(String contactAddr) {
            this.contacterAddr = contactAddr;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public BodyBean(String contacterAddr, String nickname, String coinType) {

            this.contacterAddr = contacterAddr;
            this.nickName = nickname;
            this.coinType = coinType;
        }

        private String nickName;
    }
}
