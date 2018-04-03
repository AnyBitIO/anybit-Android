package kualian.dc.deal.application.wallet;


/**
 * Created by idmin on 2018/2/23.
 */

public enum CoinIndex {
    BITCOIN(1),
    ASIACOIN(10002),
    AURORACOIN(10003),
    BATA(10004),
    BLACKCOIN(10005),
    BURST(10006),
    CANADA(10007),
    CANNACOIN(10008),
    CLAMS(10009),
    CLUBCOIN(10010),
    DASH(10011),
    DIGIBYTE(10012),
    DIGITALCOIN(10013),
    DOGECOIN(10014),
    EFL(10015),
    GCR(10016),
    GULDEN(10017),
    IXCOIN(10018),
    JUMBUCKS(10019),
    LITECOIN(10020),
    MONACOIN(10021),
    NAMECOIN(10022),
    NEOSCOIN(10023),
    NOVACOIN(10024),
    NUBITS(10025),
    NUSHARES(10026),
    NXT(10027),
    OKCASH(10028),
    PARKBYTE(10029),
    peercoin(10030),
    POTCOIN(10031),
    PRIMECOIN(10032),
    REDDCOIN(10033),
    RICHCOIN(10034),
    RUBYCOIN(10035),
    FEATHERCOIN(10036),
    SHADOWCASH(10037),
    URO(10038),
    VERGE(10039),
    VERTCOIN(10040),
    VPNCOIN(10041),
    UBCOIN(2),
    TVCOIN(1)
    ;
    public final int index;

    CoinIndex(int index) {
        this.index = index;
    }
}
