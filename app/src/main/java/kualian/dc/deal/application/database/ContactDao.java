package kualian.dc.deal.application.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import kualian.dc.deal.application.bean.ContactBean;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.HanziToPinyin;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.SpUtil;

/**
 * Created by idmin on 2018/3/8.
 */

public class ContactDao  {

    private SQLiteDatabase db;

    public ContactDao() {
        this.db = DatabaseHelper.getDatabase();
    }

    public boolean add(String nick,String address,String coinType,String walletId) {
        ContentValues values = new ContentValues();
        values.put(ContactTable.COINTYPE, coinType);
        values.put(ContactTable.CONTACTADDR, address);
        values.put(ContactTable.NICKNAME, nick);
        values.put(ContactTable.WALLET_ID, walletId);
        long result = db.insert(ContactTable.TABLENAME, null, values);
        return result != -1;
    }

    public List<ContactBean> queryAll() {
        Cursor cursor = db.query(ContactTable.TABLENAME, null, ContactTable.WALLET_ID+ "=?", new String[]{SpUtil.getInstance().getWalletID()}, null, null, ContactTable.COINTYPE + " desc");
        //Cursor cursor = db.query(ContactTable.TABLENAME, null, null, null, null, null, ContactTable.CONTACTADDR + " desc");
        List<ContactBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            ContactBean bean = new ContactBean();
            bean.setCoinType(cursor.getString(ContactTable.ID_COINTYPE));
            bean.setContactAddr(cursor.getString(ContactTable.ID_CONTACTADDR));
            String nick = cursor.getString(ContactTable.ID_NICKNAME);
            bean.setNickName(nick);
            bean.setWalletId(cursor.getString(ContactTable.ID_WALLET_ID));
            bean.setPinyin(CommonUtil.toHanyuPinyin(nick));
            list.add(bean);
        }
        cursor.close();
        return list;
    }

    public boolean queryisExist(String keyWord) {
        Cursor cursor = db.query(ContactTable.TABLENAME, null, ContactTable.COINTYPE + "=?", new String[]{keyWord}, null, null, null);
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean delete(String keyWord) {
        int id = db.delete(ContactTable.TABLENAME, ContactTable.CONTACTADDR + "=?", new String[]{keyWord});
        return id != -1;
    }
    public boolean deleteWithId(String walletId) {
        int id = db.delete(ContactTable.TABLENAME, ContactTable.WALLET_ID + "=?", new String[]{walletId});
        return id != -1;
    }
    public boolean update(String nick,String address,String coinType,String Key) {
        ContentValues values = new ContentValues();
        values.put(ContactTable.COINTYPE, coinType);
        values.put(ContactTable.CONTACTADDR, address);
        values.put(ContactTable.NICKNAME, nick);
        int result = db.update(ContactTable.TABLENAME, values, ContactTable.CONTACTADDR + "=?"+" AND "+ContactTable.WALLET_ID+"=?", new String[]{Key, SpUtil.getInstance().getWalletID()});
        return result != -1;
    }

    public boolean deleteAll() {
        int id = db.delete(ContactTable.TABLENAME, null, null);
        return id != -1;
    }
}
