package kualian.dc.deal.application.wallet;

import org.bitcoinj.core.BitcoinSerializer;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.StoredBlock;
import org.bitcoinj.core.VerificationException;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.utils.MonetaryFormat;

import java.io.Serializable;

/**
 * Created by idmin on 2018/2/23.
 */

public class CoinType extends NetworkParameters implements Serializable {
    public String coinName;
    public String coinNum;
    public String usMoney;
    public String walletId;
    private String maxFee;
    private String recommendFee;
    private String minFee;

    public String getAddTag() {
        return addTag;
    }

    public void setAddTag(String addTag) {
        this.addTag = addTag;
    }

    private String addTag;

    public String getMaxFee() {
        return maxFee!=null?maxFee:"0.01";
    }

    public void setMaxFee(String maxFee) {
        this.maxFee = maxFee;
    }

    public String getRecommendFee() {
        return recommendFee!=null?recommendFee:"0.0001";
    }

    public void setRecommendFee(String recommendFee) {
        this.recommendFee = recommendFee;
    }

    public String getMinFee() {
        return minFee!=null?minFee:"0.0001";
    }

    public void setMinFee(String minFee) {
        this.minFee = minFee;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getUsMoney() {
        return usMoney!=null?usMoney:"0";
    }

    public void setUsMoney(String usMoney) {
        this.usMoney = usMoney;
    }

    public String getCoinNum() {
        return coinNum!=null?coinNum:"0";
    }

    public void setCoinNum(String coinNum) {
        this.coinNum = coinNum;
    }

    public String coinAddress;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String coinMoney;
    public int coinIndex;
    public boolean isSelect;
    public String getCoinAddress() {
        return coinAddress;
    }

    public void setCoinAddress(String coinAddress) {
        this.coinAddress = coinAddress;
    }

    public int coinResource;

    public String getCoinMoney() {
        return coinMoney!=null?coinMoney:"0";
    }

    public void setCoinMoney(String coinMoney) {
        this.coinMoney = coinMoney;
    }

    public CoinType() {
    }
    public CoinType(String coinName) {
        this.coinName = coinName;
    }

    public int getCoinResource() {
        return coinResource;
    }

    public void setCoinResource(int coinResource) {
        this.coinResource = coinResource;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public int getCoinIndex() {
        return coinIndex;
    }

    public void setCoinIndex(int coinIndex) {
        this.coinIndex = coinIndex;
    }

    @Override
    public String getPaymentProtocolId() {
        return null;
    }

    @Override
    public void checkDifficultyTransitions(StoredBlock storedBlock, Block block, BlockStore blockStore) throws VerificationException, BlockStoreException {

    }

    @Override
    public Coin getMaxMoney() {
        return null;
    }

    @Override
    public Coin getMinNonDustOutput() {
        return null;
    }

    @Override
    public MonetaryFormat getMonetaryFormat() {
        return null;
    }

    @Override
    public String getUriScheme() {
        return null;
    }

    @Override
    public boolean hasMaxMoney() {
        return false;
    }

    @Override
    public BitcoinSerializer getSerializer(boolean b) {
        return null;
    }

    @Override
    public int getProtocolVersionNum(ProtocolVersion protocolVersion) {
        return 0;
    }
}
