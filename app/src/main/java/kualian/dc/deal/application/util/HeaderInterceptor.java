/*
 * Copyright (C) 2017 zhouyou(478319399@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kualian.dc.deal.application.util;


import android.util.Log;

import com.zhouyou.http.model.HttpHeaders;
import com.zhouyou.http.utils.HttpLog;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>描述：配置公共头部</p>
 * 作者： zhouyou<br>
 * 日期： 2016/12/19 16:46<br>
 * 版本： v2.0<br>
 */
public class HeaderInterceptor implements Interceptor {

    private HttpHeaders headers;

    public HeaderInterceptor() {
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        builder.header("User-Agent", "{\"clienttype\":\"Android\",\"handshake\":\"lightwallet480333.7438873632locklinker.com\",\"imie\":\"123\",\"language\":\"zh\",\"random\":\"480333.7438873632\",\"trancode\":\"httpPost\",\"version\":\"10001\",\"walletid\":\"9316a3cab98c6e16135c44f610ec2e66\"}").build();

        //if (headers.headersMap.isEmpty()) return chain.proceed(builder.build());
      /*  try {
            for (Map.Entry<String, String> entry : headers.headersMap.entrySet()) {
                //去除重复的header
                //builder.removeHeader(entry.getKey());
                //builder.addHeader(entry.getKey(), entry.getValue()).build();
                Log.i("TAG", "intercept: "+entry.getValue());
            }
        } catch (Exception e) {
            HttpLog.e(e);
        }*/
        return chain.proceed(builder.build());

    }
}
