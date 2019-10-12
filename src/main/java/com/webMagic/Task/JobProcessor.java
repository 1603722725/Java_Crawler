package com.webMagic.Task;

import com.google.common.hash.BloomFilter;
import com.webMagic.pojo.JobInfo;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * @Author: wl
 * @Date: 2019/10/11 19:24
 */

//@Component
public class JobProcessor implements PageProcessor {
    private String url= "https://search.51job.com/list/170200,000000,0000,00,9,99,%2520,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";
    @Override
    public void process(Page page) {
        //解析页面  获取招聘信息详情URL地址
        List<Selectable> list = page.getHtml().css("div#resultList div.el").nodes();
        //判断获取到的集合是否为空
        if(list.size() == 0){
            //为空  即为 招聘详情页
            this.saveJobInfo(page);
        }else{
            //列表页
            for (Selectable selectable:list){
                //获取url
                String jobInfoUrl = selectable.links().toString();
                //把获取的url地址放入任务队列
                page.addTargetRequest(jobInfoUrl);
//                System.out.println(url);
            }
            //获取下一页的url
            String bkUrl = page.getHtml().css("div.p_in li.bk").links().toString();
            System.out.println("6666666666666666666666"+bkUrl);
            //把url放入任务队列
            page.addTargetRequest(bkUrl);
        }
    }
    //解析招聘详情页面  保存数据
    private void saveJobInfo(Page page) {
        //创建招聘详情对象
        JobInfo jobInfo = new JobInfo();
        //解析页面
        Html html = page.getHtml();
        String mm = Jsoup.parse(html.css("div.cn p.ltype").toString()).text();
        jobInfo.setCompanyName(html.css("div.cn p.cname a","text").toString());

        jobInfo.setCompanyInfo(Jsoup.parse(html.css("div.tmsg").toString()).text());
        jobInfo.setJobName(html.css("div.cn h1","text").toString());
        jobInfo.setJobAddr(Jsoup.parse(html.css("div.bmsg p.fp").toString()).text());
        jobInfo.setJobInfo(Jsoup.parse(html.css("div.job_msg").toString()).text());
        jobInfo.setSalary(html.css("div.cn strong","text").toString());
        jobInfo.setUrl(html.css("div.cn p.cname").links().toString());
//        System.out.println(page.getUrl().toString());
        jobInfo.setCompanyAddr(mm.substring(0,mm.indexOf("|")));
        jobInfo.setTime(mm.substring(mm.length()-7,mm.length()-2));
        //保存结果  保存在内存中
        page.putField("jobInfo",jobInfo);
    }

    private Site site = Site.me()
            .setCharset("gbk")
            .setTimeOut(10*1000)
            .setRetrySleepTime(3000)  //重试间隔时间
            .setRetryTimes(3);        //重试次数
    @Override
    public Site getSite() {
        return site;
    }
    @Autowired
    private SpringDataPipeline springDataPipeline;
    @Scheduled(initialDelay = 1000,fixedDelay = 100*1000)
    public void process(){
        Spider.create(new JobProcessor())
        .addUrl(url)
        .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(1000000)))
        .thread(10)
        .addPipeline(this.springDataPipeline)
        .run();
    }


}
