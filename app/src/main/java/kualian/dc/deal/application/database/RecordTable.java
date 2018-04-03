package kualian.dc.deal.application.database;

import kualian.dc.deal.application.util.SpUtil;

/**
 * Created by idmin on 2018/3/22.
 */

public class RecordTable {
    public static final String TABLENAME = "RecordTable";

    public static final String ID = "id";
    public static final String JSON = "json";
    public static final String PAGE = "page";
    public static final String WALLET_ID = "wallet_id";


    public static final int ID_ID = 0;
    public static final int ID_JSON = 1;
    public static final int ID_PAGE = 2;
    public static final int ID_WALLET_ID = 3;

    /**
     * 创建表
     */
    public static final String CREATE_TABLE = "create table if not exists " + TABLENAME + "(" +
            ID + " text auto_increment, " +
            JSON + " text, " +
            PAGE + " text, " +
            WALLET_ID + " text) ";
}
