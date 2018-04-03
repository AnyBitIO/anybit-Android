package kualian.dc.deal.application.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import kualian.dc.deal.application.wallet.CoinType;
import kualian.dc.deal.application.wallet.WalletAccount;

/**
 * Created by idmin on 2018/3/8.
 */

public class WalletDao {
    private SQLiteDatabase db;

    public WalletDao() {
        this.db = DatabaseHelper.getDatabase();
    }

    public boolean add(String walletId,String walletName,String loginPw,String tradePw,int index,String send) {
        ContentValues values = new ContentValues();
        values.put(WalletTable.WALLET_ID, walletId);
        values.put(WalletTable.WALLET_NAME, walletName);
        values.put(WalletTable.WALLET_LOGIN_PW, loginPw);
        values.put(WalletTable.WALLET_TRADE_PW, tradePw);
        values.put(WalletTable.WALLET_DEFAULT_INDEX, index);
        values.put(WalletTable.WALLET_HEAD, "");
        values.put(WalletTable.WALLET_SEND, send);
        long result = db.insert(WalletTable.TABLENAME, null, values);
        return result != -1;
    }
    public List<WalletAccount> queryAll() {
        Cursor cursor = db.query(WalletTable.TABLENAME, null, null, null, null, null, WalletTable.WALLET_ID + " desc");
        List<WalletAccount> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            WalletAccount bean = new WalletAccount();
            bean.setWalletDefaultIndex(cursor.getInt(WalletTable.ID_WALLET_INDEX));
            bean.setWalletId(cursor.getString(WalletTable.ID_WALLET_ID));
            bean.setWalletLoginPw(cursor.getString(WalletTable.ID_WALLET_LOGIN));
            bean.setWalletTradePw(cursor.getString(WalletTable.ID_WALLET_TRADE));
            bean.setWalletName(cursor.getString(WalletTable.ID_WALLET_NAME));
            bean.setWalletSend(cursor.getString(WalletTable.ID_WALLET_SEND));
            list.add(bean);
        }
        cursor.close();
        return list;
    }
 /*   public String queryAsset() {
        Cursor cursor = db.query(WalletTable.TABLENAME, null, null, null, null, null, WalletTable.ID + " desc");
        String content = null;
        while (cursor.moveToNext()) {
            content = cursor.getString(WalletTable.ID_JSON);
        }
        cursor.close();
        return content;
    }*/
 public boolean deleteWithId(String walletId) {
     int id = db.delete(WalletTable.TABLENAME, ContactTable.WALLET_ID + "=?", new String[]{walletId});
     return id != -1;
 }
 public boolean update(String key,String content,String tag) {
     ContentValues values = new ContentValues();
     values.put(key, content);
     int result = db.update(WalletTable.TABLENAME, values, WalletTable.WALLET_ID + "=?", new String[]{tag});
     return result != -1;
 }
    public boolean deleteAll() {
        int id = db.delete(WalletTable.TABLENAME, null, null);
        return id != -1;
    }
}
