package kualian.dc.deal.application.bean;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by idmin on 2018/2/28.
 */

public class ContactBean implements MultiItemEntity ,Serializable,Comparable<ContactBean>{

    private String coinType;
    private String contactAddr;
    private String nickName;
    private String pinyin;
    private String walletId;

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getContactAddr() {
        return contactAddr;
    }

    public void setContactAddr(String contactAddr) {
        this.contactAddr = contactAddr;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setFirstChar(char firstChar) {
        this.firstChar = firstChar;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    private char firstChar;
    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
        String first = pinyin.substring(0, 1);
        if (first.matches("[A-Za-z]")) {
            firstChar = first.toUpperCase().charAt(0);
        } else {
            firstChar = '#';
        }
    }
    public char getFirstChar() {
        return firstChar;
    }




    private int itemType;
    public static final int TITLE = 0;
    public static final int CONTENT = 1;

    public ContactBean() {

    }
    @Override
    public int getItemType() {
        return 1;
    }

    @Override
    public int compareTo(@NonNull ContactBean contact) {
        return this.pinyin.compareTo(contact.getPinyin());

    }
}
