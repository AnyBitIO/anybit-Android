package com.bitcoin.sign;

import com.bitcoin.sign.bitcoindrpc.Output;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mayakui on 2018/2/11 0011.
 */
public class Test {

    public static void main(String[] args){

        Output output1 = new Output();
        output1.setOutputIndex(1);
        output1.setTransactionId("190b281fbc430c78c304a9b6e5238f3369e04a581186f58283579f5c28bcf6b0");
        output1.setScriptPubKey("76a91426608f302cb34286b8518a8d7cacacb00d15ce2e88ac");

        Output[] outputs =  new Output[]{output1};

        BitcoinWallet wallet = new BitcoinWallet();
        String rawTrx = "0200000001b0f6bc285c9f578382f58611584ae069338f23e5b6a904c3780c43bc1f280b190100000000ffffffff02a0860100000000001976a9145cb55bea2b4e462eeb5000cd0b4cf247613150a488ac553e3201000000001976a91426608f302cb34286b8518a8d7cacacb00d15ce2e88ac00000000";
        String address = "mj1seQMAUNuq8A5MaVqfGY3YuKnF7g4nh3";
        String privateKey = "cPDf25wUdfdittRNxvTsJUyrT8hH3F7L4Q3DXEqLPciWUvbV34Z8";

        List<String> privateKeyList = new ArrayList<>();
        privateKeyList.add(privateKey);

        Map<String,String> addressKey = new HashMap<>();
        addressKey.put(address,privateKey);
        String sign = wallet.signTransaction(rawTrx, addressKey, outputs,"TEST");
        System.out.println("签名结果：");
        System.out.println(sign);
    }
}
