package kualian.dc.deal.application.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by idmin on 2018/3/22.
 */

public class RecordDao {
    private SQLiteDatabase db;

    public RecordDao() {
        this.db = DatabaseHelper.getDatabase();
    }

    public boolean add(String content,String walletId,String page) {
        ContentValues values = new ContentValues();
        values.put(RecordTable.PAGE, page);
        values.put(RecordTable.JSON, content);
        values.put(RecordTable.WALLET_ID, walletId);
        long result = db.insert(RecordTable.TABLENAME, null, values);
        return result != -1;
    }

    public String queryRecord(String walletId) {
        Cursor cursor = db.query(RecordTable.TABLENAME, null, RecordTable.WALLET_ID+ "=?", new String[]{walletId}, null, null, RecordTable.ID + " desc");
        String content = null;
        while (cursor.moveToNext()) {
            content = cursor.getString(RecordTable.ID_JSON);
        }
        cursor.close();
        return content;
    }
    public boolean deleteWithId(String walletId) {
        int id = db.delete(RecordTable.TABLENAME, RecordTable.WALLET_ID + "=?", new String[]{walletId});
        return id != -1;
    }
    public boolean deleteAll() {
        int id = db.delete(RecordTable.TABLENAME, null, null);
        return id != -1;
    }
}
