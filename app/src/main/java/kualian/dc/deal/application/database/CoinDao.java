package kualian.dc.deal.application.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.wallet.CoinType;

/**
 * Created by idmin on 2018/3/8.
 */

public class CoinDao {

    private SQLiteDatabase db;

    public CoinDao() {
        this.db = DatabaseHelper.getDatabase();
    }
/*
    public boolean add(int resource, String address, String coinType, int index, String walletId, String isAdd) {

        ContentValues values = new ContentValues();
        values.put(CoinTable.COINTYPE, coinType);
        values.put(CoinTable.COIN_ADDRESS, address);
        values.put(CoinTable.COIN_RESOURCE, resource);
        values.put(CoinTable.COIN_COININDEX, index);
        values.put(CoinTable.WALLET_ID, walletId);
        values.put(CoinTable.COIN_ADD, isAdd);
        long result = db.insert(CoinTable.TABLENAME, null, values);
        return result != -1;
    }*/
    public boolean add(CoinType coinType) {

        ContentValues values = new ContentValues();
        values.put(CoinTable.COINTYPE, coinType.getCoinName());
        values.put(CoinTable.COIN_ADDRESS, coinType.getCoinAddress());
        values.put(CoinTable.COIN_RESOURCE, coinType.getCoinResource());
        values.put(CoinTable.COIN_COININDEX, coinType.getCoinIndex());
        values.put(CoinTable.WALLET_ID, SpUtil.getInstance().getWalletID());
        values.put(CoinTable.COIN_ADD, Constants.IS_NO_ADD);
        long result = db.insert(CoinTable.TABLENAME, null, values);
        return result != -1;
    }
    public List<CoinType> queryAll(String walletId) {
        Cursor cursor = db.query(CoinTable.TABLENAME, null, CoinTable.WALLET_ID+ "=?", new String[]{walletId}, null, null, CoinTable.COINTYPE + " desc");
       // Cursor cursor = db.query(CoinTable.TABLENAME, null, null, null, null, null, CoinTable.COINTYPE + " desc");
        List<CoinType> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            CoinType bean = new CoinType();
            bean.setCoinName(cursor.getString(CoinTable.ID_COINTYPE));
            bean.setCoinAddress(cursor.getString(CoinTable.ID_COIN_ADDRESS));
            bean.setCoinResource(cursor.getInt(CoinTable.ID_COIN_RESOURCE));
            bean.setCoinIndex(cursor.getInt(CoinTable.ID_COIN_COININDEX));
            bean.setWalletId(cursor.getString(CoinTable.ID_WALLET_ID));
            bean.setAddTag(cursor.getString(CoinTable.ID_COIN_SELECT));
            list.add(bean);
        }
        cursor.close();
        return list;
    }
    public List<CoinType> querySelectAll(String isAdd) {
        Cursor cursor = db.query(CoinTable.TABLENAME, null, CoinTable.COIN_ADD+ "=?"+" AND "+CoinTable.WALLET_ID+ "=?", new String[]{isAdd, SpUtil.getInstance().getWalletID()}, null, null, CoinTable.COINTYPE + " desc");
        List<CoinType> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            CoinType bean = new CoinType();
            bean.setCoinName(cursor.getString(CoinTable.ID_COINTYPE));
            bean.setCoinAddress(cursor.getString(CoinTable.ID_COIN_ADDRESS));
            bean.setCoinResource(cursor.getInt(CoinTable.ID_COIN_RESOURCE));
            bean.setCoinIndex(cursor.getInt(CoinTable.ID_COIN_COININDEX));
            bean.setWalletId(cursor.getString(CoinTable.ID_WALLET_ID));
            bean.setAddTag(cursor.getString(CoinTable.ID_COIN_SELECT));
            list.add(bean);
        }
        cursor.close();
        return list;
    }
    public boolean queryisExist(String keyWord) {
        Cursor cursor = db.query(CoinTable.TABLENAME, null, CoinTable.COINTYPE + "=?", new String[]{keyWord}, null, null, null);
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean delete(String keyWord) {
        int id = db.delete(CoinTable.TABLENAME, CoinTable.COINTYPE + "=?", new String[]{keyWord});
        return id != -1;
    }

    public boolean deleteWithId(String walletId) {
        int id = db.delete(CoinTable.TABLENAME, CoinTable.WALLET_ID + "=?", new String[]{walletId});
        return id != -1;
    }

    public boolean update(String Key, String isAdd, String address) {
        ContentValues values = new ContentValues();
        values.put(CoinTable.COIN_ADD, isAdd);
        values.put(CoinTable.COIN_ADDRESS, address);
        int result = db.update(CoinTable.TABLENAME, values, CoinTable.COINTYPE + "=?", new String[]{Key});
        return result != -1;
    }

    public boolean deleteAll() {
        int id = db.delete(CoinTable.TABLENAME, null, null);
        return id != -1;
    }
}
