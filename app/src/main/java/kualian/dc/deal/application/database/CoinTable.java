package kualian.dc.deal.application.database;

/**
 * Created by Meiji on 2017/6/17.
 */

public class CoinTable {

    /**
     * 选择货币表
     */
    public static final String TABLENAME = "CoinTable";

    /**
     * 字段部分
     */
    public static final String ID = "id";
    public static final String COIN_ADD = "coin_select";
    public static final String COINTYPE = "cointype";
    public static final String COIN_COININDEX = "id_coinindex";
    public static final String COIN_ADDRESS = "coin_address";
    public static final String COIN_RESOURCE = "coin_resource";
    public static final String COIN_NUM = "coin_num";
    public static final String COIN_US = "coin_us";
    public static final String WALLET_ID = "wallet_id";
    public static final String COIN_CNY = "coin_cny";

    /**
     * 字段ID 数据库操作建立字段对应关系 从0开始
     */
    public static final int ID_ID = 0;
    public static final int ID_COINTYPE = 1;
    public static final int ID_COIN_COININDEX = 2;
    public static final int ID_COIN_ADDRESS = 3;
    public static final int ID_COIN_RESOURCE = 4;
    public static final int ID_COIN_NUM = 5;
    public static final int ID_COIN_US = 6;
    public static final int ID_WALLET_ID = 7;
    public static final int ID_COIN_CNY = 8;
    public static final int ID_COIN_SELECT = 9;

   /* public static final int ID_ID = 0;
    public static final int ID_COINTYPE = 1;
    public static final int ID_COIN_ADDRESS = 2;
    public static final int ID_COIN_RESOURCE = 3;
    public static final int ID_COIN_NUM = 4;
    public static final int ID_COIN_US = 5;
    public static final int ID_COIN_CNY = 6;
    public static final int ID_WALLET_ID = 7;
    public static final int ID_COIN_COININDEX = 8;*/

    /**
     * 创建表
     */
    public static final String CREATE_TABLE = "create table if not exists " + TABLENAME + "(" +
            ID + " text auto_increment, " +
            COINTYPE + " text, " +
            COIN_COININDEX + " text, " +
            COIN_ADDRESS + " text, " +
            COIN_RESOURCE + " text, " +
            COIN_NUM + " text, " +
            COIN_US + " text, " +
            WALLET_ID + " text, " +
            COIN_CNY + " text, " +
            COIN_ADD + " text) ";
}
