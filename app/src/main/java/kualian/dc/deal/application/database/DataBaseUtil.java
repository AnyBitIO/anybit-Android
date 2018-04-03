package kualian.dc.deal.application.database;

/**
 * Created by idmin on 2018/3/26.
 */

public class DataBaseUtil {
    public static void deleteDataWithId(String walletID){
        ContactDao contactDao =new ContactDao();
        contactDao.deleteWithId(walletID);
        WalletDao walletDao=new WalletDao();
        walletDao.deleteWithId(walletID);
        CoinDao coinDao=new CoinDao();
        coinDao.deleteWithId(walletID);
        AssetDao assetDao=new AssetDao();
        assetDao.deleteWithId(walletID);
    }
}
