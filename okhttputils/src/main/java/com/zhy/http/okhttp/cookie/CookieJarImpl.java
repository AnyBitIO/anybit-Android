package com.zhy.http.okhttp.cookie;

import com.zhy.http.okhttp.cookie.store.CookieStore;
import com.zhy.http.okhttp.utils.Exceptions;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by zhy on 16/3/10.
 */
public class CookieJarImpl implements CookieJar
{
    private CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore)
    {
        if (cookieStore == null) Exceptions.illegalArgument("cookieStore can not be null.");
        this.cookieStore = cookieStore;
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies)
    {
        cookieStore.add(url, cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url)
    {
        return cookieStore.get(url);
    }

    public CookieStore getCookieStore()
    {
        return cookieStore;
    }
}
