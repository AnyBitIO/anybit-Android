package kualian.dc.deal.application.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by idmin on 2018/3/8.
 */

public class AssetDao {
    private SQLiteDatabase db;

    public AssetDao() {
        this.db = DatabaseHelper.getDatabase();
    }

    public boolean add(String content,String walletId) {
        ContentValues values = new ContentValues();
        values.put(AssetTable.JSON, content);
        values.put(AssetTable.WALLET_ID, walletId);
        long result = db.insert(AssetTable.TABLENAME, null, values);
        return result != -1;
    }

    public String queryAsset(String walletId) {
        Cursor cursor = db.query(AssetTable.TABLENAME, null, AssetTable.WALLET_ID+ "=?", new String[]{walletId}, null, null, AssetTable.ID + " desc");
        String content = null;
        while (cursor.moveToNext()) {
            content = cursor.getString(AssetTable.ID_JSON);
        }
        cursor.close();
        return content;
    }
    public boolean deleteWithId(String walletId) {
        int id = db.delete(AssetTable.TABLENAME, AssetTable.WALLET_ID + "=?", new String[]{walletId});
        return id != -1;
    }
    public boolean deleteAll() {
        int id = db.delete(AssetTable.TABLENAME, null, null);
        return id != -1;
    }
}
