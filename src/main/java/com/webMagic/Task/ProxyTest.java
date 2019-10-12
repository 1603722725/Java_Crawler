package com.webMagic.Task;

import org.springframework.scheduling.annotation.Scheduled;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

/**
 * @Author: wl
 * @Date: 2019/10/12 9:40
 * 使用代理爬取数据    解决ip封锁问题
 * https://proxy.mimvp.com/freesecret.php
 */
public class ProxyTest implements PageProcessor {
    @Scheduled(fixedDelay = 1000)
    public void Process(){
        //创建下载器Downloader
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        //给下载器设置代理信息
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("42.235.170.32",10946)));
        Spider.create(new ProxyTest())
                .addUrl("http://ip.chinaz.com/getip.aspx")
                .setDownloader(httpClientDownloader)
                .run();

    }
    @Override
    public void process(Page page) {
        System.out.println(page.getHtml().toString());
    }
    private Site site = Site.me();
    @Override
    public Site getSite() {
        return site;
    }
}
