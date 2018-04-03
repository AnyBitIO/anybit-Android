package kualian.dc.deal.application.database;


/**
 * Created by idmin on 2018/3/20.
 */

public class WalletTable {

    public static final String TABLENAME =  "WalletTable";

    public static final String ID = "id";
    public static final String WALLET_NAME = "wallet_name";
    public static final String WALLET_HEAD = "wallet_head";
    public static final String WALLET_ID= "wallet_id";
    public static final String WALLET_SEND= "wallet_send";
    public static final String WALLET_LOGIN_PW= "wallet_login_pw";
    public static final String WALLET_TRADE_PW= "wallet_trade_pw";
    public static final String WALLET_DEFAULT_INDEX= "wallet_default_index";


    public static final int ID_ID = 0;
    public static final int ID_WALLET_NAME = 1;
    public static final int ID_WALLET_ID = 2;
    public static final int ID_WALLET_LOGIN = 3;
    public static final int ID_WALLET_TRADE = 4;
    public static final int ID_WALLET_INDEX = 5;
    public static final int ID_WALLET_HEAD = 6;
    public static final int ID_WALLET_SEND = 7;

    /**
     * 创建表
     */
    public static final String CREATE_TABLE = "create table if not exists " + TABLENAME + "(" +
            ID + " text auto_increment, " +
            WALLET_NAME + " text, " +
            WALLET_ID + " text, " +
            WALLET_LOGIN_PW + " text, " +
            WALLET_TRADE_PW + " text, " +
            WALLET_DEFAULT_INDEX + " text, " +
            WALLET_HEAD + " text, " +
            WALLET_SEND + " text) ";
}
