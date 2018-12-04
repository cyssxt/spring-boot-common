package com.cyssxt.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {


  public static ResponseHandler<String> get(){
    return response1 -> {
      int status = response1.getStatusLine().getStatusCode();
      if (status >= 200 && status < 300) {
        HttpEntity entity = response1.getEntity();
        return entity != null ? EntityUtils.toString(entity) : null;
      } else {
        throw new ClientProtocolException("Unexpected response status: " + status);
      }
    };
  }

  public static String post(String url,Map<String,Object> map) throws IOException {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost(url);
    List<NameValuePair> nvps = new ArrayList<>();
    nvps.add(new BasicNameValuePair("username", "vip"));
    nvps.add(new BasicNameValuePair("password", "secret"));
    httpPost.setEntity(new UrlEncodedFormEntity(nvps));
    String response = httpclient.execute(httpPost, get());
    return response;
  }

  public static String get(String url,Map<String,Object> map) throws IOException {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(url);
    String response = httpclient.execute(httpGet,get());
    return response;
  }
}
