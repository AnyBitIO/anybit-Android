package kualian.dc.deal.application.database;

import kualian.dc.deal.application.util.SpUtil;

/**
 * Created by Meiji on 2017/6/17.
 */

public class ContactTable {

    /**
     * 浏览记录表
     */
    public static final String TABLENAME = "ContactTable";

    /**
     * 字段部分
     */
    public static final String ID = "id";
    public static final String COINTYPE = "cointype";
    public static final String NICKNAME = "nickName";
    public static final String WALLET_ID = "wallet_id";
    public static final String CONTACTADDR = "contactAddr";

    /**
     * 字段ID 数据库操作建立字段对应关系 从0开始
     */
    public static final int ID_ID = 0;
    public static final int ID_COINTYPE= 1;
    public static final int ID_NICKNAME = 2;
    public static final int ID_WALLET_ID = 3;
    public static final int ID_CONTACTADDR = 4;

    /**
     * 创建表
     */
    public static final String CREATE_TABLE = "create table if not exists " + TABLENAME + "(" +
            ID + " text auto_increment, " +
            COINTYPE + " text, " +
            NICKNAME + " text, " +
            WALLET_ID + " text, " +
            CONTACTADDR + " text) ";
}
