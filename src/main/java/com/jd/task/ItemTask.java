package com.jd.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.pojo.Item;
import com.jd.servive.ItemService;
import com.jd.utils.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: wl
 * @Date: 2019/10/8 21:22
 * 定时任务  每隔一段时间 爬取数据
 *
 */
@Component
public class ItemTask {
    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private ItemService itemService;

    private static  final ObjectMapper MAPPER = new ObjectMapper();
    //当下载任务完成后   间隔多长时间进行下一次任务
    @Scheduled(fixedDelay = 100*1000)
    public void itemTask() throws Exception{
        String url = "https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&wq=%E6%89%8B%E6%9C%BA&cid2=653&cid3=655&page=";
        //遍历解析
        for (int i=1;i<10;i++){
//            System.out.println("url:"+url+i);
            String html = httpUtils.doGetHtml(url+i);
//            System.out.println("html:"+html);
            //解析页面  获取商品数据并存储
            this.parse(html);
        }
        System.out.println("手机数据抓取完成！！！");
    }
    //解析页面  获取数据并存储
    private void parse(String html)  throws Exception{
        Document document = Jsoup.parse(html);
        //获取spu信息  ipone X
        Elements spuEles = document.select("div#J_goodsList > ul> li");
        for(Element spuEle:spuEles){
            long spu = Long.parseLong(spuEle.attr("data-sku"));
            //获取sku信息
            Elements skuEles = spuEle.select("li.ps-item");
            for(Element skuEle:skuEles){
                //获取sku
                long sku = Long.parseLong(skuEle.select("[data-sku]").attr("data-sku"));
                //根据sku查询商品数据
                Item item = new Item();
                item.setSpu(spu);
                item.setSku(sku);
                List<Item> list = this.itemService.findAll(item);
                if(list.size()>0){
                    //如果该商品已经存在 就进行下一个循环 该商品不保存
                    continue;
                }
                //商品详情页的Url
                String itemUrl = "https://item.jd.com/"+sku+".html";
                item.setUrl(itemUrl);
                //获取商品图片
                String picUrl = "https:"+skuEle.select("img[data-sku]").first().attr("data-lazy-img");
                picUrl = picUrl.replace("/n9/","/n1/");
//                System.out.println("999999999999"+picUrl);
                if(picUrl.length()>7){
                    String picName = this.httpUtils.doGetImage(picUrl);
                    item.setPic(picName);
                }
                String priceJSON = this.httpUtils.doGetHtml("https://p.3.cn/prices/mgets?skuIds="+sku);
                double price = MAPPER.readTree(priceJSON).get(0).get("p").asDouble();
                item.setPrice(price);
                String itemInfo = this.httpUtils.doGetHtml(item.getUrl());
                String text = Jsoup.parse(itemInfo).select("div.sku-name").text();
                item.setTitle(text);
                item.setCreated(LocalDateTime.now());
                item.setUpdated(LocalDateTime.now());
                //保存商品到数据库中
                this.itemService.save(item);
            }
        }
    }
}
