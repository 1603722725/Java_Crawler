package com.jd.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @Author: wl
 * @Date: 2019/10/8 20:37
 */
@Component
public class HttpUtils {
    private PoolingHttpClientConnectionManager cm;
    public HttpUtils(){
        this.cm = new PoolingHttpClientConnectionManager();
        this.cm.setMaxTotal(100);
        this.cm.setDefaultMaxPerRoute(10);
    }
    public String doGetHtml(String url){
        String content=null;
        CloseableHttpClient HttpClient = HttpClients.custom().setConnectionManager(this.cm).build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");
        httpGet.setConfig(this.getConfig());
        CloseableHttpResponse response = null;
        try {
            response = HttpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == 200){
                System.out.println("1111111111111111111111111111111111");
                //判斷响应体是否为空
                if(response.getEntity()!=null){
                     content = EntityUtils.toString(response.getEntity(),"utf8");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }
    public String doGetImage(String url){
        String picName=null;
        CloseableHttpClient HttpClient =  HttpClients.custom().setConnectionManager(this.cm).build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(this.getConfig());
        CloseableHttpResponse response = null;
        try {
            response = HttpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == 200){
                //判斷响应体是否为空
                if(response.getEntity()!=null){
                    //下载图片
                    //获取图片后缀
                    String extName = url.substring(url.lastIndexOf("."));
                    //创建图片名 重命图片
                    picName = UUID.randomUUID().toString()+extName;
                     //下载图片
                    OutputStream outputStream = new FileOutputStream(new File("C:/Users/WL/Desktop/campus help/images/a/")+picName);
                    response.getEntity().writeTo(outputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return picName;
    }
    private RequestConfig getConfig() {
        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(2000)
                .setConnectionRequestTimeout(2000)
                .setSocketTimeout(20000)
                .build();
        return config;
    }
}
