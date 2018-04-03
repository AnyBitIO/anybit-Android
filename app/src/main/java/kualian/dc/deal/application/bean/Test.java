package kualian.dc.deal.application.bean;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by idmin on 2018/3/15.
 */

public class Test {
    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
    }
    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null,  new TrustManager[] { new TrustAllCerts() }, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }
    public static void post( Callback callback) throws IOException {
        try {
            OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
            mBuilder.sslSocketFactory(createSSLSocketFactory());
            mBuilder.hostnameVerifier(new TrustAllHostnameVerifier());
            OkHttpClient client = mBuilder.build();


            RequestBody body = RequestBody.create( MediaType.parse("application/json; charset=utf-8"), "{\"body\":{\"assets\":[\"UBTC\"]},\"header\":{\"clienttype\":\"Android\",\"handshake\":\"43586c5a0318ba476ebd173d5f004a0b\",\"imie\":\"imie\",\"language\":\"zh\",\"random\":\"600000\",\"trancode\":\"asset_query\",\"version\":\"1.0.1\",\"walletid\":\"3abc10b18abc27f3c5e792a38231e7c4e1dc59df\"}}");
            Request request = new Request.Builder()
                    .url("https://192.168.1.220/server/process/")
                    .post(body)
                    .build();
            client.newCall(request).enqueue(callback);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
